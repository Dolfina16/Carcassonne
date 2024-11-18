package aed;

import java.util.ArrayList;
import java.util.Comparator;


public class BestEffort {
    private ArrayList<Integer> masProsperas; //Lista de las ciudades con mayor ganancia
    private ArrayList<Integer> masDecadentes; //Lista de las ciudades con mayor pérdida
    private int sumaGanancias; //Suma total de las ganancias netas de los pedidos ya despachados
    private int trasladosDespachados; //Cantidad total de pedidos ya despachados
    private Heap<Ciudad> heapCSuperavit; //Heap de ciudades ordenadas por su superávit
    private Tupla<Ciudad, Handler>[] ciudadesPorId; //Lista de las ciudades y su posición dentro del heapCSuperavit ordenadas por id para su rápida modificación. Tamaño fijo
    private Heap<Traslado> heapTRedituable; //Heap de traslados ordenados por ganancia neta
    private Heap<Traslado> heapTAntiguedad; //Heap de traslados ordenados por timestamp

    // C = |ciudadesPorId| = |heapCSuperavit|, T = |heapTRedituable| = |heapTAntiguedad|

    public class SuperavitComparador implements Comparator<Ciudad>{
        @Override
        public int compare(Ciudad ciu1, Ciudad ciu2){     //COMPLEJIDAD DE LA FUNCION: O(1) 
            if (Integer.compare(ciu1.superavit(),ciu2.superavit()) == 0) {
                return Integer.compare(-ciu1.id(),-ciu2.id());
            }
            return Integer.compare(ciu1.superavit(),ciu2.superavit());
        }
    }
    SuperavitComparador SuperavitComparador = new SuperavitComparador();
    
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
        public int compare(Traslado tras1, Traslado tras2){  //COMPLEJIDAD DE LA FUNCION: O(1)
            return Integer.compare(-tras1.timestamp(),-tras2.timestamp());
        }
    }
    AntigComparador AntiguedadComparator = new AntigComparador();

    private void crearCiudades(int n){  //COMPLEJIDAD DE LA FUNCION: O(n)
        ciudadesPorId = new Tupla[n];  //2
        for (int i = 0; i < n; i++) { // 2, n iteraciones
            Ciudad ciudad = new Ciudad(i); //2
            ciudadesPorId[i] = new Tupla<Ciudad,Handler>(ciudad, ciudad.handler()); //3
        } // t = 2 + n(5 + 2) = 2 + n*7
        //tF = 2 + t = 4 + n*7 ---> O(n)
    }

    public <T> BestEffort(int cantCiudades, Traslado[] traslados){  //COMPLEJIDAD DE LA FUNCION: O(C + T)
        //Inicializamos los arreglos de ciudades mas prosperas y mas decadentes
        masProsperas = new ArrayList<Integer>();
        masDecadentes = new ArrayList<Integer>();

        // Creamos los objetos Ciudad y los almacena ordenados en la lista ciudadesPorId.
        crearCiudades(cantCiudades); // C
        //Inicializamos el heap de ciudades.
        heapCSuperavit = new Heap<Ciudad>(ciudadesPorId, SuperavitComparador); // C

        //Creamos la lista de tuplas <Traslado,Handler> que recibe como parámetro el constructor del heap.
        Tupla<Traslado,Handler>[] trasladosR = new Tupla[traslados.length]; // 2
        Tupla<Traslado,Handler>[] trasladosA = new Tupla[traslados.length]; // 2

        for (int i = 0; i < traslados.length; i++) { // 2, T iteraciones
            //Asignamos a cada tupla un traslado con su posición dentro del heapTRedituable.
            trasladosR[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerRedi()); // 6
            //Asignamos a cada tupla un traslado con su posición dentro del heapTAntiguedad.
            trasladosA[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerAnti()); // 6
        } // t = 2 + T(12 + 2) = 2 + 14T

        //Inicializamos el heap de traslados ordenados por ganancia neta.
        heapTRedituable = new Heap<Traslado>(trasladosR, RedituabilidadComparator); // T
        //Inicializamos el heap de traslados ordenados por timestamp.
        heapTAntiguedad = new Heap<Traslado>(trasladosA, AntiguedadComparator); // T
    } // tF = 2C + 4 + t + 2T ---> O(C + T)


    public void registrarTraslados(Traslado[] traslados){   //COMPLEJIDAD DE LA FUNCION: O(|traslados|log(T))
        for(int i=0; i< traslados.length; i++){ // 2, |traslados| iteraciones
            heapTRedituable.Añadir(new Tupla<>(traslados[i], traslados[i].handlerRedi())); // log(T)

            heapTAntiguedad.Añadir(new Tupla<>(traslados[i], traslados[i].handlerAnti())); // log(T)
        } 
    }   //tF = 2 + |traslados|(2(log(T) + 2) ---> O(|traslados|log(T))

    public <T> int[] despacharMasRedituables(int n){ //COMPLEJIDAD DE LA FUNCION: O(n(log(T) + log(C)))
        int aDespachar = Math.min(n, heapTRedituable.tamaño()); // 3
        //Creamos una lista para almacenar los ids de los despachos realizados.
        int[] idsDespachados = new int[aDespachar]; // 2
        Traslado despachado; // 1
        for (int i = 0; i < aDespachar; i++) { //2, n iteraciones
            despachado = heapTRedituable.Sacar(0); // log(T)
            idsDespachados[i] = (despachado.id()); // 2

            heapTAntiguedad.Sacar(despachado.handlerAnti().ref()); // log(T)

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); // 5
            //Reordenamos el heap de ciudades a partir de la ciudad que incrementó su ganancia.
            //Reordenar recibe como parámetro un bool que indica si el superávit creció o decreció.
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true);  // log(C)

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // 5
            //Reordenanamos el heap de ciudades a partir de la ciudad que incrementó su pérdida.
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // log(C)

            //Actualizamos las listas masProsperas y masDecadentes.
            actualizarMaximos(despachado); // 1
            
            sumaGanancias += despachado.gananciaNeta(); // 2
            trasladosDespachados++; // 1
        } // t = 2 + n(2log(T)+2log(C)+18)

        return idsDespachados; // 1
    } // tF = 6 + t = 8 + n(2log(T)+2log(C)+18) ---> O(n(log(T)+log(C)))

    public int[] despacharMasAntiguos(int n){   //COMPLEJIDAD DE LA FUNCION: O(|traslados|log(T)) Misma lógica y complejidad que la función anterior.
        int aDespachar = Math.min(n, heapTAntiguedad.tamaño()); // 3
        int[] idsDespachados = new int[aDespachar]; // 2
        Traslado despachado; // 1
        for (int i = 0; i < aDespachar; i++) { // 2, n iteraciones

            despachado = heapTAntiguedad.Sacar(0); // log(T)
            idsDespachados[i] = (despachado.id()); // 3

            heapTRedituable.Sacar(despachado.handlerRedi().ref()); // log(T)

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); // 5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // log(C)

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // 5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // log(C)

            actualizarMaximos(despachado); // 1
            
            sumaGanancias += despachado.gananciaNeta(); // 2
            trasladosDespachados++; // 1
        }  // t = 2 + n(2log(T)+2log(C)+18)

        return idsDespachados; // 1
    } // tF = 6 + t = 8 + n(2log(T)+2log(C)+18) ---> O(n(log(T)+log(C)))

    public void actualizarMaximos(Traslado despachado){ //COMPLEJIDAD DE LA FUNCION: O(1)
        //Si la lista no esta vacia, chequea si la ciudad de origen superó en ganancia a la primer ciudad de la lista masProsperas.
        if (masProsperas.size() != 0) { // 2
            if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() < ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia() || masProsperas.get(0) == despachado.origen()){ //13
                //Si la supera, sobreescribe la lista anterior con una nueva que sólo contenga la ciudad de origen.
                masProsperas = new ArrayList<Integer>(); // 2
                masProsperas.add(despachado.origen()); //2
            }else if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() == ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia()){ // 9
                //Si la ganancia es la misma sólo la agrega a la lista.
                masProsperas.add(despachado.origen()); // 2
            }
            // t1 = 13 + 4 = 17
        }else{
            //Si la lista era vacia, agrega la ciudad de origen.
            masProsperas.add(despachado.origen()); // 2
        } // t2 = 2 + t1 = 19

        //Se aplica la misma lógica anterior para la ciudad de destino.
        if (masDecadentes.size() != 0) { // 2
            if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() < ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida() || masDecadentes.get(0) == despachado.destino()){ //13
                masDecadentes = new ArrayList<Integer>(); // 2
                masDecadentes.add(despachado.destino()); // 2
            }else if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() == ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida()){ // 9
                masDecadentes.add(despachado.destino()); // 2
            }
            // t3 = 13 + 4 = 17 
        }else{
            masDecadentes.add(despachado.destino()); // 2
        } // t4 = 2 + t3 = 19
    } // tF = t2 + t4 = 38 ---> O(1)

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
}
