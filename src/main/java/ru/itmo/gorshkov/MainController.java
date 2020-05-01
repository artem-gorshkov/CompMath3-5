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

public class MainController {

    public TextArea equation_1;
    public TextField a_1;
    public TextField b_1;
    public TextField e_1;
    public TextField k_1;
    public ToggleGroup method_1;
    public Text error_1;

    public TableView<Pair<Double, Integer>> table;
    public TableColumn<Pair<Double, Integer>, Double> table_root;
    public TableColumn<Pair<Double, Integer>, Integer> table_iterations;
    public AnchorPane chartBox;

    @FXML
    private void initialize() {
        table_root.setCellValueFactory(new PropertyValueFactory<>("key"));
        table_iterations.setCellValueFactory(new PropertyValueFactory<>("value"));
        table.setPlaceholder(new Label(""));

    }

    public void solve_1() {
        try {
            error_1.setVisible(false);
            String equation = Parser.parseEquation(equation_1.getText());
            RadioButton selection = (RadioButton) method_1.getSelectedToggle();
            boolean isBisection = selection.getText().equals("Метод деления пополам");
            var method = new CompMethod(equation);
            var answer = method.solve(
                    parseDouble(a_1),
                    parseDouble(b_1),
                    parseDouble(e_1),
                    parseDouble(k_1),
                    isBisection);
            table.setItems(FXCollections.observableList(answer));
            drawPlot(parseDouble(a_1), parseDouble(b_1), equation);
        } catch (Exception e) {
            error_1.setVisible(true);
            equation_1.clear();
            a_1.clear();
            b_1.clear();
            e_1.clear();
            k_1.clear();
            table.setItems(FXCollections.emptyObservableList());
        }
    }

    private double parseDouble(TextField text) {
        return Double.parseDouble(text.getText());
    }

    @FXML
    public void test1() {
        equation_1.setText("sin(x)=0");
        a_1.setText("-10");
        b_1.setText("10");
        e_1.setText("0.0001");
        k_1.setText("50");
    }

    public void drawPlot(double a, double b, String equation) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        var chart = new LineChart<>(x, y);
        var exp = new Expression(equation);
        exp.addArguments(new Argument("x", 0));
        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        for (double i = a; i <= b; i += (b - a) / 100) {
            exp.setArgumentValue("x", i);
            data.add(new XYChart.Data<>(i, exp.calculate()));
        }
        var series = new XYChart.Series<>(data);
        chart.getData().add(series);
        for (XYChart.Data<Number, Number> dataNode : series.getData()) {
            StackPane stackPane = (StackPane) dataNode.getNode();
            stackPane.setVisible(false);
        }
        AnchorPane.setBottomAnchor(chart,0d);
        AnchorPane.setLeftAnchor(chart,0d);
        AnchorPane.setRightAnchor(chart,0d);
        AnchorPane.setTopAnchor(chart,0d);
        chartBox.getChildren().clear();
        chartBox.getChildren().add(chart);
    }
}
