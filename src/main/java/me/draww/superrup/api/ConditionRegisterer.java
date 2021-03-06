package me.draww.superrup.api;

import me.draww.superrup.Main;
import me.draww.superrup.Rank;
import me.draww.superrup.api.Condition;
import me.draww.superrup.api.annotations.ActionField;
import me.draww.superrup.api.exception.ActionException;
import me.draww.superrup.conditions.ExpCondition;
import me.draww.superrup.conditions.ItemCondition;
import me.draww.superrup.conditions.MoneyCondition;
import me.draww.superrup.conditions.PlayTimeCondition;
import me.draww.superrup.utils.ItemUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConditionRegisterer {

    private Map<String, Class> conditionsClasses;

    public ConditionRegisterer() {
        setupFirst();
    }

    private void setupFirst() {
        conditionsClasses = new HashMap<>();
        register("exp", ExpCondition.class);
        register("item", ItemCondition.class);
        register("money", MoneyCondition.class);
        register("play_time", PlayTimeCondition.class);
    }

    public void register(String id, Class clazz) {
        if (!conditionsClasses.containsKey(id)) conditionsClasses.put(id, clazz);
    }

    public boolean isRegistered(String id) {
        return conditionsClasses.containsKey(id);
    }

    public void reload() {
        setupFirst();
    }

    public Map<String, Condition> deserializeConditions(ConfigurationSection section, Rank rank) {
        if (section == null) return new HashMap<>();
        Map<String, Condition> conditionMap = new HashMap<>();
        for (String condKey : section.getKeys(false)) {
            if (section.contains(condKey + ".template") && section.isString(condKey + ".template")) {
                if (section.contains(condKey + ".queue") && section.isInt(condKey + ".queue")) {
                    ConfigurationSection templateSection = Main.INSTANCE.getTemplateConfig().getConfigurationSection("conditions." + section.getString(condKey + ".template"));
                    if (!templateSection.contains("type") && !templateSection.isString("type")) continue;
                    try {
                        Condition condition = createNewCondition(templateSection.getString("type"));
                        if (condition == null) {
                            Main.INSTANCE.getLogger().severe("The action type is wrong! (" + condKey + ")");
                            continue;
                        }
                        setupFields(condition, condKey, templateSection, rank, section.getInt(condKey + ".queue"));
                        condition.onSetup();
                        conditionMap.put(condKey, condition);
                    } catch (IllegalAccessException | InstantiationException | ActionException e) {
                        if (e instanceof ActionException) {
                            Main.INSTANCE.getLogger().severe(e.getMessage());
                            continue;
                        }
                        e.printStackTrace();
                        continue;
                    }
                } else {
                    Main.INSTANCE.getLogger().severe("The queue number is undetermined! (" + condKey + ")");
                }
            } else {
                ConfigurationSection conditionSection = section.getConfigurationSection(condKey);
                if (!conditionSection.contains("type") && !conditionSection.isString("type")) continue;
                try {
                    Condition condition = createNewCondition(conditionSection.getString("type"));
                    if (condition == null) {
                        Main.INSTANCE.getLogger().severe("The action type is wrong! (" + condKey + ")");
                        continue;
                    }
                    setupFields(condition, condKey, conditionSection, rank, null);
                    condition.onSetup();
                    conditionMap.put(condKey, condition);
                } catch (IllegalAccessException | InstantiationException | ActionException e) {
                    if (e instanceof ActionException) {
                        Main.INSTANCE.getLogger().severe(e.getMessage());
                        continue;
                    }
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return conditionMap;
    }

    private Condition createNewCondition(String type) throws IllegalAccessException, InstantiationException {
        type = type.trim().toLowerCase();
        if (conditionsClasses.containsKey(type)) {
            Class clazz = conditionsClasses.get(type);
            return (Condition) clazz.newInstance();
        }
        return null;
    }

    private void setupFields(Condition condition, String id, ConfigurationSection section, Rank rank, Integer queue) throws ActionException {
        List<Field> annotatedFields = Arrays.stream(condition.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(ActionField.class)).collect(Collectors.toList());
        Map<String, Object> replaceData = section.getValues(true);
        for (Field field : annotatedFields) {
            ActionField fieldAnnotation = field.getAnnotation(ActionField.class);
            String fieldType = fieldAnnotation.type();
            boolean fieldRequired = fieldAnnotation.required();
            boolean fieldCustom = fieldAnnotation.custom();
            boolean replaceVariables = fieldAnnotation.replaceVariables();
            if (fieldType.equals("id")) {
                field.setAccessible(true);
                try {
                    field.set(condition, id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } finally {
                    field.setAccessible(false);
                }
            } else if (fieldType.equals("rank")) {
                field.setAccessible(true);
                try {
                    field.set(condition, rank);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } finally {
                    field.setAccessible(false);
                }
            } else if (fieldType.equals("queue")) {
                if (queue == null && (!section.contains("queue") || !section.isInt("queue")))
                    throw new ActionException(condition, "The queue number is undetermined! (" + id + ")");
                field.setAccessible(true);
                try {
                    field.set(condition, queue == null ? section.getInt("queue") : queue);
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
                        else
                            throw new ActionException(condition, fieldType + " item information is missing! (" + id + ")");
                    }
                    ItemStack item = ItemUtil.deserializeItemStack(section.getConfigurationSection(fieldType), rank);
                    field.setAccessible(true);
                    try {
                        field.set(condition, item);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        field.setAccessible(false);
                    }
                } else {
                    if (!section.contains(fieldType)) {
                        if (!fieldRequired) continue;
                        else
                            throw new ActionException(condition, fieldType + " custom data information is missing! (" + id + ")");
                    }
                    Object customData = section.get(fieldType);
                    if (replaceVariables && customData instanceof String) {
                        if (replaceData != null) {
                            for (Map.Entry<String, Object> entry : replaceData.entrySet()) {
                                customData = ((String) customData).replace("%" + entry.getKey().toLowerCase() + "%", String.valueOf(entry.getValue()));
                            }
                        }
                    }
                    field.setAccessible(true);
                    try {
                        field.set(condition, customData);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }

    public static boolean testAllConditions(Player player, Rank rank) {
        for (Condition condition : rank.getSortedConditions()) {
            if (!condition.test(player)) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Class> getConditionsClasses() {
        return conditionsClasses;
    }
}
