package ru.itmo.gorshkov;

import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class NewtonSystem {
    private Expression exp1;
    private Expression exp2;

    public NewtonSystem(String equation1, String equation2) {
        exp1 = new Expression(equation1);
        exp1.addArguments(new Argument("x", 0));
        exp1.addArguments(new Argument("y", 0));
        exp2 = new Expression(equation2);
        exp2.addArguments(new Argument("x", 0));
        exp1.addArguments(new Argument("y", 0));
    }

    public Triplet<Double, Double, Integer> solve(double a, double b, double e) {
        int i = 1;
        var pair = iteration(a, b);
        while (Math.abs(pair.getKey() - a) > e && Math.abs(pair.getValue() - b) > e && i < 1000000) {
            a = pair.getKey();
            b = pair.getValue();
            pair = iteration(a, b);
            i++;
        }
        return new Triplet<>(pair.getKey(), pair.getValue(), i);
    }

    public Pair<Double, Double> iteration(double x, double y) {
        exp1.setArgumentValue("x", x);
        exp1.setArgumentValue("y", y);
        exp2.setArgumentValue("x", x);
        exp2.setArgumentValue("y", y);

        var f1 = exp1.calculate();
        var f2 = exp2.calculate();

        double[][] matrix = new double[2][2];
        matrix[0][0] = derivative(exp1, "x", x);
        matrix[0][1] = derivative(exp1, "y", y);
        matrix[1][0] = derivative(exp2, "x", x);
        matrix[1][1] = derivative(exp2, "x", x);
        double jacobean = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];

        double a = x - (f1 * matrix[1][1] - f2 * matrix[0][1]) / jacobean;
        double b = y + (f1 * matrix[1][0] - f2 * matrix[0][0]) / jacobean;
        return new Pair<>(a, b);
    }

    private double derivative(Expression exp, String var, double mean) {
        double e = 1e-5;
        exp.setArgumentValue(var, mean + e);
        var second = exp.calculate();
        exp.setArgumentValue(var, mean);
        var first = exp.calculate();
        return (second - first) / e;
    }
}
