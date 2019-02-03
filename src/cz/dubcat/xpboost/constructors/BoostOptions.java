package cz.dubcat.xpboost.constructors;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BoostOptions {
    private boolean enabledByDefault = true;
    private String pluginName;
    private Map<String, Boolean> options = new ConcurrentHashMap<>();

    public BoostOptions(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public Map<String, Boolean> getOptions() {
        return options;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public void setEnabledByDefault(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public boolean isAllowedType(String expType) {
        return options.getOrDefault(expType.toUpperCase(), enabledByDefault);
    }

    @Override
    public String toString() {
        Set<Entry<String, Boolean>> tempSet = options.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : tempSet) {
            sb.append(entry.toString() + ",");
        }
        return "[" + sb.toString() + "DEFAULT=" + enabledByDefault + "]";
    }
}
