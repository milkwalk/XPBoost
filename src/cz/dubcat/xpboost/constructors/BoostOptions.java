package cz.dubcat.xpboost.constructors;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BoostOptions {

    private boolean enabledByDefault = true;
    private String pluginName;
    private ConcurrentHashMap<String, Boolean> options = new ConcurrentHashMap<String, Boolean>();

    public BoostOptions(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public ConcurrentHashMap<String, Boolean> getOptions() {
        return options;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public void setEnabledByDefault(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public boolean isAllowedType(String expType) {
        if (options.containsKey(expType.toUpperCase()))
            return options.get(expType.toUpperCase());
        else
            return enabledByDefault;
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
