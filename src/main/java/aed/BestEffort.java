package aed;

import java.util.ArrayList;
import java.util.Comparator;


public class BestEffort {
    private ArrayList<Integer> masProsperas = new ArrayList<Integer>();
    private ArrayList<Integer> masDecadentes = new ArrayList<Integer>();
    private int sumaGanancias;
    private int trasladosDespachados;
    private Heap<Ciudad> heapCSuperavit;
    private Tupla<Ciudad, Handler>[] ciudadesPorId;
    private Heap<Traslado> heapTRedituable;
    private Heap<Traslado> heapTAntiguedad;

    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    
    public class RediComparador implements Comparator<Traslado>{
        @Override
        public int compare(Traslado tras1, Traslado tras2){     //COMPLEJIDAD DE LA FUNCION: O(1)
            if (Integer.compare(tras1.gananciaNeta(),tras2.gananciaNeta()) == 0) {
                return Integer.compare(-tras1.id(),-tras2.id());
            }
            return Integer.compare(tras1.gananciaNeta(),tras2.gananciaNeta());
        }
    }
    RediComparador RedituabilidadComparator = new RediComparador();
    
    public class AntigComparador implements Comparator<Traslado>{
        @Override
        public int compare(Traslado tras1, Traslado tras2){    //COMPLEJIDAD DE LA FUNCION: O(1)     
            return Integer.compare(-tras1.timestamp(),-tras2.timestamp());
        }
    }
    AntigComparador AntiguedadComparator = new AntigComparador();

    private void crearCiudades(int n){  //COMPLEJIDAD DE LA FUNCION: 
        ciudadesPorId = new Tupla[n];  // O(1)
        for (int i = 0; i < n; i++) {
            Ciudad ciudad = new Ciudad(i);
            ciudadesPorId[i] = new Tupla<Ciudad,Handler>(ciudad, ciudad.handler());  //O(1)
        }
    }

    public <T> BestEffort(int cantCiudades, Traslado[] traslados){  //COMPLEJIDAD DE LA FUNCION:
        crearCiudades(cantCiudades);//O(C) t(T,C)=C
        heapCSuperavit = new Heap<Ciudad>(ciudadesPorId, SuperavitComparator); //O(C) t(T,C)=2C

        Tupla<Traslado,Handler>[] trasladosR = new Tupla[traslados.length];
        Tupla<Traslado,Handler>[] trasladosA = new Tupla[traslados.length];

        for (int i = 0; i < traslados.length; i++) {
            trasladosR[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerRedi());
            trasladosA[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerAnti());
        }

        heapTRedituable = new Heap<Traslado>(trasladosR, RedituabilidadComparator); //O(T) t(T,C)=2C + T
        heapTAntiguedad = new Heap<Traslado>(trasladosA, AntiguedadComparator); //O(T) t(T,C)=2C + 2T // O(T) t(T,C)=2C + 4T
    } //O(C+T)

    public void registrarTraslados(Traslado[] traslados){   //COMPLEJIDAD DE LA FUNCION:
        for(int i=0; i< traslados.length; i++){
            heapTRedituable.Añadir(new Tupla<>(traslados[i], traslados[i].handlerRedi())); //O(log T)

            heapTAntiguedad.Añadir(new Tupla<>(traslados[i], traslados[i].handlerAnti())); //O(log T)
        } //O[|traslados|.(4.log(T))]
    }

    public <T> int[] despacharMasRedituables(int n){ //COMPLEJIDAD DE LA FUNCION: O(n(log(T)+log(C)))
        int aDespachar = Math.min(n, heapTRedituable.tamaño()); //O(1)
        int[] idsDespachados = new int[aDespachar]; //O(1)
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {
            despachado = heapTRedituable.Sacar(0); // O(log(T))
            idsDespachados[i] = (despachado.id()); // O(1)

            heapTAntiguedad.Sacar(despachado.handlerAnti().ref()); // O(log(T))

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); // O(1)
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // O(log(C))

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // O(1)
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // O(log(C))

            actualizarMaximos(despachado);
            
            sumaGanancias += despachado.gananciaNeta(); //O(1)
            trasladosDespachados++; //O(1)
        }
        return idsDespachados; 
    }

    public void actualizarMaximos(Traslado despachado){
        if (masProsperas.size() != 0) {
            if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() < ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia() || masProsperas.get(0) == despachado.origen()){ // O(1)
                masProsperas = new ArrayList<Integer>();   //O(1)
                masProsperas.add(despachado.origen()); //O(1)
            }else if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() == ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia()){
                masProsperas.add(despachado.origen()); //O(1)
            }
        }else{
            masProsperas.add(despachado.origen()); //O(1)
        }

        if (masDecadentes.size() != 0) {
            if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() < ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida() || masDecadentes.get(0) == despachado.destino()){
                masDecadentes = new ArrayList<Integer>(); //O(1)
                masDecadentes.add(despachado.destino()); //O(1)
            }else if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() == ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida()){
                masDecadentes.add(despachado.destino()); //O(1)
            }
        }else{
            masDecadentes.add(despachado.destino());
        }
    }

    public int[] despacharMasAntiguos(int n){   //COMPLEJIDAD DE LA FUNCION:
        int aDespachar = Math.min(n, heapTAntiguedad.tamaño()); //O(1)
        int[] idsDespachados = new int[aDespachar]; //O(1)
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {

            despachado = heapTAntiguedad.Sacar(0); // O(log(T))
            idsDespachados[i] = (despachado.id()); // O(1)

            heapTRedituable.Sacar(despachado.handlerRedi().ref()); // O(log(T))

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); // O(1)
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // O(log(C))

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // O(1)
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // O(log(C))

            actualizarMaximos(despachado);
            
            sumaGanancias += despachado.gananciaNeta(); //O(1)
            trasladosDespachados++; //O(1)
        }
        return idsDespachados;
    }

    public int ciudadConMayorSuperavit(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return heapCSuperavit.maximo().id();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return masProsperas;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return masDecadentes;
    }

    public int gananciaPromedioPorTraslado(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return sumaGanancias/trasladosDespachados;
    }

    public static void main(String[] args) {
        // Traslado t0 = new Traslado(0, 0, 0, 0, 10);
        // Traslado t1 = new Traslado(1, 0, 0, 2, 3);
        // Traslado t2 = new Traslado(2, 0, 0, 5, 0);
        // BestEffort best = new BestEffort(0, new Traslado[]{t0,t1,t2});

        Traslado [] listaTraslados = new Traslado[] {
            new Traslado(1, 0, 1, 100, 10),
            new Traslado(2, 0, 1, 400, 20),
            new Traslado(3, 3, 4, 500, 50),
            new Traslado(4, 4, 3, 500, 11),
            new Traslado(5, 1, 0, 1000, 40),
            new Traslado(6, 1, 0, 1000, 41),
            new Traslado(7, 6, 3, 2000, 42)
        };

        BestEffort sis = new BestEffort(7, listaTraslados);
        sis.despacharMasRedituables(3);
        sis.despacharMasAntiguos(3);
        sis.despacharMasAntiguos(1);

    }
    
}
