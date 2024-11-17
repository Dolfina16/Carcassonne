package aed;

public class Ciudad {
    
    private int id;
    private int ganancia;
    private int perdida;
    private int superavit;
    private Handler handler;
    
    public Ciudad(int id){  //COMPLEJIDAD DE LA FUNCION: O(1)
        this.id = id;       //1
        this.ganancia = 0; //1
        this.perdida = 0; //1
        this.superavit = 0; //1
        handler = new Handler(null);    //2 
                                        //tF = 6 --> O(1)
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

    public void set_handler(Handler handler){ //COMPLEJIDAD DE LA FUNCION: O(1)
        this.handler = handler;
    }

    public Handler handler(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return handler;
    }

    @Override
    public String toString(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return "" + superavit;
    }

    @Override
    public boolean equals(Object otra) {
        // Algunos chequeos burocraticos...
        boolean otroEsNull = (otra == null);
        boolean claseDistinta = otra.getClass() != this.getClass();
        if (otroEsNull || claseDistinta) {
            return false;
        }
        // casting -> cambiar el tipo
        Ciudad otraCiudad = (Ciudad) otra;
        boolean res = id == otraCiudad.id && ganancia == otraCiudad.ganancia && perdida == otraCiudad.perdida && superavit == otraCiudad.superavit;
        res &= handler.equals(otraCiudad.handler());
        return res;
    }
}
