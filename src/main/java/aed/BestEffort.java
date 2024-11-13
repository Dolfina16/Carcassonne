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

    private Ciudad[] crearCities(int canti){
        Ciudad[] arregloCities = new Ciudad[canti];
        for (int i = 0; i < arregloCities.length; i++) {
            arregloCities[i] = new Ciudad(i);
        }
        return arregloCities;
    }

    private void asignarRefes(){
        int i = 0;
        while(i < trasladosRedi.tamaño()){
            trasladosRedi.get_elem(i).set_refRed(i);
            i+=1;
        }
        while(i < trasladosRedi.tamaño()){
            trasladosAnti.get_elem(i).set_refAnti(i);
            i+=1;
        }
    }

    public BestEffort(int cantCiudades, Traslado[] traslados){
        Ciudad[] lasCities = crearCities(cantCiudades); //O(C) t(T,C)=C
        ciudades = new Heap<Ciudad>(lasCities, SuperavitComparator); //O(C) t(T,C)=2C
        trasladosRedi = new Heap<Traslado>(traslados, RentabilidadComparator); //O(T) t(T,C)=2C + T
        trasladosAnti = new Heap<Traslado>(traslados, AntiguedadComparator); //O(T) t(T,C)=2C + 2T
        asignarRefes(); // O(T) t(T,C)=2C + 4T
    } //O(C+T)

    public void registrarTraslados(Traslado[] traslados){
        for(int i=0; i< traslados.length; i++){
            //indiceRedi <- trasladosRedi.agregar(traslados[i]) //O(log(T))
            //indiceAnti <- trasladosAnti.agregar(traslados[i]) //O(log(T))
            //refes.set(traslados[i].id : (indiceRedi,indiceAnti)) //O(log(T))
        } //O[|traslados|.(3.log(T))]
    }

    public int[] despacharMasRedituables(int n){ //O(n(log(T)+log(C))) 
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
