package aed;

public class Ciudad {
    
    private int id;
    private int ganancia;
    private int perdida;
    private int superavit;
    private int ref;
    
    public Ciudad(int id){
        this.id = id;
        this.ganancia = 0;
        this.perdida = 0;
        this.superavit = 0;
        this.ref = id;
    }

    public int id(){
        return id;
    }

    public int ganancia(){
        return ganancia;
    }

    public int perdida(){
        return perdida;
    }

    public int superavit(){
        return superavit;
    }

    public int ref(){
        return ref;
    }

    public void incr_ganancia(int valor){
        ganancia += valor;
        actualizar_superavit();
    }

    public void incr_perdida(int valor){
        perdida += valor;
        actualizar_superavit();
    }

    public void actualizar_superavit(){
        superavit = ganancia - perdida;
    }

    public void set_ref(int id){
        ref = id;
    }

    @Override
    public String toString(){
        return "" + superavit;
    }
}
