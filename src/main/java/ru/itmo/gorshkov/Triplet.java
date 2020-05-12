package ru.itmo.gorshkov;

public class Triplet<T, U, V> {

    private final T x;
    private final U y;
    private final V iterations;

    public Triplet(T x, U y, V iterations) {
        this.x = x;
        this.y = y;
        this.iterations = iterations;
    }

    public T getX() {
        return x;
    }

    public U getY() {
        return y;
    }

    public V getIterations() {
        return iterations;
    }

}
