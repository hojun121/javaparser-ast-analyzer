package analyzer.printers;

import analyzer.collectors.HashMapCollector;

public abstract class AbstractPrinter {

    protected HashMapCollector collector;

    public AbstractPrinter(HashMapCollector collector) {

        this.collector = collector;
    }


}
