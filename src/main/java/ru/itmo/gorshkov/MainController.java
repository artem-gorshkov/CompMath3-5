package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.stream.Collectors;

public class MainController {
    public static final String sin = "sin(x)";
    public static final String lnSin = "ln(x-5)*sin(x)+2";
    public static final String square = "x^2";



    public TableView<Node> table;
    public TableColumn<Node, Double> table_x;
    public TableColumn<Node, Double> table_y;

    public Text error;
    public AnchorPane chartBox;
    public ComboBox<String> comboBox;
    public TextField begin;
    public TextField end;
    public TextField partition;

    @FXML
    private void initialize() {

        table_x.setCellValueFactory(new PropertyValueFactory<>("x"));
        table_y.setCellValueFactory(new PropertyValueFactory<>("y"));
        table.setPlaceholder(new Label(""));
        table_x.maxWidthProperty().bind(table.widthProperty().multiply(.50));
        table_x.minWidthProperty().bind(table_x.maxWidthProperty());

        ObservableList<String> func = FXCollections.observableArrayList(sin, lnSin, square);
        comboBox.setItems(func);
    }

    public void solve() {

    }
@FXML
    public void feelNodes() {
        try {
            error.setVisible(false);
            var a = parseDouble(begin);
            var b = parseDouble(end);
            var n = Integer.parseInt(partition.getText());
            var e = getFunction();
            var nodes = MathUtil.calcNode(a, b, n, e);
            table.setItems(FXCollections.observableList(nodes));
        } catch (Exception e) {
            e.printStackTrace();
            error.setVisible(true);
            begin.clear();
            end.clear();
            partition.clear();
        }
    }

    public Expression getFunction() {
        if (comboBox.getValue() == null)
            throw new IllegalArgumentException();
        String func = comboBox.getValue();
        var e = new Expression(func);
        e.addArguments(new Argument("x",0));
        return e;
    }
    private double parseDouble(TextField text) {
        return new Expression(text.getText()).calculate();
    }

    @FXML
    public void test1() {
        begin.setText("10");
        end.setText("20");
        partition.setText("10");
        comboBox.setValue(sin);
    }

}
