package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap <T> {
    private ArrayList<Tupla<T,Handler>> elementos;
    private int tamaño;
    private Comparator<T> comparador;

// n = |elementos|

public Heap(Tupla<T,Handler>[] arreglo, Comparator<T> comparador){   //COMPLEJIDAD DE LA FUNCION: O(n)
    elementos = new ArrayList<Tupla<T,Handler>>(); // 2
    //Transformamos el arreglo en un arraylist.
    for (Tupla<T,Handler> t : arreglo) {       // 3, n iteraciones
        elementos.add(t);                      // 1                           
        tamaño += 1;                           // 1
    } // t1 = 3 + n(2 + 3)
    this.comparador = comparador; // 1
    //Le asignamos a cada objeto su referencia antes de empezar a moverlos.
    for (int i = 0; i < elementos.size(); i++) { // 2, n iteraciones
        elementos.get(i).ObtenerSegundo().set_ref(i);   // 3
    } // t2 = 2 + n(2 + 3)
    Heapify(); // O(n)
} //tF = 3 + t1 + t2 = 3 + 3 + 5n + 2 + 5n = 8 + 10n --> O(n)

public ArrayList<T> elementos(){ //COMPLEJIDAD DE LA FUNCION: O(n)
    ArrayList<T> res = new ArrayList<T>(); // 2
    for (Tupla<T,Handler> t : elementos) { // 3, n iteraciones
        res.add(t.ObtenerPrimero()); // 2
    } // t = 3 + n(2+3)
    return res; // 1
} //tF = 3 + t = 6 + 5n ---> O(n)

public T obtenerElem(int indice){     //COMPLEJIDAD DE LA FUNCION:  O(1)
    return elementos.get(indice).ObtenerPrimero();
}

public int tamaño(){   //COMPLEJIDAD DE LA FUNCION: O(1)
    return tamaño;
}

public T maximo(){     //COMPLEJIDAD DE LA FUNCION: O(1)
    return elementos.get(0).ObtenerPrimero();    
}

public void Heapify(){  // COMPLEJIDAD DE LA FUNCION: O(n). -- Algoritmo de Floyd --
    int pos = (int)(Math.pow(2, log2(tamaño)))-2; // Setea la posición actual en la del ultimo nodo del penúltimo nivel. (2**(log(n)-1) - 2).
    if(tamaño>0){
        while (pos >= 0) { // Bucle que recorre todos los elementos por nivel de manera ascendente.
            SiftDown(pos);
            pos-=1;
        }
    }
}

public void Añadir(Tupla<T,Handler> elem){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    elementos.add(elem); // 1
    tamaño++; // 1
    if (tamaño>1) { // 1
        SiftUp(tamaño-1); // log(n)
    }else{
        elem.ObtenerSegundo().set_ref(0); // 2
    } // t = 1 + log(n) 
}   // tf = 2 + t = 3 + log(n) ---> O(log(n))

public T Sacar(int pos){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    T res = null;   //1
    if(tamaño == 1 || pos == tamaño-1){ //3
        res = elementos.remove(pos).ObtenerPrimero(); //3
        tamaño --; //1
    }else if( tamaño > 1){ // 1
        res = elementos.get(pos).ObtenerPrimero(); //3
        elementos.set(pos, elementos.remove(tamaño-1)); //2
        tamaño --; //1
        SiftDown(pos); // log(n)
    // t1 = 4 + 6 + log(n) = 10 + log(n)
    }
    return res; //1     
} //tF = 2 + t1 = 11 + log(n) ---> O(log(n))

//Acomoda el heap a partir de la posición del objeto que modificó su valor externamente.
public void Reordenar(int pos, boolean subio){ //COMPLEJIDAD DE LA FUNCION: O(log(n)). 
    if (subio){ //Si el valor del objeto aumentó, entonces puede haber superado el valor de su padre y hay que hacer siftUp. t = 1
        SiftUp(pos); // log(n)
    }else{ //Si el valor del objeto disminuyó, entonces alguno de sus hijos puede haber superado su valor y hay que hacer siftDown.
        SiftDown(pos); // log(n)
    } 
} //tF = 1 + log(n) ---> O(log(n))

public boolean EsHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1). Recibe como parámetro una lista de las posiciones de un sub-heap {padre, hijo izquierdo, hijo derecho}.
    T padre = elementos.get(heap[0]).ObtenerPrimero(); // 4
    if(heap[2] >= tamaño && heap[1] < tamaño){ //Si la posición del hijo derecho está fuera del tamaño del heap pero la del izquierdo no, entonces el padre tiene sólo hijo izq. t=5
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero(); // 4
        return comparador.compare(padre, hijo_izq) >= 0; // 3 
    }else if(heap[1] < tamaño){ //El sub-heap está completo. t=2
        T hijo_izq = elementos.get(heap[1]).ObtenerPrimero(); // 4
        T hijo_der = elementos.get(heap[2]).ObtenerPrimero(); // 4 
        return comparador.compare(padre, hijo_izq) >= 0 && comparador.compare(padre, hijo_der) >= 0; // 6
    } 
    return true; //1    
} // tF = 4 + 5 + 2 + 14 ---> O(1)

