package ru.itmo.gorshkov;

import javafx.util.Pair;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CompMethod {
    private Expression exp;

    public CompMethod(String equation) {
        exp = new Expression(equation);
        exp.addArguments(new Argument("x", 0));
    }

    public List<Pair<Double, Integer>> solve(double a, double b, double e, double k, boolean isBisection) {
        if (a > b) {
            double c = a;
            a = b;
            b = c;
        }
        var answer = new ArrayList<Pair<Double, Integer>>();
        var list = localize(a, b, k);
        for (var pair : list) {
            answer.add(oneRoot(pair.getKey(), pair.getValue(), e, isBisection));
        }
        return answer;
    }

    private List<Pair<Double, Double>> localize(double a, double b, double k) {
        var list = new ArrayList<Pair<Double, Double>>();
        double step = (b - a) / k;
        for (double i = a; i < b; i += step) {
            if (f(i) * f(i + step) < 0)
                list.add(new Pair<>(i, i + step));
        }
        return list;
    }

    private Pair<Double, Integer> oneRoot(double a, double b, double e, boolean isBisection) {
        Function<Pair<Double, Double>, Double> calcNewC;
        if (isBisection)
            calcNewC = (p) -> {
                var A = p.getKey();
                var B = p.getValue();
            return (A + B) / 2;
            };
        else
            calcNewC = (p) -> {
            var A = p.getKey();
            var B = p.getValue();
            return A - f(A) * (B - A) / (f(B) - f(A));
            };
        double c = calcNewC.apply(new Pair<>(a, b));
        int n = 1;
        while (Math.abs(f(c)) >= e && b - a >= e) {
            if (f(a) * f(c) < 0)
                b = c;
            else
                a = c;
            n++;
            c = calcNewC.apply(new Pair<>(a, b));
        }
        return new Pair<>(c, n);
    }

    private double f(double x) {
        exp.setArgumentValue("x", x);
        return exp.calculate();
    }
}
