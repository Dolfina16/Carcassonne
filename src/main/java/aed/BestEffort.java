package aed;

import java.util.ArrayList;

//q onda el tamaño del conjunto de traslados, una vez q despacho tengo que achicar el conjunto o puedo dejarlo en blanco?

public class BestEffort {
    //Completar atributos privados
    private int[] mayorGanancia;
    private int[] mayorPerdida;
    private int sumaGanancias;
    private int trasladosDespachados;
    private Heap<Ciudad> ciudades;
    private Heap<Traslado> trasladosRedi;
    private Heap<Traslado> trasladosAnti;
    //ArrayList<(trasladoR,trasladoA)> refes;

    private void agrandar(ArrayList<Tupla> arreglo, int n){
        ArrayList<Tupla> nuevoArreglo = new ArrayList<Tupla>(n);
        for (int j = 0; j < arreglo.size(); j++) {
			nuevoArreglo.set(j,arreglo.get(j));
		}
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        ciudades = new Heap<Ciudad>(cantCiudades); // O(C)
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
        for(int i=0; i< Math.min(n,trasladosAnti.size()); i++){
            //idTraslado <- trasladosRedi.sacarMaximo //O(log(T))
            //(Ir,Ia) <- refes.obtener(idTraslado)
            //traslad
        }
        return null;
    }

    public int[] despacharMasAntiguos(int n){
        // Implementar
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
        // Implementar
        return sumaGanancias/trasladosDespachados;
    }
    
}
