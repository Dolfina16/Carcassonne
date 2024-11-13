package aed;

public class Traslado {
    
    private int id;
    private int origen;
    private int destino;
    private int gananciaNeta;
    private int timestamp;
    private Tupla<Integer,Integer> refs = new Tupla(null, null);

    public Traslado(int id, int origen, int destino, int gananciaNeta, int timestamp){
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.gananciaNeta = gananciaNeta;
        this.timestamp = timestamp;
    }

    public int id(){
        return id;
    }
    public int origen(){
        return origen;
    }
    public int destino(){
        return destino;
    }
    public int gananciaNeta(){
        return gananciaNeta;
    }
    public int timestamp(){
        return timestamp;
    }
    public Tupla refs(){
        return refs;
    }

    public void set_refRed(int i){
        refs.set_first(i);
    }
    public void set_refAnti(int i){
        refs.set_second(i);
    }
}
