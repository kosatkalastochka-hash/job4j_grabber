package service;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stores.Store;

public class Web {
    private Store store;
    private static final Logger LOG = LoggerFactory.getLogger(Web.class);

    public Web(Store store) {
        this.store = store;
    }

    Javalin app = itialization();

    public Javalin itialization() {
        return  Javalin.create(config -> {
            config.http.defaultContentType = "text/html; charset=utf-8";
            config.routes.get("/", ctx -> {
                var page = new StringBuilder();
                store.getAll().forEach(post -> page.append(post.toString().replaceAll(System.lineSeparator(), "<br/>")).append("<br/><br/>"));
                ctx.contentType("text/html; charset=utf-8");
                ctx.result(page.toString());
                LOG.info("Инициализация Web завершена");
            });
        });
    }

    public void start(int port) {
        app.start(port);
        LOG.info("Сервер запущен на порту {}", port);
    }

    public void stop() {
        app.stop();
        LOG.info("Сервер остановлен");
    }
}
