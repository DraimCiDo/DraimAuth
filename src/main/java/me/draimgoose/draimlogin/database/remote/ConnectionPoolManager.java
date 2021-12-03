package me.draimgoose.draimlogin.database.remote;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.database.SqlManager;

public abstract class ConnectionPoolManager extends SqlManager {
    @Getter
    @Setter
    private HikariDataSource dataSource;

    @Getter
    private final HikariConfig hikariConfig = new HikariConfig();

    public ConnectionPoolManager(DraimLogin plugin) {
        super(plugin);
        setupPool();
    }

    private void setupPool() {
        hikariConfig.setConnectionTimeout(30000);
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}

