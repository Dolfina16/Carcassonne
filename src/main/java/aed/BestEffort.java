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
    private Heap<Ciudad> ciudades;
    private Heap<Traslado> trasladosRedi;
    private Heap<Traslado> trasladosAnti;
    //ArrayList<(trasladoR,trasladoA)> refes;

    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    Comparator<Traslado> AntiguedadComparator = Comparator.comparing(Traslado::timestamp);
    Comparator<Traslado> RentabilidadComparator = Comparator.comparing(Traslado::gananciaNeta);

    private void agrandar(ArrayList<Tupla> arreglo, int n){
        ArrayList<Tupla> nuevoArreglo = new ArrayList<Tupla>(n);
        for (int j = 0; j < arreglo.size(); j++) {
			nuevoArreglo.set(j,arreglo.get(j));
		}
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        //ciudades = new Heap<Ciudad>(cantCiudades); // O(C)
        //trasladosRedi <-- heapify(traslados, redituabilidad) // O(T)
        //
        //trasladosAnti <-- heapify(traslados, antiguedad) // O(T)
    }

    public void registrarTraslados(Traslado[] traslados){
        for(int i=0; i< traslados.length; i++){
            //indiceRedi <- trasladosRedi.agregar(traslados[i]) //O(log(T))
            //indiceAnti <- trasladosAnti.agregar(traslados[i]) //O(log(T))
            //refes.set(traslados[i].id : (indiceRedi,indiceAnti)) //O(log(T))
        } //O[|traslados|.(3.log(T))]
    }

    public int[] despacharMasRedituables(int n){
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
    
}
