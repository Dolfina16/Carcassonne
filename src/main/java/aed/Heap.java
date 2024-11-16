package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap <T> {
    private ArrayList<Tupla<T,Handler>> elementos;
    private int tamaño;
    private Comparator<T> comparador;

// n = |elementos|

public Heap(Tupla<T,Handler>[] arreglo, Comparator<T> comparador){   //COMPLEJIDAD DE LA FUNCION: O(n)
                                    // n = |arreglo|, |arreglo| va a ser igual a |elementos|
    elementos = new ArrayList<Tupla<T,Handler>>(); //1
    for (Tupla<T,Handler> t : arreglo) {       // 3, n iteraciones
        elementos.add(t); // 1                          
        tamaño += 1;        // 1
    }                       // t = 3 + n(2 + 3)
    this.comparador = comparador; // 1
    for (int i = 0; i < elementos.size(); i++) {
        elementos.get(i).ObtenerSegundo().set_ref(i);
    }
    Heapify();  //O(n)         //      tF = 5 + n(5) + n --> O(n)
}

public T obtenerElem(int indice){     //COMPLEJIDAD DE LA FUNCION:  O(1)
    return elementos.get(indice).ObtenerPrimero();
}

public int tamaño(){   //COMPLEJIDAD DE LA FUNCION: O(1)
    return tamaño;
}

public T maximo(){     //COMPLEJIDAD DE LA FUNCION: O(1)
    return elementos.get(0).ObtenerPrimero();    
}

public void Heapify(){
    int pos = (int)(Math.pow(2, log2(tamaño)))-2;
    if(tamaño>0){
        while (pos >= 0) {
            SiftDown(pos);
            pos-=1;
        }
    }
}

public void Añadir(Tupla<T,Handler> elem){
    elementos.addLast(elem);
    tamaño++;
    if (tamaño>1) {
        SiftUp(tamaño-1);
    }else{
        elem.ObtenerSegundo().set_ref(0);
    }
}

public T Sacar(int pos){
    T res = null;
    if(tamaño == 1 || pos == tamaño-1){
        res = elementos.remove(pos).ObtenerPrimero();
        tamaño --;
    }else if( tamaño > 1){
        res = elementos.get(pos).ObtenerPrimero();
        elementos.set(pos, elementos.removeLast());
        tamaño --;
        SiftDown(pos);
    }
    return res;
}

public void Reordenar(int pos, boolean subio){
    if (subio){
        SiftUp(pos);
    }else{
        SiftDown(pos);
    }
}

public boolean EsHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(heap[0]).ObtenerPrimero();
    if(heap[2] >= tamaño && heap[1] < tamaño){ // 2
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero();
        return comparador.compare(padre, hijo_izq) >= 0; //1
    }else if(heap[1] < tamaño){
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero();
        T hijo_der = elementos.get(heap[2]).ObtenerPrimero();
        return comparador.compare(padre, hijo_izq) >= 0 && comparador.compare(padre, hijo_der) >= 0; //1
    }   // tF = 2 + 1 = 3 --> O(1)
    return true;
}

public Integer log2(int n){
    return (int)Math.floor(Math.log(n)/Math.log(2)); 
}


public void SiftUp(int pos){        //COMPLEJIDAD DE LA FUNCION: O(1)
    Tupla<T,Handler> padre = elementos.get((pos-1)/2);  // 2
    Tupla<T,Handler> hijo = elementos.get(pos);   // 2
    if(pos > 0 && !EsHeap(new int[]{(pos-1)/2,pos, tamaño})){
        elementos.set(pos, padre);  // 2
        elementos.set((pos-1)/2, hijo); // 2
        padre.ObtenerSegundo().set_ref(pos); // 2
        SiftUp((pos-1)/2);
    }else{
        hijo.ObtenerSegundo().set_ref(pos);
    }
}

public void SiftDown(int pos){
    Tupla<T,Handler> t_padre = elementos.get(pos);
    if(!EsHeap(new int[]{pos, pos*2+1,pos*2+2})){
        if(pos*2 + 2 >= tamaño && pos*2+1 < tamaño){
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1);
            elementos.set(pos*2+1, t_padre);
            t_padre.ObtenerSegundo().set_ref(pos*2+1);
            elementos.set(pos,t_hijo_izq);
            t_hijo_izq.ObtenerSegundo().set_ref(pos);
        }else if(pos*2 + 2 < tamaño){
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1);
            T hijo_izq = t_hijo_izq.ObtenerPrimero();
            Tupla<T,Handler> t_hijo_der = elementos.get(pos*2+2);
            T hijo_der = t_hijo_der.ObtenerPrimero();
            Integer pos_hijo_mayor = comparador.compare(hijo_izq, hijo_der) > 0 ? pos*2+1 : pos*2+2;
            elementos.set(pos, elementos.get(pos_hijo_mayor));
            elementos.get(pos).ObtenerSegundo().set_ref(pos);
            elementos.set(pos_hijo_mayor, t_padre);
            SiftDown(pos_hijo_mayor);
        }
    }else{
        t_padre.ObtenerSegundo().set_ref(pos);
    }
}


public String toString(){   
    String res = "[";
    for (Tupla<T,Handler> t : elementos) {     
        res += t.ObtenerPrimero().toString() + ","; 
    }  
    return res + "]";   
}


public static void main(String[] args) {
    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);

    Tupla<Ciudad,Handler>[] ciudades = new Tupla[8];
    for (int i = 0; i < ciudades.length; i++) {
        Handler hand = new Handler(null);
        ciudades[i] = new Tupla<Ciudad,Handler>(new Ciudad(i), hand);
        ciudades[i].ObtenerPrimero().set_handler(hand);
        ciudades[i].ObtenerPrimero().incr_ganancia(i);
    }

    Heap<Ciudad> heap = new Heap<Ciudad>(ciudades, SuperavitComparator);
    System.out.println("lol");
    Ciudad gotica = new Ciudad(9);
    gotica.incr_ganancia(10);
    Handler handGot = new Handler(null);
    Tupla<Ciudad,Handler> tuplaGotica = new Tupla<Ciudad,Handler>(gotica, handGot);
    heap.Añadir(tuplaGotica);
    tuplaGotica.ObtenerPrimero().incr_perdida(9);
    heap.Reordenar(tuplaGotica.ObtenerSegundo().ref(), false);
    // System.out.println(heap2.sacar(0));
    // Ciudad city = new Ciudad(8);
    // city.incr_ganancia(10);
    // heap2.Agregar(city);
    // city.incr_perdida(11);
    // city.setear_ref(0);
    // heap2.reordenar(city.ref(), -1);
}
}
