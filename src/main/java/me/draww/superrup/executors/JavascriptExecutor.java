package me.draww.superrup.executors;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.script.*;
import java.io.File;
import java.util.Objects;

@SuppressWarnings("unused")
public class JavascriptExecutor implements Executor<Player> {

    @ActionField(type = "id")
    private String id;

    @ActionField(type = "queue")
    private Integer queue;

    @ActionField(type = "rank")
    private Rank rank;

    @ActionField(type = "file", required = true, custom = true)
    private String filePath;

    private String fileContent;

    @Override
    public boolean onSetup() {
        File jsFile = new File(Main.INSTANCE.getJsFolder(), filePath);
        fileContent = FileUtil.read(jsFile);
        return true;
    }

    @Override
    public void run(Player player) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine;
        try {
            NashornScriptEngineFactory factory = (NashornScriptEngineFactory) engineManager.getEngineFactories().stream().filter(factories -> "Oracle Nashorn".equalsIgnoreCase(factories.getEngineName())).findFirst().orElse(null);
            engine = Objects.requireNonNull(factory).getScriptEngine("-doe", "--global-per-engine");
        } catch (Exception ex) {
            engine = engineManager.getEngineByName("JavaScript");
        }

        SimpleBindings bindings = new SimpleBindings();
        bindings.put("player", player);
        bindings.put("bukkitServer", Bukkit.getServer());
        bindings.put("economy", Main.INSTANCE.getVaultEconomy());
        bindings.put("executor", this);

        try {
            CompiledScript compiledScript = ((Compilable) engine).compile(fileContent);
            compiledScript.eval(bindings);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Integer queue() {
        return queue;
    }

    @Override
    public Rank rank() {
        return rank;
    }
}
