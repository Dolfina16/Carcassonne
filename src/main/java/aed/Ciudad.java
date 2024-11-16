package aed;

public class Ciudad {
    
    private int id;
    private int ganancia;
    private int perdida;
    private int superavit;
    private int ref;
    private Handler handler;
    
    public Ciudad(int id){  //COMPLEJIDAD DE LA FUNCION: O(1)
        this.id = id;
        this.ganancia = 0;
        this.perdida = 0;
        this.superavit = 0;
        this.ref = id;
    }

    public int id(){    //COMPLEJIDAD DE LA FUNCION: O(1)
        return id;
    }

    public int ganancia(){  //COMPLEJIDAD DE LA FUNCION: O(1)
        return ganancia;
    }

    public int perdida(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return perdida;
    }

    public int superavit(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return superavit;
    }

    public int ref(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return ref;
    }

    public void incr_ganancia(int valor){   //COMPLEJIDAD DE LA FUNCION: O(1)
        ganancia += valor;
        actualizar_superavit();
    }

    public void incr_perdida(int valor){    //COMPLEJIDAD DE LA FUNCION: O(1)
        perdida += valor;
        actualizar_superavit();
    }

    public void actualizar_superavit(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        superavit = ganancia - perdida;
    }

    public void setear_ref(int id){    //COMPLEJIDAD DE LA FUNCION: O(1)
        ref = id;
    }

    public void set_handler(Handler handler){
        this.handler = handler;
    }

    public Handler handler(){
        return handler;
    }

    @Override
    public String toString(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return "" + superavit;
    }
}
