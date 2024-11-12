package aed;

public class Ciudad {
    
    private int id;
    private int ganancia;
    private int perdida;
    private int superavit;
    
    public Ciudad(int id){
        this.id = id;
        this.ganancia = 0;
        this.perdida = 0;
        this.superavit = 0;
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
        actualizar_superavit();
    }

    public void incr_perdida(int valor){
        perdida += valor;
        actualizar_superavit();
    }

    public void actualizar_superavit(){
        superavit = ganancia - perdida;
    }

    @Override
    public String toString(){
        return "" + superavit;
    }
}
