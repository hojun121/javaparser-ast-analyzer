package analyzer.collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapCollector implements Collector {
    protected static HashMap<String, List<String>> warnings;
    protected static HashMap<String, Integer> stats;
    public static HashMap<String, List<String>> getWarnings() {
        return warnings;
    }
    public static HashMap<String, Integer> getStats() {
        return stats;
    }
    public HashMapCollector() {
        warnings = new HashMap<>();
        stats = new HashMap<>();
    }
    public void addWarning(String className, String warning) {
        if (warnings.containsKey(className)) {
            warnings.get(className).add(warning);
        } else {
            warnings.put(className, new ArrayList<String>() {{
                add(warning);
            }});
        }

    }

    public void incrementMetric(String metricName) {
        if (stats.containsKey(metricName)) {
            stats.put(metricName, stats.get(metricName) + 1);
        } else {
            stats.put(metricName, 1);
        }
    }

    public void incrementMetric(String metricName, int count) {
        if (stats.containsKey(metricName)) {
            stats.put(metricName, stats.get(metricName) + count);
        } else {
            stats.put(metricName, count);
        }
    }


}
