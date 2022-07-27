package org.khasanof.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyConfig {
    private static Properties p;

    static {
        load();
    }

    private static void load() {
        try (FileReader fileReader = new FileReader("src/main/resources/application.properties")) {
            p = new Properties();
            p.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return p.getProperty(key);
    }
}
