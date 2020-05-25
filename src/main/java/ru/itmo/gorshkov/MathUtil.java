package ru.itmo.gorshkov;


import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
    public static List<Node> calcNode(double a, double b, int n, Expression e) {
        if (a > b) {
            double c = a;
            a = b;
            b = c;
        }
        var list = new ArrayList<Node>();
        double step = (b - a) / n;
        for (double i = a; i <= b; i += step) {
            e.setArgumentValue("x", i);
            list.add(new Node(i, e.calculate()));
        }
        return list;
    }
}
