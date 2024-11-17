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

    // n = |ciuadesPorId|, n = |heapCSuperavit|, m = |heapTRedituable|, m = |heapTAntiguedad|, j = |masProsperas|, k = |masDecadentes|

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

    public <T> BestEffort(int cantCiudades, Traslado[] traslados){  //COMPLEJIDAD DE LA FUNCION: O(n + m)
        crearCiudades(cantCiudades);// n
        heapCSuperavit = new Heap<Ciudad>(ciudadesPorId, SuperavitComparator); //2

        Tupla<Traslado,Handler>[] trasladosR = new Tupla[traslados.length]; //3
        Tupla<Traslado,Handler>[] trasladosA = new Tupla[traslados.length]; //3

        for (int i = 0; i < traslados.length; i++) { // 2, |traslados| iteraciones
            trasladosR[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerRedi()); //5
            trasladosA[i] = new Tupla<Traslado,Handler>(traslados[i], traslados[i].handlerAnti()); //5
        }
         // t = 2 + |traslados|(10 + 2) = 2 + |traslados|*12

        heapTRedituable = new Heap<Traslado>(trasladosR, RedituabilidadComparator); // 2
        heapTAntiguedad = new Heap<Traslado>(trasladosA, AntiguedadComparator); // 2
    } //    tF = n + 6 + t = n + 8 + |traslados|*12 --> O(n + m) (esta acotado por m tambien, porque en este caso |traslados| va a ser
    //      la longitud que determina m )


    public void registrarTraslados(Traslado[] traslados){   //COMPLEJIDAD DE LA FUNCION: O(|traslados|log(n))
        for(int i=0; i< traslados.length; i++){ // 2, |traslados| iteraciones
            heapTRedituable.Añadir(new Tupla<>(traslados[i], traslados[i].handlerRedi())); //log(n)

            heapTAntiguedad.Añadir(new Tupla<>(traslados[i], traslados[i].handlerAnti())); //log(n)
        } 
    }   //tF = 2 + |traslados|(2(log(n) + 2) --> O(|traslados|log(n))

    public <T> int[] despacharMasRedituables(int n){ //COMPLEJIDAD DE LA FUNCION: O(n'(log(m) + log(n)))
                                                    //tomo n' = n 
        int aDespachar = Math.min(n, heapTRedituable.tamaño()); //2
        int[] idsDespachados = new int[aDespachar]; //3
        Traslado despachado; //1
        for (int i = 0; i < aDespachar; i++) { //2, n' iteraciones
            despachado = heapTRedituable.Sacar(0); //log(m)
            idsDespachados[i] = (despachado.id()); //2

            heapTAntiguedad.Sacar(despachado.handlerAnti().ref()); //log(m)

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); //5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // log(n)

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); //5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); //log(n) 

            actualizarMaximos(despachado); // 1
            
            sumaGanancias += despachado.gananciaNeta(); // 1
            trasladosDespachados++; // 1
        } // t = 2 + n'(2log(m) + 2log(n) + 14 + 2 )

        return idsDespachados; //1   tF = 6 + t ---> O(n'(log(m) + log(n)))
    }

    public void actualizarMaximos(Traslado despachado){ //COMPLEJIDAD DE LA FUNCION: O(1)
        if (masProsperas.size() != 0) { //1
            if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() < ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia() || masProsperas.get(0) == despachado.origen()){ //12
                masProsperas = new ArrayList<Integer>();   //2
                masProsperas.add(despachado.origen()); //2
            }else if(ciudadesPorId[masProsperas.get(0)].ObtenerPrimero().ganancia() == ciudadesPorId[despachado.origen()].ObtenerPrimero().ganancia()){
                masProsperas.add(despachado.origen()); //2
            }
             // t1 = 12 + 4 = 16
        }else{
            masProsperas.add(despachado.origen()); //2
        } // t2 = 1 + t1 = 17

        if (masDecadentes.size() != 0) { //1
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

    public int[] despacharMasAntiguos(int n){   //COMPLEJIDAD DE LA FUNCION: O(n'(log(m) + log(n)))
                                                //tomo n' = n 
        int aDespachar = Math.min(n, heapTAntiguedad.tamaño()); // 2
        int[] idsDespachados = new int[aDespachar]; // 2
        Traslado despachado; // 1
        for (int i = 0; i < aDespachar; i++) { // 2, n' iteraciones

            despachado = heapTAntiguedad.Sacar(0); // log(m)
            idsDespachados[i] = (despachado.id()); // 2

            heapTRedituable.Sacar(despachado.handlerRedi().ref()); // log(m)

            ciudadesPorId[despachado.origen()].ObtenerPrimero().incr_ganancia(despachado.gananciaNeta()); //5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.origen()].ObtenerSegundo().ref(), true); // log(n)

            ciudadesPorId[despachado.destino()].ObtenerPrimero().incr_perdida(despachado.gananciaNeta()); // 5
            heapCSuperavit.Reordenar(ciudadesPorId[despachado.destino()].ObtenerSegundo().ref(), false); // log(n)

            actualizarMaximos(despachado); //1
            
            sumaGanancias += despachado.gananciaNeta(); //1
            trasladosDespachados++; //1
        }  // t = 2 + n'(2log(m) + 2log(n) + 14 + 2 )

        return idsDespachados; // 1     tF = 6 + t ---> O(n'(log(m) + log(n)))
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
