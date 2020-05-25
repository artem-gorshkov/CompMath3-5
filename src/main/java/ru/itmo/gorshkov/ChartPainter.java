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
}
