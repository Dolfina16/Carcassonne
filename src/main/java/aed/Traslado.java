package aed;

public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private Handler handlerRedi;
    private Handler handlerAnti;

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){ //COMPLEJIDAD DE LA FUNCION: O(1)
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
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

    public Handler handlerRedi(){
        return handlerRedi;
    }
    
    public Handler handlerAnti(){
        return handlerAnti;
    }
}
