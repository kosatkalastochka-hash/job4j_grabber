package service;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Logger LOG = Logger.getLogger(Config.class);
    private final Properties properties = new Properties();

    public void load(String file) {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            LOG.error(String.format("When load file : %s", file), e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
