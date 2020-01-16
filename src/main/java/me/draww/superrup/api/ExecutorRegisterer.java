package me.draww.superrup.api;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.api.Executor;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.executors.*;
import me.draww.superrup.utils.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class ExecutorRegisterer {

    private Map<String, Class> executorsClasses;

    public ExecutorRegisterer() {
        setupFirst();
    }

    private void setupFirst() {
        executorsClasses = new HashMap<>();
        register("message", MessageExecutor.class);
        register("actionbar_message", ActionbarMessageExecutor.class);
        register("broadcast_message", BroadcastMessageExecutor.class);
        register("broadcast_actionbar_message", BroadcastActionbarMessageExecutor.class);
        register("console_command", ConsoleCommandExecutor.class);
        register("player_command", PlayerCommandExecutor.class);
        register("withdraw_money", WithdrawMoneyExecutor.class);
        register("deposit_money", DepositMoneyExecutor.class);
        register("give_item", GiveItemExecutor.class);
        register("remove_item", RemoveItemExecutor.class);
        register("sound", SoundExecutor.class);
        register("teleport", TeleportExecutor.class);
        register("js", JavascriptExecutor.class);
    }

    public void register(String id, Class clazz) {
        if (!executorsClasses.containsKey(id)) executorsClasses.put(id, clazz);
    }

    public boolean isRegistered(String id) {
        return executorsClasses.containsKey(id);
    }

    public void reload() {
        setupFirst();
    }

    public Map<String, Executor> deserializeExecutors(ConfigurationSection section, Rank rank) {
        if (section == null) return new HashMap<>();
        Map<String, Executor> executorMap = new HashMap<>();
        for (String executorKey : section.getKeys(false)) {
            if (section.contains(executorKey + ".template") && section.isString(executorKey + ".template")) {
                ConfigurationSection templateSection = Main.INSTANCE.getTemplateConfig().getConfigurationSection("executors." + section.getString(executorKey + ".template"));
                if (!templateSection.contains("type") && !templateSection.isString("type")) continue;
                try {
                    Executor executor = createNewExecutor(templateSection.getString("type"));
                    if (executor == null) {
                        //TODO: error log
                        continue;
                    }
                    boolean successfullyLoaded = setupFields(executor, executorKey, templateSection, rank);
                    if (!successfullyLoaded) continue;
                    executor.onSetup();
                    executorMap.put(executorKey, executor);
                } catch (IllegalAccessException | InstantiationException | ActionException e) {
                    e.printStackTrace();
                    continue;
                }
            } else {
                ConfigurationSection executorSection = section.getConfigurationSection(executorKey);
                if (!executorSection.contains("type") && !executorSection.isString("type")) continue;
                try {
                    Executor executor = createNewExecutor(executorSection.getString("type"));
                    if (executor == null) {
                        //TODO: error log
                        continue;
                    }
                    boolean successfullyLoaded = setupFields(executor, executorKey, executorSection, rank);
                    if (!successfullyLoaded) continue;
                    executor.onSetup();
                    executorMap.put(executorKey, executor);
                } catch (IllegalAccessException | InstantiationException | ActionException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return executorMap;
    }

    private Executor createNewExecutor(String type) throws IllegalAccessException, InstantiationException {
        type = type.trim().toLowerCase();
        if (executorsClasses.containsKey(type)) {
            Class clazz = executorsClasses.get(type);
            return (Executor) clazz.newInstance();
        }
        return null;
    }

    private boolean setupFields(Executor executor, String id, ConfigurationSection section, Rank rank) {
        List<Field> annotatedFields = Arrays.stream(executor.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(ActionField.class)).collect(Collectors.toList());
        for (Field field : annotatedFields) {
            ActionField fieldAnnotation = field.getAnnotation(ActionField.class);
            String fieldType = fieldAnnotation.type();
            boolean fieldRequired = fieldAnnotation.required();
            boolean fieldCustom = fieldAnnotation.custom();
            if (fieldType.equals("id")) {
                field.setAccessible(true);
                try {
                    field.set(executor, id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } finally {
                    field.setAccessible(false);
                }
            } else if (fieldType.equals("rank")) {
                field.setAccessible(true);
                try {
                    field.set(executor, rank);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } finally {
                    field.setAccessible(false);
                }
            } else if (fieldType.equals("queue")) {
                if (!section.contains("queue") || !section.isInt("queue")) return false;
                field.setAccessible(true);
                try {
                    field.set(executor, section.getInt("queue"));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } finally {
                    field.setAccessible(false);
                }
            } else if (fieldCustom) {
                if (field.getType().isAssignableFrom(ItemStack.class)) {
                    if (!section.contains(fieldType) || !section.isConfigurationSection(fieldType)) {
                        if (!fieldRequired) continue;
                        else {
                            //TODO: error print
                            return false;
                        }
                    }
                    ItemStack item = ItemUtil.deserializeItemStack(section.getConfigurationSection(fieldType), rank);
                    field.setAccessible(true);
                    try {
                        field.set(executor, item);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        field.setAccessible(false);
                    }
                } else {
                    if (!section.contains(fieldType)) {
                        if (!fieldRequired) continue;
                        else {
                            //TODO: error print
                            return false;
                        }
                    }
                    Object customData = section.get(fieldType);
                    field.setAccessible(true);
                    try {
                        field.set(executor, customData);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
        return true;
    }

    public static void runAllExecutors(Player player, Rank rank) {
        for (Executor executor : rank.getSortedExecutors()) {
            executor.run(player);
        }
    }

    public Map<String, Class> getExecutorsClasses() {
        return executorsClasses;
    }
}
