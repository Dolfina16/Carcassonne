package aed;

public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private Handler handlerRedi; // La posición del traslado dentro del heapTRedituable
    private Handler handlerAnti; // La posición del traslado dentro del heapTAntiguedad

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){ //COMPLEJIDAD DE LA FUNCION: O(1)
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
        handlerRedi = new Handler(null);
        handlerAnti = new Handler(null);
    }

    public int id(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return id;
    }
    public int origen(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return origen;
    }
    public int destino(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return destino;
    }
    public int gananciaNeta(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return gananciaNeta;
    }
    public int timestamp(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return timestamp; 
    }
    public void setear_handlerRedi(Handler handler){ //COMPLEJIDAD DE LA FUNCION: O(1)
        handlerRedi = handler;
    }
    public void setear_handlerAnti(Handler handler){ //COMPLEJIDAD DE LA FUNCION: O(1)
        handlerAnti = handler;
    }

    public Handler handlerRedi(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return handlerRedi;
    }
    
    public Handler handlerAnti(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return handlerAnti;
    }

    @Override
    public boolean equals(Object otra) {
        boolean otroEsNull = (otra == null);
        boolean claseDistinta = otra.getClass() != this.getClass();
        if (otroEsNull || claseDistinta) {
            return false;
        }
        Traslado otroTraslado = (Traslado) otra;
        boolean res = id == otroTraslado.id && origen == otroTraslado.origen && destino == otroTraslado.destino && gananciaNeta == otroTraslado.gananciaNeta && timestamp == otroTraslado.timestamp;
        res &= handlerAnti.equals(otroTraslado.handlerAnti());
        res &= handlerRedi.equals(otroTraslado.handlerRedi());
        return res;
    }

}
