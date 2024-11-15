package aed;

public class Tupla <T,Q> {
    private T primero;
    private Q segundo;

    public Tupla(T primero, Q segundo) { //COMPLEJIDAD DE LA FUNCION: O(1)
        this.primero = primero;
        this.segundo = segundo;
    }

    public T ObtenerPrimero() { //COMPLEJIDAD DE LA FUNCION: O(1)
        return primero;
    }

    public Q ObtenerSegundo() { //COMPLEJIDAD DE LA FUNCION: O(1)
        return segundo; 
    }

    public void setear_primero(T valor){ //COMPLEJIDAD DE LA FUNCION: O(1)
        primero = valor;
    }

    public void setear_segundo(Q valor){ //COMPLEJIDAD DE LA FUNCION: O(1)
        segundo = valor;
    }

    public void setear_ambos(T valor0, Q valor1){ //COMPLEJIDAD DE LA FUNCION: O(1)
        primero = valor0;
        segundo = valor1;
    }
}
