package analyzer.collectors;

public interface Collector {
    void addWarning(String className, String warning);
    void incrementMetric(String metricName);
    void incrementMetric(String metricName, int count);
}
