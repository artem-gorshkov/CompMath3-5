package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.List;
import java.util.function.Function;

public class ChartPainter {

    public static void drawChart(double a, double b, List<Node> nodes, Expression exp, double[] s, AnchorPane chartBox) {
        NumberAxis y = new NumberAxis();
        NumberAxis x = new NumberAxis();
        x.setForceZeroInRange(false);
        var chart = new LineChart<>(x, y);

        drawPlot(a, b, exp, chart);
        drawNodes(nodes, chart);
        drawSpline(s, nodes, chart);

        chartBox.getChildren().clear();
        chartBox.getChildren().add(chart);
        AnchorPane.setBottomAnchor(chart, 0d);
        AnchorPane.setLeftAnchor(chart, 0d);
        AnchorPane.setRightAnchor(chart, 0d);
        AnchorPane.setTopAnchor(chart, 0d);
        chart.setLegendVisible(false);
    }

    private static void drawSpline(double[] s, List<Node> nodes, LineChart<Number, Number> chart) {
        int n = nodes.size() - 1;
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (int i = 1; i <= n; i += 1) {
            int finalI = i;
            Function<Double, Double> func = x -> {
                double x0 = nodes.get(finalI - 1).getX();
                double y0 = nodes.get(finalI - 1).getY();
                double x1 = nodes.get(finalI).getX();
                double y1 = nodes.get(finalI).getY();
                double h = x1 - x0;
                return y0 * Math.pow(x - x1, 2) * (2 * (x - x0) + h) / Math.pow(h, 3) +
                        s[finalI - 1] * Math.pow(x - x1, 2) * (x - x0) / Math.pow(h, 2) +
                        y1 * Math.pow(x - x0, 2) * (2 * (x1 - x) + h) / Math.pow(h, 3) +
                        s[finalI] * Math.pow(x - x0, 2) * (x - x1) / Math.pow(h, 2);
            };
            for (double j = nodes.get(i - 1).getX(); j <= nodes.get(i).getX();
                 j += (nodes.get(i).getX() - nodes.get(i - 1).getX()) / 50) {
                double result = func.apply(j);
                if (Double.isFinite(result))
                    data.add(new XYChart.Data<>(j, result));
            }
        }
        var series = new XYChart.Series<>(data);
        series.setName("spline");
        chart.getData().add(series);
        for (var dataNode : series.getData()) {
            StackPane stackPane = (StackPane) dataNode.getNode();
            stackPane.setVisible(false);
        }
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
        series.setName("plot");
        chart.getData().add(series);
        for (var dataNode : series.getData()) {
            StackPane stackPane = (StackPane) dataNode.getNode();
            stackPane.setVisible(false);
        }
    }

    private static void drawNodes(List<Node> nodes, LineChart<Number, Number> chart) {
        nodes.forEach(node -> {
            var series = new XYChart.Series<Number, Number>();
            if (Double.isFinite(node.getY()))
                series.getData().add(new XYChart.Data<>(node.getX(), node.getY()));
            chart.getData().add(series);
        });
    }
}
