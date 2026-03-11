package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stores.JdbcStore;
import stores.Store;
import utils.HabrCareerDateTimeParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Running {
    private static Connection connection;
    private static SchedulerManager scheduler;
    private static Web webServer;
    private static final Logger LOG = LoggerFactory.getLogger(Running.class);

    public static void main(String[] args) {
        try {
            LOG.info("Запуск приложения");
            var config = new Config();
            config.load("application.properties");
            connection = createConnection(config);
            var store = new JdbcStore(connection);
            config.load("application.properties");
            scheduler = startScheduler(config, store);
            webServer = startWebServer(store, config);
            registerShutdownHook();
        } catch (SQLException e) {
            LOG.error("Не удалось подключиться к базе данных", e);
        }
    }

    private static Connection createConnection(Config config) throws SQLException {
        String url = config.get("db.url");
        String username = config.get("db.username");
        String password = config.get("db.password");
        Connection conn = DriverManager.getConnection(url, username, password);
        LOG.info("Подключение к базе данных: {}", url);
        return conn;
    }

    private static SchedulerManager startScheduler(Config config, Store store) {
        HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        var scheduler = new SchedulerManager();
        var interval = Integer.parseInt(config.get("rabbit.interval"));
        scheduler.init();
        scheduler.load(
                interval,
                SuperJobGrab.class,
                parse, store);
        LOG.info("Планировщик запущен с интервалом {}", interval);
        return scheduler;
    }

    private static Web startWebServer(Store store, Config config) {
        Web server = new Web(store);
        server.start(Integer.parseInt(config.get("server.port")));
        LOG.info("Сервер запущен");
        return server;
    }

    static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(Running::close));
    }

    private static void close() {
        LOG.info("Завершение работы приложения");
        if (scheduler != null) {
            try {
                scheduler.close();
            } catch (Exception e) {
                LOG.error("Ошибка при остановке планировщика", e);
            }
            if (webServer != null) {
                try {
                    webServer.stop();
                } catch (Exception e) {
                    LOG.error("Ошибка при остановке сервера", e);
                }
            }
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    LOG.info("Соединение с базой данных закрыто");
                }
            } catch (SQLException e) {
                LOG.error("Ошибка при закрытии соединения с базой данных", e);
            }
        }
    }
}

