import analyzer.collectors.HashMapCollector;
import analyzer.printers.MetricPrinter;
import analyzer.printers.WarningPrinter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Analyzer {

    public static void main(String[] args) throws Exception {
        String filePath =  "src/main/resources/demo.java";

        HashMapCollector collector = new HashMapCollector();

        Files.walkFileTree(Paths.get(filePath), new analyzer.Analyzer(collector));

        new WarningPrinter(collector).print();
        new MetricPrinter(collector).print();
    }
}
