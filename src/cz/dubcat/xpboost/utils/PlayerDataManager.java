package cz.dubcat.xpboost.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerDataManager {
    private JavaPlugin plugin;
    
    public FileConfiguration getPlayerConfig(String player) {
        File playerYml = this.getPlayerFile(player);
        return YamlConfiguration.loadConfiguration(playerYml);
    }
    
    public void savePlayerConfig(String player, FileConfiguration config) {
        File guildYml = this.getPlayerFile(player);
        try {
            config.save(guildYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public File getPlayerFile(String player) {
        return new File(plugin.getDataFolder() + "/playerData/" + player + ".yml");
    }
}
