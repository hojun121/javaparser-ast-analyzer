package analyzer.visitors;

import analyzer.AbstractVoidVisitorAdapter;
import analyzer.collectors.Collector;
import com.github.javaparser.ast.body.MethodDeclaration;

public class BooleanMethodVisitor extends AbstractVoidVisitorAdapter<Collector> {
    @Override
    public void visit(MethodDeclaration declaration, Collector collector) {

        if (declaration.getType().toString().equals("boolean") && declaration.getParameters().size() == 0) {

            if (!declaration.getNameAsString().startsWith("is")) {

                collector.addWarning(className, "Method name \"" + declaration.getType().toString() + "\"  should start with is, e.g.\"isSomething()\"");

            }
        }

    }


}
