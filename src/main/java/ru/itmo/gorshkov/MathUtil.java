package ru.itmo.gorshkov;


import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
    private static final double EPSILON = 1e-5;

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

    public static double calcSecondDerivative(double x, Expression e) {
        e.setArgumentValue("x", x - EPSILON);
        double y0 = e.calculate();
        e.setArgumentValue("x", x);
        double y1 = e.calculate();
        e.setArgumentValue("x", x + EPSILON);
        double y2 = e.calculate();
        return (y2 - 2 * y1 + y0) / Math.pow(x - EPSILON, 2);
    }
}
