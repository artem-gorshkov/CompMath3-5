package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Expression;

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
            String equation = parseEquation(equation_1.getText());
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
            ChartPainter.drawPlotWithRoots(a, b, equation, root, chartBox);
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
            String equation1 = parseEquation(equation_2_1.getText());
            String equation2 = parseEquation(equation_2_2.getText());
            var method = new NewtonSystem(equation1, equation2);
            var a = parseDouble(a_2);
            var b = parseDouble(b_2);
            var e = parseDouble(e_2);
            var k = parseDouble(k_2);
            var answer = method.solve(a, b, e, k);
            table_2.setItems(FXCollections.observableArrayList(answer));
            var ans = new Pair<>(answer.getX(), answer.getY());
            ChartPainter.drawPlotsWithRoot(equation1, equation2, ans, chartBox);
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
    public void test1_1() {
        equation_1.setText("sin(x)=0");
        a_1.setText("-10");
        b_1.setText("10");
        e_1.setText("0.0001");
        k_1.setText("50");
    }

    @FXML
    public void test1_2() {
        equation_1.setText("ln(x-5)*sin(x)+2=0");
        a_1.setText("4");
        b_1.setText("50");
        e_1.setText("10^(-4)");
        k_1.setText("100");
    }

    @FXML
    public void test2_1() {
        equation_2_1.setText("1.6*x^2*sin(y)-y=0");
        equation_2_2.setText("3.2*y*x^2+cos(x)=0");
        a_2.setText("1");
        b_2.setText("-1");
        e_2.setText("0.001");
        k_2.setText("3");
    }

    @FXML
    public void test2_2() {
        equation_2_1.setText("sin(y-1)+x-1.3=0");
        equation_2_2.setText("y-sin(x+1)-0.8=0");
        a_2.setText("0");
        b_2.setText("1");
        e_2.setText("0.001");
        k_2.setText("5");
    }

    @FXML
    public void test2_3() {
        equation_2_1.setText("3*x^2*sin(y)^2-y^2=0");
        equation_2_2.setText("3.5*y*x^3+x=0");
        a_2.setText("1");
        b_2.setText("-1");
        e_2.setText("0.001");
        k_2.setText("5");
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

    private double parseDouble(TextField text) {
        return new Expression(text.getText()).calculate();
    }

    private static String parseEquation(String str) throws IllegalArgumentException {
        String str1 = str.substring(0, str.indexOf('='));
        String str2 = str.substring(str.indexOf('=') + 1);
        Expression e = new Expression(str1 + "-(" + str2 + ")");
        if (e.checkSyntax())
            throw new IllegalArgumentException();
        return str1 + "-(" + str2 + ")";
    }

}
