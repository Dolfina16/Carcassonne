package aed;

import java.util.ArrayList;
import java.util.Comparator;


public class BestEffort {
    private ArrayList<Integer> mayorGanancia = new ArrayList<Integer>();
    private ArrayList<Integer> mayorPerdida = new ArrayList<Integer>();
    private int sumaGanancias;
    private int trasladosDespachados;
    private Heap<Ciudad> heapCSuperavit;
    private Ciudad[] ciudadesPorId;
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
        ciudadesPorId = new Ciudad[n];  // O(1)
        for (int i = 0; i < n; i++) {
            ciudadesPorId[i] = new Ciudad(i);   //O(1)
        }
    }

    private void asignarRefes(){    //COMPLEJIDAD DE LA FUNCION: 
        int i = 0;
        int j = 0;
        int k = 0;
        while(i < heapTRedituable.tamaño()){
            heapTRedituable.obtenerElem(i).setear_refRedituable(i); //O(1)
            i++;    //O(1)
        }
        while(j < heapTAntiguedad.tamaño()){
            heapTAntiguedad.obtenerElem(j).setear_refAntiguedad(j); //O(1)
            j++; //O(1)
        }
        while (k < heapCSuperavit.tamaño()) {
            heapCSuperavit.obtenerElem(k).setear_ref(k); //O(1)
            k++; //O(1)
        }
    }

    private <T> void cambiarRefes(ArrayList<Tupla<T,Integer>> cambios, int x){  //COMPLEJIDAD DE LA FUNCION:
        for (Tupla<T,Integer> e : cambios) {
            if(e.ObtenerPrimero().getClass().getName() == "aed.Ciudad"){
                Ciudad ciudad = (Ciudad) e.ObtenerPrimero(); //O(1)
                ciudad.setear_ref(e.ObtenerSegundo()); //O(1)
            }else{
                Traslado traslado = (Traslado) e.ObtenerPrimero(); //O(1)
                if (x == 0) {
                    traslado.setear_refRedituable(e.ObtenerSegundo()); //O(1)
                }else{
                    traslado.setear_refAntiguedad(e.ObtenerSegundo()); //O(1)
                }
            }
        }
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){  //COMPLEJIDAD DE LA FUNCION:
        crearCiudades(cantCiudades);//O(C) t(T,C)=C
        heapCSuperavit = new Heap<Ciudad>(ciudadesPorId, SuperavitComparator); //O(C) t(T,C)=2C
        mayorGanancia.add(0);   //O(1)
        mayorPerdida.add(0);    //O(1)

        heapTRedituable = new Heap<Traslado>(traslados, RedituabilidadComparator); //O(T) t(T,C)=2C + T
        heapTAntiguedad = new Heap<Traslado>(traslados, AntiguedadComparator); //O(T) t(T,C)=2C + 2T
        asignarRefes(); // O(T) t(T,C)=2C + 4T
    } //O(C+T)

    public void registrarTraslados(Traslado[] traslados){   //COMPLEJIDAD DE LA FUNCION:
        ArrayList<Tupla<Traslado,Integer>> mutados;
        for(int i=0; i< traslados.length; i++){
            
            mutados = heapTRedituable.Agregar(traslados[i]); //O(log T)
            cambiarRefes(mutados, 0);//O(log T)

            mutados = heapTAntiguedad.Agregar(traslados[i]); //O(log T)
            cambiarRefes(mutados, 1); //O(log T)        
        
        } //O[|traslados|.(4.log(T))]
    }

    public <T> int[] despacharMasRedituables(int n){ //COMPLEJIDAD DE LA FUNCION: O(n(log(T)+log(C)))
        int aDespachar = Math.min(n, heapTRedituable.tamaño()); //O(1)
        int[] idsDespachados = new int[aDespachar]; //O(1)
        ArrayList<Tupla<Traslado,Integer>> tras_mutados;
        ArrayList<Tupla<Ciudad,Integer>> ciu_mutados;
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {

            tras_mutados = heapTRedituable.sacar(0); // O(log(T))
            despachado = tras_mutados.removeFirst().ObtenerPrimero(); // O(1)
            idsDespachados[i] = (despachado.id()); // O(1)
            cambiarRefes(tras_mutados,0); // O(log(T))

            tras_mutados = heapTAntiguedad.sacar(despachado.refs().ObtenerSegundo()); // O(log(T))
            tras_mutados.removeFirst(); // O(1)
            cambiarRefes(tras_mutados,1); // O(log(T))

            ciudadesPorId[despachado.origen()].incr_ganancia(despachado.gananciaNeta()); // O(1)
            ciu_mutados = heapCSuperavit.reordenar(ciudadesPorId[despachado.origen()].ref(), 0); // O(log(C))
            cambiarRefes(ciu_mutados,0); // O(log(C))

            ciudadesPorId[despachado.destino()].incr_perdida(despachado.gananciaNeta()); // O(1)
            ciu_mutados = heapCSuperavit.reordenar(ciudadesPorId[despachado.destino()].ref(), -1); // O(log(C))
            cambiarRefes(ciu_mutados,0); // O(log(C))

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia() || mayorGanancia.getFirst() == despachado.origen()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();   //O(1)
                mayorGanancia.add(despachado.origen()); //O(1)
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen()); //O(1)
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].perdida() < ciudadesPorId[despachado.destino()].perdida() || mayorPerdida.getFirst() == despachado.destino()){
                mayorPerdida = new ArrayList<Integer>(); //O(1)
                mayorPerdida.add(despachado.destino()); //O(1)
            }else if(ciudadesPorId[mayorPerdida.getFirst()].perdida() == ciudadesPorId[despachado.destino()].perdida()){
                mayorPerdida.add(despachado.destino()); //O(1)
            }
            
            sumaGanancias += despachado.gananciaNeta(); //O(1)
            trasladosDespachados++; //O(1)
        }
        return idsDespachados; 
    }

    public int[] despacharMasAntiguos(int n){   //COMPLEJIDAD DE LA FUNCION:
        int aDespachar = Math.min(n, heapTAntiguedad.tamaño()); //O(1)
        int[] idsDespachados = new int[aDespachar]; //O(1)
        ArrayList<Tupla<Traslado,Integer>> tras_mutados;
        ArrayList<Tupla<Ciudad,Integer>> ciu_mutados;
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {

            tras_mutados = heapTAntiguedad.sacar(0); // O(log(T))
            despachado = tras_mutados.removeFirst().ObtenerPrimero(); // O(1)
            idsDespachados[i] = (despachado.id()); // O(1)
            cambiarRefes(tras_mutados,1); // O(log(T))

            tras_mutados = heapTRedituable.sacar(despachado.refs().ObtenerPrimero()); // O(log(T))
            tras_mutados.removeFirst(); // O(1)
            cambiarRefes(tras_mutados,0); // O(log(T))

            ciudadesPorId[despachado.origen()].incr_ganancia(despachado.gananciaNeta()); // O(1)
            ciu_mutados = heapCSuperavit.reordenar(ciudadesPorId[despachado.origen()].ref(), 0); // O(log(C))
            cambiarRefes(ciu_mutados,0); // O(log(C))

            ciudadesPorId[despachado.destino()].incr_perdida(despachado.gananciaNeta()); // O(1)
            ciu_mutados = heapCSuperavit.reordenar(ciudadesPorId[despachado.destino()].ref(), -1); // O(log(C))
            cambiarRefes(ciu_mutados,0); // O(log(C))

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia() || mayorGanancia.getFirst() == despachado.origen()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();   //O(1)
                mayorGanancia.add(despachado.origen());     //O(1)
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen());     //O(1)
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].perdida() < ciudadesPorId[despachado.destino()].perdida() || mayorPerdida.getFirst() == despachado.destino()){
                mayorPerdida = new ArrayList<Integer>(); //O(1)
                mayorPerdida.add(despachado.destino()); //O(1)
            }else if(ciudadesPorId[mayorPerdida.getFirst()].perdida() == ciudadesPorId[despachado.destino()].perdida()){
                mayorPerdida.add(despachado.destino()); //O(1)
            }
            
            sumaGanancias += despachado.gananciaNeta(); //O(1)
            trasladosDespachados++; //O(1)
        }
        return idsDespachados;
    }

    public int ciudadConMayorSuperavit(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return heapCSuperavit.maximo().id();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){ //COMPLEJIDAD DE LA FUNCION: O(1)
        return mayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){   //COMPLEJIDAD DE LA FUNCION: O(1)
        return mayorPerdida;
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
