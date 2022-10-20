package analyzer.printers;

import analyzer.collectors.HashMapCollector;

import java.util.Map;

public class MetricPrinter extends AbstractPrinter implements Printable {

    public MetricPrinter(HashMapCollector collector) {
        super(collector);
    }

    /**
     * Print out metrics to the console
     */
    @Override
    public void print() {
        System.out.println("|--------------------------METRICS---------------------------------|");
        System.out.format("|%60s|%5s|\n", "Type", "Count");

        for (Map.Entry<String, Integer> entry: collector.getStats().entrySet()) {

            System.out.format("|%60s|%5s|\n", entry.getKey(), entry.getValue());
        }
    }
}
