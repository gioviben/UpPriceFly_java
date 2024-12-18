package org.example;

import java.util.HashMap;
import java.util.Map;

public class MessageConfiguration {
    private Map<String, String> config;

    public MessageConfiguration() {
        this.config = new HashMap<>();
    }

    public void addConfig(String key, String value) {
        this.config.put(key, value);
    }

    public String getConfig(String key) {
        return this.config.get(key);
    }

}

