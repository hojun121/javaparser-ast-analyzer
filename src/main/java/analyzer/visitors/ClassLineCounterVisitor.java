package analyzer.visitors;

import analyzer.AbstractVoidVisitorAdapter;
import analyzer.collectors.Collector;
import com.github.javaparser.ast.CompilationUnit;

public class ClassLineCounterVisitor extends AbstractVoidVisitorAdapter<Collector>  {

    private static final int MAX_CLASS_LENGTH = 1000;
    public void visit(CompilationUnit cu, Collector collector) {

        super.visit(cu, collector);

        int count = cu.toString().split("\n").length;

        if (count > MAX_CLASS_LENGTH) {
            collector.addWarning(className, "Class has more than " + MAX_CLASS_LENGTH + " lines");
        }

        collector.incrementMetric("Code Lines", count);
    }

}
