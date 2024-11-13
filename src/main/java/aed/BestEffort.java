package aed;

import java.util.ArrayList;
import java.util.Comparator;

//q onda el tamaño del conjunto de traslados, una vez q despacho tengo que achicar el conjunto o puedo dejarlo en blanco?

public class BestEffort {
    //Completar atributos privados
    private ArrayList<Integer> mayorGanancia;
    private ArrayList<Integer> mayorPerdida;
    private int sumaGanancias;
    private int trasladosDespachados;
    private Heap<Ciudad> ciudadesSuper;
    private Ciudad[] ciudadesPorId;
    private Heap<Traslado> trasladosRedi;
    private Heap<Traslado> trasladosAnti;

    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    Comparator<Traslado> AntiguedadComparator = Comparator.comparing(Traslado::timestamp);
    Comparator<Traslado> RedituabilidadComparator = Comparator.comparing(Traslado::gananciaNeta);

    private void agrandar(ArrayList<Tupla> arreglo, int n){
        ArrayList<Tupla> nuevoArreglo = new ArrayList<Tupla>(n);
        for (int j = 0; j < arreglo.size(); j++) {
			nuevoArreglo.set(j,arreglo.get(j));
		}
    }

    private void crearCities(int n){
        for (int i = 0; i < n; i++) {
            ciudadesPorId[i] = new Ciudad(i);
        }
    }

    private void asignarRefes(){
        int i = 0;
        while(i < trasladosRedi.tamaño()){
            trasladosRedi.get_elem(i).set_refRed(i);
            i+=1;
        }
        while(i < trasladosAnti.tamaño()){
            trasladosAnti.get_elem(i).set_refAnti(i);
            i+=1;
        }
    }

    private <T> void cambiarRefes(ArrayList<Tupla<T,Integer>> cambios, int x){
        for (Tupla<T,Integer> e : cambios) {
            if(e.getClass().getName() == "Ciudad"){
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

        trasladosRedi = new Heap<Traslado>(traslados, RedituabilidadComparator); //O(T) t(T,C)=2C + T
        trasladosAnti = new Heap<Traslado>(traslados, AntiguedadComparator); //O(T) t(T,C)=2C + 2T
        asignarRefes(); // O(T) t(T,C)=2C + 4T
    } //O(C+T)

    public void registrarTraslados(Traslado[] traslados){
        ArrayList<Tupla<Traslado,Integer>> mutados;
        for(int i=0; i< traslados.length; i++){
            
            mutados = trasladosRedi.Anhadir(traslados[i]); //O(log T)
            cambiarRefes(mutados, 0); //O(log T)

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

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();
                mayorGanancia.add(despachado.origen());
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen());
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].ganancia() > ciudadesPorId[despachado.destino()].ganancia()){
                mayorPerdida = new ArrayList<Integer>();
                mayorPerdida.add(despachado.destino());
            }else if(ciudadesPorId[mayorPerdida.getFirst()].ganancia() == ciudadesPorId[despachado.destino()].ganancia()){
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
            cambiarRefes(tras_mutantes,0); // O(log(T))

            tras_mutantes = trasladosRedi.sacar(despachado.refs().getSecond()); // O(log(T))
            tras_mutantes.removeFirst(); // O(1)
            cambiarRefes(tras_mutantes,1); // O(log(T))

            ciudadesPorId[despachado.origen()].incr_ganancia(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.origen()].ref(), 0); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            ciudadesPorId[despachado.destino()].incr_perdida(despachado.gananciaNeta()); // O(1)
            ciu_mutantes = ciudadesSuper.reordenar(ciudadesPorId[despachado.destino()].ref(), -1); // O(log(C))
            cambiarRefes(ciu_mutantes,0); // O(log(C))

            if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() < ciudadesPorId[despachado.origen()].ganancia()){ // O(1)
                mayorGanancia = new ArrayList<Integer>();
                mayorGanancia.add(despachado.origen());
            }else if(ciudadesPorId[mayorGanancia.getFirst()].ganancia() == ciudadesPorId[despachado.origen()].ganancia()){
                mayorGanancia.add(despachado.origen());
            }

            if(ciudadesPorId[mayorPerdida.getFirst()].ganancia() > ciudadesPorId[despachado.destino()].ganancia()){
                mayorPerdida = new ArrayList<Integer>();
                mayorPerdida.add(despachado.destino());
            }else if(ciudadesPorId[mayorPerdida.getFirst()].ganancia() == ciudadesPorId[despachado.destino()].ganancia()){
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
        Traslado t0 = new Traslado(0, 0, 0, 0, 10);
        Traslado t1 = new Traslado(1, 0, 0, 2, 3);
        Traslado t2 = new Traslado(2, 0, 0, 5, 0);
        BestEffort best = new BestEffort(0, new Traslado[]{t0,t1,t2});
    }
    
}
