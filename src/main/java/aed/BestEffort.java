package aed;

import java.util.ArrayList;
import java.util.Comparator;

//q onda el tamaño del conjunto de traslados, una vez q despacho tengo que achicar el conjunto o puedo dejarlo en blanco?

public class BestEffort {
    //Completar atributos privados
    private ArrayList<Integer> mayorGanancia = new ArrayList<Integer>();
    private ArrayList<Integer> mayorPerdida = new ArrayList<Integer>();
    private int sumaGanancias;
    private int trasladosDespachados;
    private Heap<Ciudad> ciudadesSuper;
    private Ciudad[] ciudadesPorId;
    private Heap<Traslado> trasladosRedi;
    private Heap<Traslado> trasladosAnti;

    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    
    public class RediCompa implements Comparator<Traslado>{
        @Override
        public int compare(Traslado tras1, Traslado tras2){
            if (Integer.compare(tras1.gananciaNeta(),tras2.gananciaNeta()) == 0) {
                return Integer.compare(-tras1.id(),-tras2.id());
            }
            return Integer.compare(tras1.gananciaNeta(),tras2.gananciaNeta());
        }
    }
    RediCompa RedituabilidadComparator = new RediCompa();
    
    public class AntiCompa implements Comparator<Traslado>{
        @Override
        public int compare(Traslado tras1, Traslado tras2){
            return Integer.compare(-tras1.timestamp(),-tras2.timestamp());
        }
    }
    AntiCompa AntiguedadComparator = new AntiCompa();
    
    private void agrandar(ArrayList<Tupla> arreglo, int n){
        ArrayList<Tupla> nuevoArreglo = new ArrayList<Tupla>(n);
        for (int j = 0; j < arreglo.size(); j++) {
			nuevoArreglo.set(j,arreglo.get(j));
		}
    }

    private void crearCities(int n){
        ciudadesPorId = new Ciudad[n];
        for (int i = 0; i < n; i++) {
            ciudadesPorId[i] = new Ciudad(i);
        }
    }

    private void asignarRefes(){
        int i = 0;
        int j = 0;
        int k = 0;
        while(i < trasladosRedi.tamaño()){
            trasladosRedi.get_elem(i).set_refRed(i);
            i++;
        }
        while(j < trasladosAnti.tamaño()){
            trasladosAnti.get_elem(j).set_refAnti(j);
            j++;
        }
        while (k < ciudadesSuper.tamaño()) {
            ciudadesSuper.get_elem(k).set_ref(k);
            k++;
        }
    }

    private <T> void cambiarRefes(ArrayList<Tupla<T,Integer>> cambios, int x){
        for (Tupla<T,Integer> e : cambios) {
            if(e.getFirst().getClass().getName() == "aed.Ciudad"){
                Ciudad ciudad = (Ciudad) e.getFirst();
                ciudad.set_ref(e.getSecond());
            }else{
                Traslado traslado = (Traslado) e.getFirst();
                if (x == 0) {
                    traslado.set_refRed(e.getSecond());
                }else{
                    traslado.set_refAnti(e.getSecond());
                }
            }
        }
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        crearCities(cantCiudades);//O(C) t(T,C)=C
        ciudadesSuper = new Heap<Ciudad>(ciudadesPorId, SuperavitComparator); //O(C) t(T,C)=2C
        mayorGanancia.add(0);
        mayorPerdida.add(0);

        trasladosRedi = new Heap<Traslado>(traslados, RedituabilidadComparator); //O(T) t(T,C)=2C + T
        trasladosAnti = new Heap<Traslado>(traslados, AntiguedadComparator); //O(T) t(T,C)=2C + 2T
        asignarRefes(); // O(T) t(T,C)=2C + 4T
    } //O(C+T)

    public void registrarTraslados(Traslado[] traslados){
        ArrayList<Tupla<Traslado,Integer>> mutados;
        for(int i=0; i< traslados.length; i++){
            
            mutados = trasladosRedi.Anhadir(traslados[i]); //O(log T)
            cambiarRefes(mutados, 0);//O(log T)

            mutados = trasladosAnti.Anhadir(traslados[i]); //O(log T)
            cambiarRefes(mutados, 1); //O(log T)        
        
        } //O[|traslados|.(4.log(T))]
    }

