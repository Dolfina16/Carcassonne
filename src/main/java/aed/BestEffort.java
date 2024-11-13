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

    private void cambiarRefes(ArrayList<Tupla<Traslado,Integer>> mutantes, int x){
        for (Tupla<Traslado,Integer> mutante : mutantes) {
            Traslado traslado = mutante.getFirst();
            if (x == 0) {
                traslado.set_refRed(mutante.getSecond());
            }else{
                traslado.set_refAnti(mutante.getSecond());
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
        ArrayList<Tupla<Traslado,Integer>> mutantes;
        for(int i=0; i< traslados.length; i++){
            
            mutantes = trasladosRedi.Anhadir(traslados[i]); //O(log T)
            cambiarRefes(mutantes, 0); //O(log T)

            mutantes = trasladosAnti.Anhadir(traslados[i]); //O(log T)
            cambiarRefes(mutantes, 1); //O(log T)        
        
        } //O[|traslados|.(4.log(T))]
    }

    public int[] despacharMasRedituables(int n){ //O(n(log(T)+log(C)))
        int cantDespachados = Math.min(n, trasladosRedi.tamaño());
        int[] idsDespachados = new int[cantDespachados];
        ArrayList<Tupla<Traslado,Integer>> mutantes;
        Tupla<Traslado,Integer> primerMutante;
        Traslado tConMasRedi;
        for (int i = 0; i < cantDespachados; i++) {
            mutantes = trasladosRedi.sacar(0);
            primerMutante = mutantes.removeFirst();
            tConMasRedi = primerMutante.getFirst();

            idsDespachados[i] = (tConMasRedi.id());
            sumaGanancias += tConMasRedi.gananciaNeta();
            trasladosDespachados++;
            ciudadesPorId[tConMasRedi.origen()].incr_ganancia(tConMasRedi.gananciaNeta());
            ciudadesPorId[tConMasRedi.destino()].incr_perdida(tConMasRedi.gananciaNeta());

            //ACA NOS CHOCAMOS CON LA REALIDAD DE QUE PROBABLEMTE TENGAMOS QUE TENER:
            // - Una funcion reordenar que me actualice el heap de superavit
            // - Una referencia al indice de la ciudad en el heap de superavit porque sino no se donde carancho esta
            // PANIC
            // PANIC
            // PANIC
            // PANIC
            
            cambiarRefes(mutantes, 0);
        }

        // for(int i=0; i< Math.min(n,trasladosAnti); i++){
        //     //idTraslado <- trasladosRedi.sacarMaximo //O(log(T))
        //     //(Ir,Ia) <- refes.obtener(idTraslado)
        //     //traslad
        // }
        return null;
    }

    public int[] despacharMasAntiguos(int n){
        // Impleme
        return null;
    }

    public int ciudadConMayorSuperavit(){
        // Implementar
        return 0;
    }

    public ArrayList<Integer> ciudadesConMayorGanancia(){
        // Implementar
        return mayorGanancia;
    }

    public ArrayList<Integer> ciudadesConMayorPerdida(){
        // Implementar
        return mayorPerdida;
    }

    public int gananciaPromedioPorTraslado(){
        // escribir
        return sumaGanancias/trasladosDespachados;
    }

    public static void main(String[] args) {
        Traslado t0 = new Traslado(0, 0, 0, 0, 10);
        Traslado t1 = new Traslado(1, 0, 0, 2, 3);
        Traslado t2 = new Traslado(2, 0, 0, 5, 0);
        BestEffort best = new BestEffort(0, new Traslado[]{t0,t1,t2});
    }
    
}
