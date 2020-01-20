package me.draww.superrup.executors;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.utils.FileUtil;
import me.draww.superrup.utils.Text;
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

    @ActionField(type = "file", custom = true)
    private String filePath;

    @ActionField(type = "value", custom = true)
    private String scripts;

    private String fileContent;

    private ScriptEngineManager engineManager = new ScriptEngineManager();
    private ScriptEngine engine;

    @Override
    public void onSetup() throws ActionException {
        if (filePath != null) {
            File jsFile = new File(Main.INSTANCE.getJsFolder(), filePath + ".js");
            if (!jsFile.exists()) throw new ActionException(this, filePath + ".js file is not exist");
            fileContent = FileUtil.read(jsFile);
        } else {
            fileContent = scripts;
        }
        try {
            NashornScriptEngineFactory factory = (NashornScriptEngineFactory) engineManager.getEngineFactories().stream().filter(factories -> "Oracle Nashorn".equalsIgnoreCase(factories.getEngineName())).findFirst().orElse(null);
            engine = Objects.requireNonNull(factory).getScriptEngine("-doe", "--global-per-engine");
        } catch (Exception ex) {
            engine = engineManager.getEngineByName("JavaScript");
        }
    }

    @Override
    public void run(Player player) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("player", player);
        bindings.put("bukkitServer", Bukkit.getServer());
        bindings.put("economy", Main.INSTANCE.getVaultEconomy());
        bindings.put("executor", this);
        bindings.put("TextUtil", StaticClass.forClass(Text.class));

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
