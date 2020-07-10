package cz.dubcat.xpboost.constructors;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cz.dubcat.xpboost.XPBoostMain;
import lombok.Getter;

public class Database {
    private final String table_sql = "CREATE TABLE IF NOT EXISTS `xpboost` ("
            + "`id` int(11) NOT NULL AUTO_INCREMENT,"
            + "`uuid` varchar(100) CHARACTER SET utf8 COLLATE utf8_czech_ci NOT NULL," + "`boost` double(8,2) NOT NULL,"
            + "`endtime` bigint(20) NOT NULL," + "`conditions` tinytext CHARACTER SET utf8 COLLATE utf8_czech_ci,"
            + "`advanced` text CHARACTER SET utf8 COLLATE utf8_czech_ci,"
            + "`inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," + " PRIMARY KEY (`id`),"
            + "UNIQUE KEY `uuid` (`uuid`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

    public enum DType {
        MYSQL, FILE;
    }
    
    @Getter
    private static DType databaseType = DType.FILE;
    @Getter
    private static HikariDataSource hikariDataSource;

    public boolean loadMysql() {
        try {
            HikariConfig config = new HikariConfig();
            String connectionHost = XPBoostMain.getPlugin().getConfig().getString("database.host");
            boolean verifySsl = true;
            if(XPBoostMain.getPlugin().getConfig().contains("database.ssl")) {
                verifySsl = XPBoostMain.getPlugin().getConfig().getBoolean("database.ssl");
            }
            config.setPoolName("dubcat-pool");
            config.setJdbcUrl("jdbc:mysql://"+connectionHost+":" + XPBoostMain.getPlugin().getConfig().getString("database.port") +"/" + 
                    XPBoostMain.getPlugin().getConfig().getString("database.database") + (verifySsl ? "?verifyServerCertificate=false&useSSL=true" : ""));
            config.setUsername(XPBoostMain.getPlugin().getConfig().getString("database.user"));
            config.setPassword(XPBoostMain.getPlugin().getConfig().getString("database.password"));
            config.setMinimumIdle(1);
            config.setMaximumPoolSize(4);
            hikariDataSource = new HikariDataSource(config);
            databaseType = DType.MYSQL;
            
            try(Connection conn = getConnection()) {
                conn.prepareStatement(table_sql).execute();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            XPBoostMain.getPlugin().getLogger().severe("Could not connect to the database. Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(XPBoostMain.getPlugin());
        }
        return false;
    }

    public static Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
