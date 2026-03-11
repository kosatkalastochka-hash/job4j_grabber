package service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    private final Properties properties = new Properties();

    public void load(String file) {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            LOG.error("Ошибка при загрузке файла {}", file, e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