    public <T> int[] despacharMasRedituables(int n){ //O(n(log(T)+log(C)))
        int aDespachar = Math.min(n, trasladosRedi.tamaño());
        int[] idsDespachados = new int[aDespachar];
        ArrayList<Tupla<Traslado,Integer>> tras_mutantes;
        ArrayList<Tupla<Ciudad,Integer>> ciu_mutantes;
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {

            tras_mutantes = trasladosRedi.sacar(0); // O(log(T))
            despachado = tras_mutantes.removeFirst().getFirst(); // O(1)
            idsDespachados[i] = (despachado.id()); // O(1)
            cambiarRefes(tras_mutantes,0); // O(log(T))

            tras_mutantes = trasladosAnti.sacar(despachado.refs().getSecond()); // O(log(T))
            tras_mutantes.removeFirst(); // O(1)
            cambiarRefes(tras_mutantes,1); // O(log(T))

            ciudadesPorId[despachado.origen()].incr_ganancia(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.origen()].ref(), 0); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            ciudadesPorId[despachado.destino()].incr_perdida(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.destino()].ref(), -1); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia() || mayorGanancia.getFirst() == despachado.origen()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();
                mayorGanancia.add(despachado.origen());
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen());
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].perdida() < ciudadesPorId[despachado.destino()].perdida() || mayorPerdida.getFirst() == despachado.destino()){
                mayorPerdida = new ArrayList<Integer>();
                mayorPerdida.add(despachado.destino());
            }else if(ciudadesPorId[mayorPerdida.getFirst()].perdida() == ciudadesPorId[despachado.destino()].perdida()){
                mayorPerdida.add(despachado.destino());
            }
            
            sumaGanancias += despachado.gananciaNeta();
            trasladosDespachados++;
        }
        return idsDespachados;
    }

    public int[] despacharMasAntiguos(int n){
        int aDespachar = Math.min(n, trasladosAnti.tamaño());
        int[] idsDespachados = new int[aDespachar];
        ArrayList<Tupla<Traslado,Integer>> tras_mutantes;
        ArrayList<Tupla<Ciudad,Integer>> ciu_mutantes;
        Traslado despachado;
        for (int i = 0; i < aDespachar; i++) {

            tras_mutantes = trasladosAnti.sacar(0); // O(log(T))
            despachado = tras_mutantes.removeFirst().getFirst(); // O(1)
            idsDespachados[i] = (despachado.id()); // O(1)
            cambiarRefes(tras_mutantes,1); // O(log(T))

            tras_mutantes = trasladosRedi.sacar(despachado.refs().getFirst()); // O(log(T))
            tras_mutantes.removeFirst(); // O(1)
            cambiarRefes(tras_mutantes,0); // O(log(T))

            ciudadesPorId[despachado.origen()].incr_ganancia(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.origen()].ref(), 0); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            ciudadesPorId[despachado.destino()].incr_perdida(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.destino()].ref(), -1); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia() || mayorGanancia.getFirst() == despachado.origen()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();
                mayorGanancia.add(despachado.origen());
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen());
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].perdida() < ciudadesPorId[despachado.destino()].perdida() || mayorPerdida.getFirst() == despachado.destino()){
                mayorPerdida = new ArrayList<Integer>();
                mayorPerdida.add(despachado.destino());
            }else if(ciudadesPorId[mayorPerdida.getFirst()].perdida() == ciudadesPorId[despachado.destino()].perdida()){
                mayorPerdida.add(despachado.destino());
            }
            
            sumaGanancias += despachado.gananciaNeta();
            trasladosDespachados++;
        }
        return idsDespachados;
    }

    public int ciudadConMayorSuperavit(){
        return ciudadesSuper.maximus().id();
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        return mayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        return mayorPerdida;
    }

    public int gananciaPromedioPorTraslado(){
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
