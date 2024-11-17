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
    elementos = new ArrayList<Tupla<T,Handler>>(); //2
    for (Tupla<T,Handler> t : arreglo) {       // 3, n iteraciones
        elementos.add(t); // 1                           
        tamaño += 1;        // 1
    }                       // t1 = 3 + n(2 + 3)
    this.comparador = comparador; // 1
    for (int i = 0; i < elementos.size(); i++) { // 2, n iteraciones
        elementos.get(i).ObtenerSegundo().set_ref(i);   //3
    }                                                   // t2 = 2 + n(2 + 3)
    Heapify();  //O(n)       tF = 3 + t1 + t2 = 3 + 3 + n*5 + 2 + n*5 = 8 + 10n  --> O(n)
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

public void Heapify(){  // COMPLEJIDAD DE LA FUNCION: O(n) --> La complejidad del algoritmo es O(n) debido a que utilizamos el algoritmo de Floyd
    int pos = (int)(Math.pow(2, log2(tamaño)))-2;
    if(tamaño>0){
        while (pos >= 0) {
            SiftDown(pos);
            pos-=1;
        }
    }
}

public void Añadir(Tupla<T,Handler> elem){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    elementos.addLast(elem); //1
    tamaño++; //1
    if (tamaño>1) { //1
        SiftUp(tamaño-1); //log(n)
    }else{
        elem.ObtenerSegundo().set_ref(0); //2
    } // t = 1 + log(n) 
}   // tf = 2 + t = 3 + log(n) --> O(log(n))

public T Sacar(int pos){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    T res = null;   //1
    if(tamaño == 1 || pos == tamaño-1){ //4
        res = elementos.remove(pos).ObtenerPrimero(); //3
        tamaño --; //1
    }else if( tamaño > 1){
        res = elementos.get(pos).ObtenerPrimero(); //3
        elementos.set(pos, elementos.removeLast()); //2
        tamaño --; //1
        SiftDown(pos); // log(n)
    // t1 = 4 + 6 + log(n) = 10 + log(n)
    }
    return res; //1     tF = 2 + t1 = 12 + log(n) --> O(log(n))
}

public void Reordenar(int pos, boolean subio){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    if (subio){ //1
        SiftUp(pos); //log(n)
    }else{
        SiftDown(pos); //log(n)
    } //tF = 1 + log(n) --> O(log(n))
}

public boolean EsHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(heap[0]).ObtenerPrimero(); // 4
    if(heap[2] >= tamaño && heap[1] < tamaño){ // 5
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero(); //4
        return comparador.compare(padre, hijo_izq) >= 0; //2 
    }else if(heap[1] < tamaño){
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero(); //4
        T hijo_der = elementos.get(heap[2]).ObtenerPrimero(); //4 
        return comparador.compare(padre, hijo_izq) >= 0 && comparador.compare(padre, hijo_der) >= 0; //4
    }   // t = 5 + 12
    return true; //1    tF = 4 + t + 1  = 22 --> O(1)
}

public Integer log2(int n){ //COMPLEJIDAD DE LA FUNCION: O(1)
    return (int)Math.floor(Math.log(n)/Math.log(2)); 
}


public void SiftUp(int pos){    //COMPLEJIDAD DE LA FUNCION: O(log(n))
    Tupla<T,Handler> padre = elementos.get((pos-1)/2);  // 3
    Tupla<T,Handler> hijo = elementos.get(pos);   // 2
    if(pos > 0 && !EsHeap(new int[]{(pos-1)/2,pos, tamaño})){ // 5
        elementos.set(pos, padre);  // 1
        elementos.set((pos-1)/2, hijo); // 2
        padre.ObtenerSegundo().set_ref(pos); // 2
        SiftUp((pos-1)/2); // 1
    }else{
        hijo.ObtenerSegundo().set_ref(pos); //2
    } // t  = 5 + 6 = 11
    // tF = 5 + t = 16 , Como se trata de una funcion recursiva, la cual en el peor de los casos
    //                   hace log(n) llamados asi misma (longitud de una rama del heap pensado como arbol)
    //                   entonces la complejidad es tF*log(n) = 16*log(n) --> O(log(n))
}

public void SiftDown(int pos){ //COMPLEJIDAD DE LA FUNCION: O(log(n)) 
    Tupla<T,Handler> t_padre = elementos.get(pos); //2
    if(!EsHeap(new int[]{pos, pos*2+1,pos*2+2})){ //6
        if(pos*2 + 2 >= tamaño && pos*2+1 < tamaño){ // 7
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1); //4 
            elementos.set(pos*2+1, t_padre); //3
            t_padre.ObtenerSegundo().set_ref(pos*2+1); //4
            elementos.set(pos,t_hijo_izq); // 1
            t_hijo_izq.ObtenerSegundo().set_ref(pos); //2
        }else if(pos*2 + 2 < tamaño){
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1); //4 
            T hijo_izq = t_hijo_izq.ObtenerPrimero(); //2
            Tupla<T,Handler> t_hijo_der = elementos.get(pos*2+2); //4
            T hijo_der = t_hijo_der.ObtenerPrimero(); //2
            Integer pos_hijo_mayor = comparador.compare(hijo_izq, hijo_der) > 0 ? pos*2+1 : pos*2+2; //7
            elementos.set(pos, elementos.get(pos_hijo_mayor)); //2
            elementos.get(pos).ObtenerSegundo().set_ref(pos); //3
            elementos.set(pos_hijo_mayor, t_padre); //1
            SiftDown(pos_hijo_mayor); //1
            //t1 = 7 + 26 
        }
    }else{
        t_padre.ObtenerSegundo().set_ref(pos); //2
    // t2 = 6 + t1 = 6 + 33 = 39 
    }
}   // tF = 2 + t2 = 41 --> O(1)    Como se trata de una funcion recursiva, la cual en el peor de los casos
//                                  hace log(n) llamados asi misma (longitud de una rama del heap pensado como arbol)
//                                  entonces la complejidad es tF*log(n) = 41*log(n) --> O(log(n))


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
