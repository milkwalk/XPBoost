package cz.dubcat.xpboost.api;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonElement;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.constructors.BoostOptions;
import cz.dubcat.xpboost.constructors.Database;
import cz.dubcat.xpboost.constructors.Database.DType;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;
import cz.dubcat.xpboost.constructors.XPBoostInventoryHolder;
import cz.dubcat.xpboost.utils.DbUtils;
import cz.dubcat.xpboost.utils.PlayerDataManager;
import cz.dubcat.xpboost.utils.XMaterial;

public class MainAPI {

    public enum Condition {
        VANILLA, SKILLAPI, MCMMO, RPGME, HEROES, JOBS;
        public static Condition[] CONDITIONS = new Condition[] { VANILLA, SKILLAPI, MCMMO, RPGME, HEROES, JOBS };
    }

    public static File playersyml;
    public static FileConfiguration playerCfg;
    public static PlayerDataManager playerData;;

    public static XPBoost loadPlayer(UUID uuid) {
        double boost = 0;
        long endtime = 0;
        HashMap<Condition, Boolean> bConditions = new HashMap<>();
        HashMap<String, BoostOptions> bOptions = new HashMap<>();

        if (Database.type == DType.FILE) {
            setPlayerFile(uuid);

            if (playersyml.exists()) {
                if (playerCfg.contains("xp.boost")) {
                    boost = (double) getPlayerVariable("xp.boost");
                }

                if (playerCfg.contains("xp.endtime")) {
                    endtime = (long) getPlayerVariable("xp.endtime");
                }

                if (playerCfg.contains("xp.condition")) {
                    for (String key : playerCfg.getConfigurationSection("xp.condition").getKeys(false)) {
                        bConditions.put(Condition.valueOf(key.toUpperCase()),
                                Boolean.valueOf(playerCfg.getString("xp.condition." + key)));
                    }
                }

                if (playerCfg.contains("xp.advanced")) {
                    for (String key : playerCfg.getConfigurationSection("xp.advanced").getKeys(false)) {
                        BoostOptions options = new BoostOptions(key);
                        for (String option : playerCfg.getConfigurationSection("xp.advanced." + key).getKeys(false)) {
                            if (option.equalsIgnoreCase("DEFAULT"))
                                options.setEnabledByDefault(
                                        (boolean) getPlayerVariable("xp.advanced." + key + "." + option));
                            else
                                options.getOptions().put(option,
                                        (boolean) getPlayerVariable("xp.advanced." + key + "." + option));
                        }

                        bOptions.put(key, options);
                    }
                }
            }

        } else {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = Database.getConnection().prepareStatement("SELECT * FROM xpboost WHERE uuid=?");
                ps.setString(1, uuid.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    boost = rs.getDouble("boost");
                    endtime = rs.getLong("endtime");

                    JSONObject condJson = null;
                    try {
                        condJson = (JSONObject) new JSONParser().parse(rs.getString("conditions"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // conditions
                    Set<Entry<String, JsonElement>> entrySet = condJson.entrySet();
                    if (condJson != null && condJson.size() > 0) {
                        for (Map.Entry<String, JsonElement> jsonSet : entrySet) {
                            bConditions.put(Condition.valueOf(jsonSet.getKey()),
                                    Boolean.valueOf(String.valueOf(jsonSet.getValue())));
                        }
                    }

                    // advanced
                    JSONParser parser = new JSONParser();
                    JSONObject advancJson = null;
                    try {
                        advancJson = (JSONObject) parser.parse(rs.getString("advanced"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Set<Entry<String, JsonElement>> advancedEntrySet = advancJson.entrySet();
                    if (condJson != null && condJson.size() > 0) {
                        for (Map.Entry<String, JsonElement> jsonSet : advancedEntrySet) {
                            BoostOptions option = new BoostOptions(jsonSet.getKey());
                            Set<Entry<String, JsonElement>> boostOptionEntrySet;
                            try {
                                boostOptionEntrySet = ((JSONObject) parser.parse(String.valueOf(jsonSet.getValue())))
                                        .entrySet();
                            } catch (ParseException e) {
                                e.printStackTrace();
                                continue;
                            }
                            for (Map.Entry<String, JsonElement> jsonSet2 : boostOptionEntrySet) {

                                if (jsonSet2.getKey().equalsIgnoreCase("DEFAULT"))
                                    option.setEnabledByDefault(Boolean.valueOf(String.valueOf(jsonSet2.getValue())));
                                else
                                    option.getOptions().put(jsonSet2.getKey(),
                                            Boolean.valueOf(String.valueOf(jsonSet2.getValue())));
                            }

                            bOptions.put(jsonSet.getKey(), option);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(ps);
            }
        }

        if (endtime > System.currentTimeMillis()) {

            MainAPI.debug("Loading boost for UUID: " + uuid.toString(), Debug.NORMAL);
            MainAPI.debug("  Boost: " + boost + " Until: " + endtime, Debug.NORMAL);
            MainAPI.debug("  Conditions: " + bConditions.toString(), Debug.NORMAL);
            MainAPI.debug("  Advanced options: " + bOptions.toString(), Debug.NORMAL);

            XPBoost xpb = new XPBoost(uuid, boost, endtime);
            xpb.getConditions().putAll(bConditions);
            xpb.getAdvancedOptions().putAll(bOptions);

            return xpb;
        } else {
            MainAPI.debug("Did not find any boost for UUID: " + uuid.toString(), Debug.NORMAL);
            return null;
        }

    }

    public static void savePlayer(UUID uuid) {
        if (XPBoostAPI.hasBoost(uuid)) {
            XPBoost xpb = XPBoostAPI.getBoost(uuid);

            if (Database.type == DType.FILE) {
                setPlayerFile(uuid);
                setPlayerVariable("xp", "");
                setPlayerVariable("xp.boost", xpb.getBoost());
                setPlayerVariable("xp.endtime", xpb.getEndtime());

                Iterator<Entry<Condition, Boolean>> it = xpb.getConditions().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Condition, Boolean> pair = it.next();
                    setPlayerVariable("xp.condition." + pair.getKey(), pair.getValue());
                    it.remove();
                }

                Iterator<Entry<String, BoostOptions>> it2 = xpb.getAdvancedOptions().entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, BoostOptions> pair = it2.next();
                    setPlayerVariable("xp.advanced." + pair.getKey() + ".DEFAULT",
                            pair.getValue().isEnabledByDefault());

                    Iterator<Entry<String, Boolean>> it3 = pair.getValue().getOptions().entrySet().iterator();
                    while (it3.hasNext()) {
                        Map.Entry<String, Boolean> pair2 = it3.next();
                        setPlayerVariable("xp.advanced." + pair.getKey() + "." + pair2.getKey(), pair2.getValue());
                    }
                    it2.remove();
                }

                savePlayerProfile();
            } else {
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    // removing old value
                    PreparedStatement psr = Database.getConnection()
                            .prepareStatement("DELETE FROM xpboost WHERE uuid=?");
                    psr.setString(1, uuid.toString());
                    psr.execute();
                    DbUtils.closeQuietly(psr);

                    // readding new value
                    ps = Database.getConnection().prepareStatement(
                            "INSERT INTO xpboost (uuid,boost,endtime,conditions,advanced) VALUES (?,?,?,?,?)");
                    ps.setString(1, uuid.toString());
                    ps.setDouble(2, xpb.getBoost());
                    ps.setLong(3, xpb.getEndtime());

                    JSONObject json = new JSONObject();
                    Iterator<Entry<Condition, Boolean>> it = xpb.getConditions().entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Condition, Boolean> pair = it.next();
                        json.put(pair.getKey(), pair.getValue());
                        it.remove();
                    }
                    ps.setString(4, json.toJSONString());

                    json.clear();
                    Iterator<Entry<String, BoostOptions>> it2 = xpb.getAdvancedOptions().entrySet().iterator();
                    while (it2.hasNext()) {
                        JSONObject jsonObject = new JSONObject();

                        Map.Entry<String, BoostOptions> firstPair = it2.next();
                        Iterator<Entry<String, Boolean>> it3 = firstPair.getValue().getOptions().entrySet().iterator();
                        while (it3.hasNext()) {
                            Map.Entry<String, Boolean> pair = it3.next();

                            jsonObject.put(pair.getKey(), pair.getValue());
                            it3.remove();
                        }

                        jsonObject.put("DEFAULT", firstPair.getValue().isEnabledByDefault());
                        json.put(firstPair.getKey(), jsonObject.toJSONString());
                        it2.remove();
                    }

                    ps.setString(5, json.toJSONString());
                    ps.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DbUtils.closeQuietly(rs);
                    DbUtils.closeQuietly(ps);
                }
            }
        }
    }

    public static File setPlayerFile(UUID uuid) {
        playersyml = new File(XPBoostMain.getPlugin().getDataFolder() + "/players/" + uuid + ".yml");
        playerCfg = YamlConfiguration.loadConfiguration(playersyml);

        return playersyml;
    }

    public static void setPlayerVariable(String cesta, Object variable) {
        playerCfg.set(cesta, variable);
        saveCustomYml(playerCfg, playersyml);
    }

    public static void savePlayerProfile() {
        saveCustomYml(playerCfg, playersyml);
    }

    public static Object getPlayerVariable(String cesta) {
        return playerCfg.get(cesta);
    }

    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void debug(String srt, Debug val) {
        if (XPBoostMain.debug != Debug.OFF && XPBoostMain.debug.getValue() >= val.getValue()) {
            XPBoostMain.getLog().info(srt);
        }
    }

    public static void sendMessage(String string, UUID player) {
        sendMessage(string, Bukkit.getServer().getPlayer(player));
    }

    public static void sendMessage(String string, CommandSender sender) {
        sender.sendMessage(colorizeText(XPBoostMain.getLang().getString("lang.prefix") + string));
    }

    public static String colorizeText(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String stripColours(String string) {
        return ChatColor.stripColor(string);
    }

    public static void createDisplay(Material material, Inventory inv, int Slot, String name, List<String> lore) {
        createDisplay(material, inv, Slot, name, lore, false);
    }

    public static void createDisplay(Material material, Inventory inv, int Slot, String name, List<String> lore,
            boolean enchanted) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorizeText(name));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);

        List<String> itemlore = new ArrayList<String>();

        for (String s : lore)
            itemlore.add(colorizeText(s));

        if (enchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }

        meta.setLore(itemlore);
        item.setItemMeta(meta);
        inv.setItem(Slot, item);
    }

    public static void openXpBoostShop(Player player) {
        int i = -1;
        int amount = 1;
        for (String key : XPBoostMain.boostCfg.getConfigurationSection("").getKeys(false)) {
            if (XPBoostMain.boostCfg.getBoolean(key + ".enabled") == true) {
                amount++;
            }
        }

        if (amount > 9) {
            amount = 18;
        } else if (amount > 18) {
            amount = 27;
        } else if (amount > 27) {
            amount = 36;
        } else if (amount > 36) {
            amount = 45;
        } else { 
            amount = 9;
        }

        Inventory GUI = Bukkit.createInventory(new XPBoostInventoryHolder(), amount, colorizeText(XPBoostMain.getLang().getString("lang.gui")));
        for (String key : XPBoostMain.boostCfg.getConfigurationSection("").getKeys(false)) {
            if (XPBoostMain.boostCfg.getBoolean(key + ".enabled") == true) {
                i++;
                int cost = XPBoostMain.boostCfg.getInt(key + ".cost");
                int time = XPBoostMain.boostCfg.getInt(key + ".time");
                double boost = XPBoostMain.boostCfg.getDouble(key + ".boost");
                Material defaultMaterial = XMaterial.EXPERIENCE_BOTTLE.parseMaterial();

                if (XPBoostMain.boostCfg.contains(key + ".item_type")) {
                    try {
                        defaultMaterial = Material.valueOf(XPBoostMain.boostCfg.getString(key + ".item_type"));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        XPBoostMain.getLog().log(Level.SEVERE, "ItemType '" + XPBoostMain.boostCfg.getString(key + ".item_type")
                                + "' doesnt exist! Boost: " + key);
                    }
                }

                List<String> lore = XPBoostMain.getLang().getStringList("lang.boostlore");

                if (XPBoostMain.boostCfg.contains(key + ".lore")) {
                    lore = XPBoostMain.boostCfg.getStringList(key + ".lore");
                }

                MainAPI.createDisplay(defaultMaterial, GUI, i,
                        (XPBoostMain.boostCfg.getString(key + ".title") != null)
                                ? XPBoostMain.boostCfg.getString(key + ".title").replaceAll("%boost%", boost + "")
                                        .replaceAll("%money%", cost + "").replaceAll("%time%", time + "")
                                : XPBoostMain.getLang().getString("lang.xptitle").replaceAll("%boost%", boost + ""),
                        processListPlaceholders(lore, time, boost, cost),
                        (XPBoostMain.boostCfg.contains(key + ".glowing")) ? XPBoostMain.boostCfg.contains(key + ".glowing") : false);
            }
        }

        player.openInventory(GUI);
    }

    public static String replacePlaceholders(String s, int time, double boost, double cost) {
        return colorizeText(s.replaceAll("%time%", String.valueOf(time)).replaceAll("%money%", String.valueOf(cost))
                .replaceAll("%boost%", String.valueOf(boost)));
    }

    public static List<String> processListPlaceholders(List<String> s, int time, double boost, double cost) {
        for (int i = 0; i < s.size(); i++) {
            s.set(i, replacePlaceholders(s.get(i), time, boost, cost));
        }
        return s;
    }
}
