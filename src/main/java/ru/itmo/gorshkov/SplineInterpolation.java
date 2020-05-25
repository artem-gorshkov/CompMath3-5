package ru.itmo.gorshkov;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.List;

public class SplineInterpolation {
    private List<Node> nodes;
    private Expression e;
    private int n;

    double[][] a;
    double[] b;
    double[] s; //answer

    //run-through coefficients
    double[] alpha;
    double[] beta;


    public SplineInterpolation(List<Node> nodes, Expression e) {
        this.nodes = nodes;
        this.e = e;
        n = nodes.size() - 1;
        b = new double[n + 1];
        s = new double[n + 1];
        a = new double[n + 1][3];
        alpha = new double[n + 1];
        beta = new double[n + 1];
    }

    public double[] calcSpline() {
        fillMatrix();
        run_through_method();
        return s;
    }

    private void fillMatrix() {
        a[0][0] = 0;
        a[0][1] = 4 * step(1);
        a[0][2] = -2 * step(1);
        b[0] = MathUtil.calcSecondDerivative(nodes.get(0).getX(), e)
                + 6 * -fraction(1);
        for (int i = 1; i <= n - 1; i++) {
            a[i][0] = step(i);
            a[i][1] = 2 * (step(i) + step(i + 1));
            a[i][2] = step(i + 1);
            b[i] = 3 * (fraction(i) + fraction(i + 1));
        }
        b[n] = MathUtil.calcSecondDerivative(nodes.get(n).getX(), e)
                + 6 * fraction(n);
        a[n][0] = 2 * step(n);
        a[n][1] = 4 * step(n);
        a[n][2] = 0;
    }

    private double fraction(int k) {
        return (nodes.get(k).getY() - nodes.get(k - 1).getY()) * Math.pow(step(k), 2);
    }

    private double step(int k) {
        return 1 / (nodes.get(k).getX() - nodes.get(k - 1).getX());
    }

    /**
     * Solve three diagonal linear systems in O(n)
     */
    private void run_through_method() {
        //forward stroke
        alpha[0] = -a[0][2] / a[0][1];
        beta[0] = b[0] / a[0][1];
        for (int i = 1; i <= n; i++) {
            double y = a[i][1] + a[i][0] * a[i - 1][0];
            alpha[i] = -a[i][2] / y;
            beta[i] = (b[i] - alpha[i] * beta[i - 1]) / y;
        }
        //return stroke
        s[n] = beta[n];
        for (int i = n - 1; i >=0; i--) {
            s[i] = alpha[i]*s[i+1] + beta[i];
        }
    }
}
