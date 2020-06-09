package ru.itmo.gorshkov;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;

public class ImprovedEulerMethod {
    private double x0;
    private double y0;
    private double end;
    private double e;
    private Expression exp;

    public ImprovedEulerMethod(double x0, double y0, double end, double e, Expression exp) {
        this.x0 = x0;
        this.y0 = y0;
        this.end = end;
        this.e = e;
        this.exp = exp;
    }

    public List<Node> solve() {
        var node = new ArrayList<Node>();
        double h = (end - x0) / 10;
        boolean flag = true;
        int timeout = 0;
        while (flag && timeout < 5) {
            double y = y0;
            node.clear();
            node.add(new Node(x0, y0));
            flag = false;
            for (double i = x0; i < end; i += h) {
                exp.setArgumentValue("x", i);
                exp.setArgumentValue("y", y);
                double f1 = exp.calculate();
                double y1 = y + h * f1;
                exp.setArgumentValue("x", i + h);
                exp.setArgumentValue("y", y1);
                double f2 = exp.calculate();
                y = y + h / 2 * (f1 + f2);
                if (Math.abs(y - y1) > e * Math.abs(y)) {
                    h = h / 2;
                    flag = true;
                    timeout++;
                    break;
                }
                node.add(new Node(i + h, y));
            }
        }
        return node;
    }
}
