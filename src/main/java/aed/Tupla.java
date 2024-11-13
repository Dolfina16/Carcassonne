package aed;

public class Tupla <T,Q> {
    private T first;
    private Q second;

    public Tupla(T first, Q second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public Q getSecond() {
        return second;
    }

    public void set_first(T value){
        first = value;
    }

    public void set_second(Q value){
        second = value;
    }

    public void set_both(T value0, Q value1){
        first = value0;
        second = value1;
    }
}