public Integer log2(int n){ //COMPLEJIDAD DE LA FUNCION: O(1)
    return (int)Math.floor(Math.log(n)/Math.log(2)); 
}


public void SiftUp(int pos){    //COMPLEJIDAD DE LA FUNCION: O(log(n))
    Tupla<T,Handler> padre = elementos.get((pos-1)/2);  // 3
    Tupla<T,Handler> hijo = elementos.get(pos);   // 2
    if(pos > 0 && !EsHeap(new int[]{(pos-1)/2,pos, tamaño})){ //Le pasa tamaño como la posición del hijo izquierdo para que sea ignorado. t=5
        elementos.set(pos, padre);  // 1
        elementos.set((pos-1)/2, hijo); // 2
        padre.ObtenerSegundo().set_ref(pos); // 2
        SiftUp((pos-1)/2); // 2
    }else{
        hijo.ObtenerSegundo().set_ref(pos); // 2
    } // t  = 5 + 7 = 12
    // tF = 5 + t = 17 , Como se trata de una funcion recursiva, la cual en el peor de los casos
    //                   hace log(n) llamados asi misma (longitud de una rama del heap pensado como arbol)
    //                   entonces la complejidad es tF*log(n) = 17*log(n) --> O(log(n))
}

public void SiftDown(int pos){ //COMPLEJIDAD DE LA FUNCION: O(log(n))
    Tupla<T,Handler> t_padre = elementos.get(pos); // 2
    if(!EsHeap(new int[]{pos, pos*2+1,pos*2+2})){ // 6
        if(pos*2 + 2 >= tamaño && pos*2+1 < tamaño){ //Caso base: El último padre solo tiene un hijo. t=7
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1); // 4 
            elementos.set(pos*2+1, t_padre); // 3
            elementos.set(pos,t_hijo_izq); // 1
            t_padre.ObtenerSegundo().set_ref(pos*2+1); // 4
            t_hijo_izq.ObtenerSegundo().set_ref(pos); //Cambia ambas referencias ya que estamos al final de la recursión. t=2
        }else if(pos*2 + 2 < tamaño){ // Paso Recursivo: La hoja en pos tiene dos hijos. t=3
            Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1); // 4
            Tupla<T,Handler> t_hijo_der = elementos.get(pos*2+2); // 4
            Integer pos_hijo_mayor = comparador.compare(t_hijo_izq.ObtenerPrimero(), t_hijo_der.ObtenerPrimero()) > 0 ? pos*2+1 : pos*2+2; // 10
            elementos.set(pos, elementos.get(pos_hijo_mayor)); // 2
            elementos.get(pos).ObtenerSegundo().set_ref(pos); //Cambia la referencia del hijo por el cual hizo el cambio, ya que no volverá a moverse. t=3
            elementos.set(pos_hijo_mayor, t_padre); // 1
            SiftDown(pos_hijo_mayor); //Recursión. t=1
        }//t1 = 10 + 25
    }else{// Fin : El heap completo cumple el invRep.
        t_padre.ObtenerSegundo().set_ref(pos); // 2
    // t2 = 6 + 35
    }
}   // tF = 2 + t2 = 43 --> O(1)    Como se trata de una funcion recursiva, la cual en el peor de los casos
//                                  hace log(n) llamados a si misma (longitud de una rama del heap pensado como arbol)
//                                  entonces la complejidad es tF*log(n) = 43*log(n) --> O(log(n))

public String toString(){
    String res = "[";
    for (int i = 0; i < elementos.size() - 1 ; i++) {     
        res += elementos.get(i).ObtenerPrimero().toString() + ","; 
    }
    return res + elementos.get(tamaño-1) + "]";   
}
}
