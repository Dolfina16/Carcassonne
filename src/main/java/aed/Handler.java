package aed;

public class Handler{
    private Integer ref;

    public Handler(Integer r){  //COMPLEJIDAD DE LA FUNCION: O(1)
        ref = r;
    }

    public Integer ref(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return ref;
    }

    public void set_ref(int new_ref){ //COMPLEJIDAD DE LA FUNCION: O(1)
        ref = new_ref;
    }
}
