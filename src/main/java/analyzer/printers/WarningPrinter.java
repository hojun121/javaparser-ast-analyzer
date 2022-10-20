package analyzer.printers;

import analyzer.collectors.HashMapCollector;

import java.util.List;
import java.util.Map;

public class WarningPrinter extends AbstractPrinter implements Printable {
    public WarningPrinter(HashMapCollector collector) {
        super(collector);
    }
    @Override
    public void print() {
        System.out.println("|---------------------------WARNINGS-------------------------------|");

        if (collector.getWarnings().entrySet().size() == 0) {

            System.out.println("No warnings");

        } else {
            System.out.println("there are " + collector.getWarnings().entrySet().size()+ " warnings you need to fix ");

        }
        for (Map.Entry<String, List<String>> entry: collector.getWarnings().entrySet()) {

            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " warnings");

            for (String warning: entry.getValue()) {

                System.out.println("[WARNING] " + warning);
            }
            System.out.println();
        }
    }
}
