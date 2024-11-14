package aed;

public class Tupla <T,Q> {
    private T first;
    private Q second;

    public Tupla(T first, Q second) { //COMPLEJIDAD DE LA FUNCION: O(1)
        this.first = first;
        this.second = second;
    }

    public T getFirst() { //COMPLEJIDAD DE LA FUNCION: O(1)
        return first;
    }

    public Q getSecond() { //COMPLEJIDAD DE LA FUNCION: O(1)
        return second; 
    }

    public void set_first(T value){ //COMPLEJIDAD DE LA FUNCION: O(1)
        first = value;
    }

    public void set_second(Q value){ //COMPLEJIDAD DE LA FUNCION: O(1)
        second = value;
    }

    public void set_both(T value0, Q value1){ //COMPLEJIDAD DE LA FUNCION: O(1)
        first = value0;
        second = value1;
    }
}
