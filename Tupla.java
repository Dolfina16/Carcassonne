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

    public void set_first(T value){
        first = value;
    }

    public void set_second(T value){
        second = value;
    }
}
