package ru.itmo.gorshkov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class MainController {

    public TableView<Node> table;
    public TableColumn<Node, Double> table_x;
    public TableColumn<Node, Double> table_y;

    public Text error;
    public AnchorPane chartBox;
    public TextField equation;
    public TextField x0;
    public TextField y0;
    public TextField end;
    public TextField e;

    @FXML
    private void initialize() {
        table.setEditable(true);
        table_x.setCellValueFactory(new PropertyValueFactory<>("x"));
        table_x.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        table_y.setCellValueFactory(new PropertyValueFactory<>("y"));
        table_y.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        table.setPlaceholder(new Label(""));
        table_x.maxWidthProperty().bind(table.widthProperty().multiply(.50));
        table_x.minWidthProperty().bind(table_x.maxWidthProperty());
    }

    public void solve() {
        error.setVisible(false);
        if (x0.getText().equals("") || y0.getText().equals("") || equation.getText().equals("") || end.getText().equals("") || e.getText().equals(""))
            error.setVisible(true);
        else {
            var exp = new Expression(equation.getText());
            exp.addArguments(new Argument("x", 0), new Argument("y", 0));
            var eulerMethod = new ImprovedEulerMethod(parseDouble(x0), parseDouble(y0), parseDouble(end), parseDouble(e), exp);
            var nodes = eulerMethod.solve();
            table.setItems(FXCollections.observableList(nodes));
            var splineMethod = new SplineInterpolation(nodes);
            var s = splineMethod.calcSpline();
            ChartPainter.drawChart(nodes, s, chartBox);
        }
    }

    private double parseDouble(TextField text) {
        return Double.parseDouble(text.getText());
    }

    @FXML
    public void test1() {
        equation.setText("y+(1+x)*y^2");
        x0.setText("1");
        y0.setText("-1");
        end.setText("1.5");
        e.setText("0.1");
    }

    @FXML
    public void test2() {
        equation.setText("(x-y)^2+1");
        x0.setText("-10");
        y0.setText("-9.9");
        end.setText("-0.1");
        e.setText("0.3");
    }

    @FXML
    public void test3() {
        equation.setText("x^2*y+2*x*y");
        x0.setText("-3");
        y0.setText("1");
        end.setText("1.2");
        e.setText("0.1");
    }

    @FXML
    public void test4() {
        equation.setText("y^2*x+4*x+y^2+4");
        x0.setText("-2");
        y0.setText("0");
        end.setText("-0.1");
        e.setText("0.1");
    }

    @FXML
    public void test5() {
        equation.setText("cos(e^(2*x))^2-2*y");
        x0.setText("0.5");
        y0.setText("0.5");
        end.setText("2.5");
        e.setText("0.3");
    }
}
