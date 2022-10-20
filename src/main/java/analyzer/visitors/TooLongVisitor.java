package analyzer.visitors;

import analyzer.AbstractVoidVisitorAdapter;
import analyzer.collectors.Collector;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

public class TooLongVisitor extends AbstractVoidVisitorAdapter<Collector> {
    public static final int MAX_BODY_LENGTH = 1000;
    private static final int MAX_METHOD_NAME_LENGTH = 50;
    private static final int MAX_PARAM_COUNT = 8;
    private static final int MAX_VARIABLE_LENGTH = 20;
    private static final int MAX_VARIABLE_COUNT =  15;
    private static final int MAX_METHODS_COUNT = 25;
    @Override
    public void visit(MethodCallExpr declaration, Collector collector) {

        if (declaration.getArguments().size() > MAX_METHODS_COUNT) {

            collector.addWarning(className, "Class  has more than " + MAX_METHODS_COUNT + " methods");

        }

        super.visit(declaration, collector);
    }
    @Override
    public void visit(MethodDeclaration declaration, Collector collector) {
        int methodBodyLength = declaration.getRange().map(range -> range.begin.line - range.end.line).orElse(0);
        
        int methodNameLength = declaration.getNameAsString().length();
        
        int parametersCount = declaration.getParameters().size();


        if (methodBodyLength > MAX_BODY_LENGTH) {
            collector.addWarning(className, "Method \""+ declaration.getName() +"\" body has more than " + MAX_BODY_LENGTH + " lines");
        }
        
        if (methodNameLength > MAX_METHOD_NAME_LENGTH) {
            collector.addWarning(className, "Method \"" + declaration.getName() + "\" name is too long, it has more than " + MAX_METHOD_NAME_LENGTH + " characters");
        }

        if (parametersCount > MAX_PARAM_COUNT) {
            collector.addWarning(className, "Method \"" + declaration.getName() + "\"  has more than " + MAX_METHOD_NAME_LENGTH + " parameters");
        }

        for (Parameter param: declaration.getParameters()) {

            if (param.getNameAsString().length() > MAX_VARIABLE_LENGTH) {

                collector.addWarning(className, "Method \"" + declaration.getName() + "\" variable \"" + param.getName() +"\" is way too long!");

            }

        }

    }

    @Override
    public void visit(VariableDeclarationExpr declaration, Collector collector) {

        if (declaration.getVariables().size() > MAX_VARIABLE_COUNT) {
            collector.addWarning(className, "Class has more than " + MAX_VARIABLE_COUNT + " variables");
        }

        for (VariableDeclarator variable: declaration.getVariables()) {

            if (variable.getNameAsString().length() > MAX_VARIABLE_LENGTH) {

                collector.addWarning(className, "Field variable \"" + variable.getNameAsString() +"\" is way too long!");

            }

        }


    }
    
}
