package analyzer;

import analyzer.collectors.Collector;
import analyzer.visitors.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
import com.github.javaparser.printer.XmlPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Analyzer extends SimpleFileVisitor<Path> {

    private Collector collector;

    public Analyzer(Collector collector) {
        this.collector = collector;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
        // initialize compilation unit ( AST Tree )
        CompilationUnit unit = JavaParser.parse(file.toFile());

        // dot -Tpng ast.dot > ast.png
        DotPrinter printer = new DotPrinter(true);
        try (FileWriter fileWriter = new FileWriter("ast.dot");
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(printer.output(unit));
        }

        // collect all the stats
        new BooleanMethodVisitor().visit(unit, collector);
        new TooLongVisitor().visit(unit, collector);
        new TooSmallVisitor().visit(unit, collector);
        new VariableNamingConventionVisitor().visit(unit, collector);
        new ClassLineCounterVisitor().visit(unit, collector);
        new MetricVisitor().visit(unit, collector);

        return FileVisitResult.CONTINUE;
    }
}
