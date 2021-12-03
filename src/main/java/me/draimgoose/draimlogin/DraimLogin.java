package me.draimgoose.draimlogin;

// Лобок
import lombok.Getter;
// ноунейм
import me.draimgoose.draimlogin.data.Task;
import me.draimgoose.draimlogin.database.SQLite;
import me.draimgoose.draimlogin.database.SqlManager;
import me.draimgoose.draimlogin.database.remote.MYSQL;
import me.draimgoose.draimlogin.spigot.command.TelegramCommandHandler;
import me.draimgoose.draimlogin.spigot.command.TgCommandTabCompleter;
import me.draimgoose.draimlogin.telegram.TelegramBot;
// букит
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
// телега
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

// джава
import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.Executor;

@Getter
public class DraimLogin extends JavaPlugin {
    @Getter
    private static DraimLogin instance;
    private Thread botThread;
    private TelegramBotsApi botsApi;
    private TelegramBot bot;
    private SqlManager sql;
    private File langFile;
    private FileConfiguration langConfig;
    private boolean bungeeEnabled = false;
    private boolean loginSessionEnabled = false;
    private final Executor executor = runnable -> Bukkit.getScheduler().runTaskAsynchronously(this, runnable);

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.createLangFile("ru_RU");
        this.loadLangConfig();

        this.setupDb();

        getCommand("draimlogin").setExecutor(new TelegramCommandHandler(this));
        getCommand("draimlogin").setTabCompleter(new TgCommandTabCompleter(this));
        if (this.getConfig().getBoolean("bungee")) {
            this.bungeeEnabled = true;
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "hxj:draimlogin");
        }
        if (this.getConfig().getBoolean("login-session"))
            this.loginSessionEnabled = true;

        this.startBot();
        new Task(this).startClearCacheTask();

        getLogger().info("---------------------------------------");
        getLogger().info("DraimLogin by DraimGooSe");
        getLogger().info("Версия: " + this.getDescription().getVersion());
        getLogger().info("Дискорд: https://discord.gg/TBeK3nE");
        getLogger().info("Плагин успешно запущен!");
        getLogger().info("---------------------------------------");
    }

    private void setupDb() {
        String database = getConfig().getString("MySQL.Database");
        String username = getConfig().getString("MySQL.Username");
        String password = getConfig().getString("MySQL.Password");
        String host = getConfig().getString("MySQL.Host");
        int port = getConfig().getInt("MySQL.Port");
        boolean ssl = getConfig().getBoolean("MySQL.SSL");
        sql = getConfig().getBoolean("MySQL.Enable") ? new MYSQL(this, database, username, password, host, port, ssl) : new SQLite(this);
        sql.setup();
        try {
            sql.createTables();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("MySQL.Enable")) {
            MYSQL mysql = (MYSQL) sql;
            mysql.closePool();
        }
        botThread.stop();
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void startBot() {
        botThread = new Thread(() -> {
            try {
                botsApi = new TelegramBotsApi(DefaultBotSession.class);
                bot = new TelegramBot(this);
                botsApi.registerBot(bot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
        botThread.start();
    }

    private void createLangFile(String... names) {
        for (String name : names) {
            if (!new File(getDataFolder(), "lang" + File.separator + name + ".yml").exists()) {
                saveResource("lang" + File.separator + name + ".yml", false);
            }
        }
    }

    public void loadLangConfig() {
        langFile = new File(getDataFolder(), "lang" + File.separator + getConfig().getString("lang") + ".yml");
        if (!langFile.exists()) {
            langFile = new File(getDataFolder(), "lang" + File.separator + "ru_RU.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }
}
