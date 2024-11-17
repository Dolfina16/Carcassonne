package aed;

import java.util.ArrayList;
import java.util.Comparator;


public class BestEffort {
    private ArrayList<Integer> masProsperas = new ArrayList<Integer>(); //Lista de las ciudades con mayor ganancia
    private ArrayList<Integer> masDecadentes = new ArrayList<Integer>(); //Lista de las ciudades con mayor pérdida
    private int sumaGanancias; //Suma total de las ganancias netas de los pedidos ya despachados
    private int trasladosDespachados; //Cantidad total de pedidos ya despachados
    private Heap<Ciudad> heapCSuperavit; //Heap de ciudades ordenadas por su superávit
    private Tupla<Ciudad, Handler>[] ciudadesPorId; //Lista de las ciudades y su posición dentro del heapCSuperavit ordenadas por id para su rápida modificación. Tamaño fijo
    private Heap<Traslado> heapTRedituable; //Heap de traslados ordenados por ganancia neta
    private Heap<Traslado> heapTAntiguedad; //Heap de traslados ordenados por timestamp

    // C = |ciudadesPorId| = |heapCSuperavit|, T = |heapTRedituable| = |heapTAntiguedad|

    // Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);

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
        //tF = 2 + t = 4 + n*7 --> O(n)
    }

    public <T> BestEffort(int cantCiudades, Traslado[] traslados){  //COMPLEJIDAD DE LA FUNCION: O(C + T)
        crearCiudades(cantCiudades);// Crea los objetos Ciudad y los almacena ordenados en la lista ciudadesPorId. T(C,T) = C
        heapCSuperavit = new Heap<Ciudad>(ciudadesPorId, SuperavitComparador); //Inicializa el heap de ciudades. T(C,T) = 2C

        Tupla<Traslado,Handler>[] trasladosR = new Tupla[traslados.length]; //Crea la lista de tuplas <Traslado,Handler> que recibe como parámetro el constructor del heap. T(C,T) = 2C + T
        Tupla<Traslado,Handler>[] trasladosA = new Tupla[traslados.length]; // T(C,T) = 2C + 2T

        for (int i = 0; i < traslados.length; i++) { // 2, T iteraciones
            trasladosR[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerRedi()); //Asigna a cada tupla un traslado con su posición dentro del heapTRedituable. T = 6
            trasladosA[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerAnti()); //Asigna a cada tupla un traslado con su posición dentro del heapTAntiguedad. T = 6
        } // t = 2 + T(12 + 2) = 2 + 14T --> T(C,T) = 2C + 16T + 2

        heapTRedituable = new Heap<Traslado>(trasladosR, RedituabilidadComparator); //Inicializa el heap de traslados ordenados por ganancia neta. T(C,T) = 2C + 18T + 2
        heapTAntiguedad = new Heap<Traslado>(trasladosA, AntiguedadComparator); //Inicializa el heap de traslados ordenados por timestamp. T(C,T) = 2C + 20T + 2
    } //    T(C,T) = 20T + 2C + 2 --> O(C + T)


    public void registrarTraslados(Traslado[] traslados){   //COMPLEJIDAD DE LA FUNCION: O(|traslados|log(T))
        for(int i=0; i< traslados.length; i++){ // 2, |traslados| iteraciones
            heapTRedituable.Añadir(new Tupla<>(traslados[i], traslados[i].handlerRedi())); //log(T)

            heapTAntiguedad.Añadir(new Tupla<>(traslados[i], traslados[i].handlerAnti())); //log(T)
        } 
    }   //tF = 2 + |traslados|(2(log(T) + 2) --> O(|traslados|log(T))

    public <T> int[] despacharMasRedituables(int n){ //COMPLEJIDAD DE LA FUNCION: O(n(log(T) + log(C)))
        int aDespachar = Math.min(n, heapTRedituable.tamaño()); // T(n) = 2
        int[] idsDespachados = new int[aDespachar]; //Crea una lista para almacenar los ids de los despachos realizados. T(n) = 5
        Traslado despachado; // T(n) = 6
        for (int i = 0; i < aDespachar; i++) { //2, n iteraciones
            despachado = heapTRedituable.Sacar(0); //log(T)
            idsDespachados[i] = (despachado.id()); //T(n) = log(T) + 2

            heapTAntiguedad.Sacar(despachado.handlerAnti().ref()); //T(n) = 2log(T) + 2

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); //T(n) = 2log(T) + 7
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); //Reordena el heap de ciudades a partir de la ciudad que incrementó su ganancia. 
                                                                                    //Recibe como parámetro un bool que indica si el superávit creció o decreció. T(n) = 2log(T) + log(C) + 7

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // T(n) = 2log(T) + log(C) + 12
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); //Reordena el heap de ciudades a partir de la ciudad que incrementó su pérdida. T(n) = 2log(T) + 2log(C) + 12

            actualizarMaximos(despachado); //Actualiza las listas masProsperas y masDecadentes. T(n) = 2log(T) + 2log(C) + 13
            
            sumaGanancias += despachado.gananciaNeta(); // T(n) = 2log(T) + 2log(C) + 15
            trasladosDespachados++; // T(n) = 2log(T) + 2log(C) + 16
        } // T(n) = 2log(T) + 2log(C) + 16

        return idsDespachados; // T(n) = 2log(T) + 2log(C) + 17 ---> O(n(log(T) + log(C)))
    }

    public int[] despacharMasAntiguos(int n){   //COMPLEJIDAD DE LA FUNCION: O(|traslados|log(T)) Misma lógica y complejidad que la función anterior.
        int aDespachar = Math.min(n, heapTAntiguedad.tamaño()); // 2
        int[] idsDespachados = new int[aDespachar]; // 2
        Traslado despachado; // 1
        for (int i = 0; i < aDespachar; i++) { // 2, n iteraciones

            despachado = heapTAntiguedad.Sacar(0); // log(T)
            idsDespachados[i] = (despachado.id()); // 2

            heapTRedituable.Sacar(despachado.handlerRedi().ref()); // log(T)

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); //5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // log(C)

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // 5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // log(C)

            actualizarMaximos(despachado); //1
            
            sumaGanancias += despachado.gananciaNeta(); //1
            trasladosDespachados++; //1
        }  // t = 2 + n(2log(T) + 2log(C) + 14 + 2 )

        return idsDespachados; // 1     tF = 6 + t ---> O(n(log(T) + log(C)))
    }

    public void actualizarMaximos(Traslado despachado){ //COMPLEJIDAD DE LA FUNCION: O(1)
        if (masProsperas.size() != 0) { //Si la lista ya fue inicializada, chequea si la ciudad de origen superó en ganancia a la primer ciudad de la lista masProsperas. 1
            if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() < ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia() || masProsperas.get(0) == despachado.origen()){ //12
                masProsperas = new ArrayList<Integer>(); //Si la supera, sobreescribe la lista anterior con una nueva que sólo contenga la ciudad de origen. 2
                masProsperas.add(despachado.origen()); //2
            }else if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() == ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia()){
                masProsperas.add(despachado.origen()); //Si la ganancia es la misma sólo la agrega a la lista. 2
            }
             // t1 = 12 + 4 = 16
        }else{
            masProsperas.add(despachado.origen()); //Si la lista no fue inicializada, agrega la ciudad de origen. 2
        } // t2 = 1 + t1 = 17

        if (masDecadentes.size() != 0) { //Se aplica la misma lógica anterior para la ciudad de destino. 1
            if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() < ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida() || masDecadentes.get(0) == despachado.destino()){ //12
                masDecadentes = new ArrayList<Integer>(); // 2
                masDecadentes.add(despachado.destino()); // 1
            }else if(ciudadesPorId[masDecadentes.get(0)].ObtenerPrimero().perdida() == ciudadesPorId[despachado.destino()].ObtenerPrimero().perdida()){
                masDecadentes.add(despachado.destino()); // 1
            }
            // t3 = 12 + 3 = 15 
        }else{
            masDecadentes.add(despachado.destino()); //1
        } // t4 = 1 + t3 = 16
    } // tf = t2 + t4 = 33 --> O(1)

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
