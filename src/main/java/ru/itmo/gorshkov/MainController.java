package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    public TextArea equation_1;
    public TextField a_1;
    public TextField b_1;
    public TextField e_1;
    public TextField k_1;
    public ToggleGroup method_1;
    public Text error_1;

    public TextArea equation_2_1;
    public TextArea equation_2_2;
    public TextField a_2;
    public TextField b_2;
    public TextField e_2;
    public TextField k_2;
    public Text error_2;

    public TableView<Pair<Double, Integer>> table_1;
    public TableColumn<Pair<Double, Integer>, Double> table_1_root;
    public TableColumn<Pair<Double, Integer>, Integer> table_1_iterations;

    public TableView<Triplet<Double, Double, Integer>> table_2;
    public TableColumn<Triplet<Double, Double, Integer>, Double> table_2_x;
    public TableColumn<Triplet<Double, Double, Integer>, Double> table_2_y;
    public TableColumn<Triplet<Double, Double, Integer>, Integer> table_2_iterations;

    public AnchorPane chartBox;


    @FXML
    private void initialize() {
        table_1_root.setCellValueFactory(new PropertyValueFactory<>("key"));
        table_1_iterations.setCellValueFactory(new PropertyValueFactory<>("value"));
        table_1.setPlaceholder(new Label(""));
        table_1_root.maxWidthProperty().bind(table_1.widthProperty().multiply(.67));
        table_1_root.minWidthProperty().bind(table_1_root.maxWidthProperty());

        table_2_x.setCellValueFactory(new PropertyValueFactory<>("x"));
        table_2_y.setCellValueFactory(new PropertyValueFactory<>("y"));
        table_2_iterations.setCellValueFactory(new PropertyValueFactory<>("iterations"));
        table_2.setPlaceholder(new Label(""));
        table_2_x.maxWidthProperty().bind(table_2.widthProperty().multiply(.33));
        table_2_x.minWidthProperty().bind(table_2_x.maxWidthProperty());
        table_2_y.maxWidthProperty().bind(table_2.widthProperty().multiply(.33));
        table_2_y.minWidthProperty().bind(table_2_y.maxWidthProperty());
    }

    public void solve_1() {
        try {
            error_1.setVisible(false);
            String equation = Parser.parseEquation(equation_1.getText());
            RadioButton selection = (RadioButton) method_1.getSelectedToggle();
            boolean isBisection = selection.getText().equals("Метод деления пополам");
            var method = new NonLinearCompMethod(equation);
            var a = parseDouble(a_1);
            var b = parseDouble(b_1);
            var e = parseDouble(e_1);
            double k = (k_1.getText().equals("")) ? 0 : parseDouble(k_1);
            var answer = method.solve(a, b, e, k, isBisection);
            var root = answer.stream().map(Pair::getKey).collect(Collectors.toList());
            table_1.setItems(FXCollections.observableList(answer));
            drawPlotWithRoots(a, b, equation, root);
        } catch (Exception e) {
            error_1.setVisible(true);
            equation_1.clear();
            a_1.clear();
            b_1.clear();
            e_1.clear();
            k_1.clear();
            table_1.setItems(FXCollections.emptyObservableList());
        }
    }

    public void solve_2() {
        try {
            error_2.setVisible(false);
            String equation1 = Parser.parseEquation(equation_2_1.getText());
            String equation2 = Parser.parseEquation(equation_2_2.getText());
            var method = new NewtonSystem(equation1, equation2);
            var a = parseDouble(a_1);
            var b = parseDouble(b_1);
            var e = parseDouble(e_1);
            double x = 0d;
            double y = 0d;//!!!!!!!!!!!!!!!!
            var answer = method.solve(x, y, e);
            table_2.setItems(FXCollections.observableArrayList(answer));
            var roots = new ArrayList<Pair<Double, Double>>();
            roots.add(new Pair<>(answer.getX(), answer.getY()));
            drawPlotWithRoots(a, b, equation1, equation2, roots);
        } catch (Exception e) {
            error_2.setVisible(true);
            equation_2_1.clear();
            equation_2_2.clear();
            a_2.clear();
            b_2.clear();
            e_2.clear();
            k_2.clear();
            table_2.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    public void test1() {
        equation_1.setText("sin(x)=0");
        a_1.setText("-10");
        b_1.setText("10");
        e_1.setText("0.0001");
        k_1.setText("50");
    }

    @FXML
    public void test1_2() {
        equation_1.setText("ln(x-5)*sin(x)+2=0");
        a_1.setText("15");
        b_1.setText("17");
        e_1.setText("10^(-4)");
        k_1.setText("1");
    }

    @FXML
    public void test2() {

    }

    @FXML
    public void showTable1() {
        table_2.setVisible(false);
        table_2.setManaged(false);
        table_1.setVisible(true);
        table_1.setManaged(true);
    }

    @FXML
    public void showTable2() {
        table_1.setVisible(false);
        table_1.setManaged(false);
        table_2.setVisible(true);
        table_2.setManaged(true);
    }

    private void drawPlotWithRoots(double a, double b, String equation, List<Double> roots) {
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

    private void drawPlotWithRoots(double a, double b, String equation1, String equation2, List<Pair<Double, Double>> roots) {
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

    private void drawPlot(double a, double b, Expression exp, LineChart<Number, Number> chart) {
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

    private void drawRoots(List<Double> roots, LineChart<Number, Number> chart, Expression exp) {
        roots.forEach(root -> {
            exp.setArgumentValue("x", root);
            var series = new XYChart.Series<Number, Number>();
            series.getData().add(new XYChart.Data<>(root, exp.calculate()));
            chart.getData().add(series);
        });
    }

    private void drawRoots(List<Pair<Double, Double>> roots, LineChart<Number, Number> chart) {
        roots.forEach(root -> {
            var series = new XYChart.Series<Number, Number>();
            series.getData().add(new XYChart.Data<>(root.getKey(), root.getValue()));
            chart.getData().add(series);
        });
    }

    private double parseDouble(TextField text) {
        return new Expression(text.getText()).calculate();
    }


}
