package aed;

public class Tupla <T> {
    private T first;
    private T second;

    public Tupla(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }
}
