package analyzer.visitors;

import analyzer.AbstractVoidVisitorAdapter;
import analyzer.collectors.Collector;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

public class TooSmallVisitor extends AbstractVoidVisitorAdapter<Collector> {
    private static final int MIN_VARIABLE_LENGTH = 25;
    private static final int MIN_METHOD_NAME = 3;
    @Override
    public void visit(MethodDeclaration declaration, Collector collector) {
        if (declaration.getName().toString().length() < MIN_METHOD_NAME)
        super.visit(declaration, collector);
    }
    @Override
    public void visit(VariableDeclarationExpr declaration, Collector collector) {
        for (VariableDeclarator variable: declaration.getVariables()) {
            if (variable.getNameAsString().length() < MIN_VARIABLE_LENGTH) {
                collector.addWarning(className, "Variable \"" + variable.getNameAsString() + "\" length is too small");
            }
        }
    }
}
