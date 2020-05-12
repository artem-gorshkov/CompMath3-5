package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.List;

public class ChartPainter {

    public static void drawPlotWithRoots(double a, double b, String equation, List<Double> roots, AnchorPane chartBox) {
        //        double step = (Math.ceil(b) - Math.floor(a)) / 25;
//        NumberAxis x = new NumberAxis(Math.floor(a), Math.ceil(b), step);
//        x.setAutoRanging(true);
//        exp.setArgumentValue("x", a);
//        if (!Double.isFinite(exp.calculate())) {
//            var series = new XYChart.Series<Number, Number>();
//            var dot = new XYChart.Data<Number, Number>(a, 0);
//            series.getData().add(dot);
//            chart.getData().add(series);
//            StackPane stackPane = (StackPane) dot.getNode();
//            stackPane.setVisible(false);
//        }
        var exp = new Expression(equation);
        exp.addArguments(new Argument("x", 0));
        NumberAxis y = new NumberAxis();
        NumberAxis x = new NumberAxis();
        x.setForceZeroInRange(false);
        var chart = new LineChart<>(x, y);
        drawPlot(a, b, exp, chart);
        drawRoots(roots, chart, exp);
        chartBox.getChildren().clear();
        chartBox.getChildren().add(chart);
    }

    public static void drawPlotWithRoots(double a, double b, String equation1, String equation2, List<Pair<Double, Double>> roots, AnchorPane chartBox) {
        var exp1 = new Expression(equation1);
        exp1.addArguments(new Argument("x", 0));
        exp1.addArguments(new Argument("y", 0));
        var exp2 = new Expression(equation2);
        exp2.addArguments(new Argument("x", 0));
        exp1.addArguments(new Argument("y", 0));
        NumberAxis y = new NumberAxis();
        NumberAxis x = new NumberAxis();
        x.setForceZeroInRange(false);
        var chart = new LineChart<>(x, y);
        drawPlot(a, b, exp1, chart);
        drawPlot(a, b, exp2, chart);
        drawRoots(roots, chart);
        chartBox.getChildren().clear();
        chartBox.getChildren().add(chart);
    }

    private static void drawPlot(double a, double b, Expression exp, LineChart<Number, Number> chart) {
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (double i = a; i <= b; i += (b - a) / 1000) {
            exp.setArgumentValue("x", i);
            var result = exp.calculate();
            if (Double.isFinite(result))
                data.add(new XYChart.Data<>(i, result));
        }
        var series = new XYChart.Series<>(data);
        chart.getData().add(series);
        for (var dataNode : series.getData()) {
            StackPane stackPane = (StackPane) dataNode.getNode();
            stackPane.setVisible(false);
        }
        AnchorPane.setBottomAnchor(chart, 0d);
        AnchorPane.setLeftAnchor(chart, 0d);
        AnchorPane.setRightAnchor(chart, 0d);
        AnchorPane.setTopAnchor(chart, 0d);
        chart.setLegendVisible(false);
    }

    private static void drawRoots(List<Double> roots, LineChart<Number, Number> chart, Expression exp) {
        roots.forEach(root -> {
            exp.setArgumentValue("x", root);
            var series = new XYChart.Series<Number, Number>();
            series.getData().add(new XYChart.Data<>(root, exp.calculate()));
            chart.getData().add(series);
        });
    }

    private static void drawRoots(List<Pair<Double, Double>> roots, LineChart<Number, Number> chart) {
        roots.forEach(root -> {
            var series = new XYChart.Series<Number, Number>();
            series.getData().add(new XYChart.Data<>(root.getKey(), root.getValue()));
            chart.getData().add(series);
        });
    }
}
