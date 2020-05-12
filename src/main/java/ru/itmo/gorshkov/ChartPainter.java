package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.List;

public class ChartPainter {

    public static void drawPlotsWithRoot(String equation1, String equation2, Pair<Double, Double> root, AnchorPane chartBox) {
        var chart1 = createChart("x", root.getKey(), createExpression(equation1, root), createExpression(equation2, root));
        var chart2 = createChart("y", root.getValue(), createExpression(equation1, root), createExpression(equation2, root));
        VBox vbox = new VBox();
        vbox.getChildren().add(chart1);
        vbox.getChildren().add(chart2);
        AnchorPane.setBottomAnchor(vbox, 0d);
        AnchorPane.setLeftAnchor(vbox, 0d);
        AnchorPane.setRightAnchor(vbox, 0d);
        AnchorPane.setTopAnchor(vbox, 0d);
        chartBox.getChildren().clear();
        chartBox.getChildren().add(vbox);
    }

    private static LineChart<Number, Number> createChart(String var, double root, Expression exp1, Expression exp2) {
        NumberAxis y = new NumberAxis();
        NumberAxis x = new NumberAxis();
        x.setForceZeroInRange(false);
        y.setForceZeroInRange(false);
        var chart = new LineChart<>(x, y);
        double a = root - 5;
        double b = root + 5;
        drawPlot(a, b, exp1, chart, var, var + "1", 100);
        drawPlot(a, b, exp2, chart, var, var + "2", 100);
        return chart;
    }

    public static void drawPlotWithRoots(double a, double b, String equation, List<Double> roots, AnchorPane chartBox) {
        var exp = new Expression(equation);
        exp.addArguments(new Argument("x", 0));
        NumberAxis y = new NumberAxis();
        NumberAxis x = new NumberAxis();
        x.setForceZeroInRange(false);
        var chart = new LineChart<>(x, y);
        drawPlot(a, b, exp, chart, "x", "x", 1000);
        drawRoots(roots, chart, exp);
        chartBox.getChildren().clear();
        chartBox.getChildren().add(chart);
        AnchorPane.setBottomAnchor(chart, 0d);
        AnchorPane.setLeftAnchor(chart, 0d);
        AnchorPane.setRightAnchor(chart, 0d);
        AnchorPane.setTopAnchor(chart, 0d);
        chart.setLegendVisible(false);
    }

    private static void drawPlot(double a, double b, Expression exp, LineChart<Number, Number> chart, String var, String name, int iteration) {
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (double i = a; i <= b; i += (b - a) / iteration) {
            exp.setArgumentValue(var, i);
            var result = exp.calculate();
            if (Double.isFinite(result))
                data.add(new XYChart.Data<>(i, result));
        }
        var series = new XYChart.Series<>(data);
        series.setName(name);
        chart.getData().add(series);
        for (var dataNode : series.getData()) {
            StackPane stackPane = (StackPane) dataNode.getNode();
            stackPane.setVisible(false);
        }
    }

    private static void drawRoots(List<Double> roots, LineChart<Number, Number> chart, Expression exp) {
        roots.forEach(root -> {
            exp.setArgumentValue("x", root);
            var series = new XYChart.Series<Number, Number>();
            series.getData().add(new XYChart.Data<>(root, exp.calculate()));
            chart.getData().add(series);
        });
    }

    private static Expression createExpression(String str, Pair<Double, Double> root) {
        var exp = new Expression(str);
        exp.addArguments(new Argument("x", root.getKey()));
        exp.addArguments(new Argument("y", root.getValue()));
        return exp;
    }
}
