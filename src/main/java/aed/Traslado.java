package aed;

public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private Tupla<Integer,Integer> refs = new Tupla(null, null);

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
    public Tupla<Integer,Integer> refs(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return refs;
    }

    public void set_refRed(int i){ //COMPLEJIDAD DE LA FUNCION: O(1)
        refs.set_first(i);
    }
    public void set_refAnti(int i){ //COMPLEJIDAD DE LA FUNCION: O(1)
        refs.set_second(i);
    }

    @Override
    public String toString(){
        return "" + timestamp;
    }
}
