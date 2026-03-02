package service;

import model.Post;
import org.apache.log4j.Logger;
import stores.JdbcStore;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Running {

    private static final Logger LOG = Logger.getLogger(Running.class);

    public static void main(String[] args) {

        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(config.get("db.url"),
                config.get("db.username"), config.get("db.password"));
             var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            Thread.sleep(10000);

        } catch (SQLException | InterruptedException e) {
            LOG.error("When create a connection", e);
        }
    }

}
