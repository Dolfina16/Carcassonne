package aed;

public class Ciudad {
    
    private int id;
    private int ganancia;
    private int perdida;
    private int superavit;
    
    public Ciudad(int id, int ganancia, int perdida, int superavit){
        this.id = id;
        this.ganancia = ganancia;
        this.perdida = perdida;
        this.superavit = superavit;
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

    public void incr_ganancia(int valor){
        ganancia += valor;
    }

    public void incr_perdida(int valor){
        perdida += valor;
    }

    public void actualizar_superavit(int gana, int pierde){
        superavit += gana - pierde;
    }
}
