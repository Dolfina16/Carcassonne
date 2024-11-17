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

    @Override
    public boolean equals(Object otro) {
        // Algunos chequeos burocraticos...
        boolean otroEsNull = (otro == null);
        boolean claseDistinta = otro.getClass() != this.getClass();
        if (otroEsNull || claseDistinta) {
            return false;
        }
        // casting -> cambiar el tipo
        Handler otroHandler = (Handler) otro;
        return ref == otroHandler.ref();
        
    }
}
