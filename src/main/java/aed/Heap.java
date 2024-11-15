package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap <T> {
    private ArrayList<T> elementos;
    private int tamaño;
    private Comparator<T> comparador;

// E = atributo elementos

public Heap(T[] arreglo, Comparator<T> comparador){   //COMPLEJIDAD DE LA FUNCION: O(log(E))*O(|E|)
    elementos = new ArrayList<T>();
    for (T t : arreglo) {   
        elementos.add(t);   // O(1)                          
        tamaño += 1;        // O(1)
    }
    this.comparador = comparador;
    heapify();  //O(log(E))*O(|E|)
}

public T obtenerElem(int indice){     //COMPLEJIDAD DE LA FUNCION:  O(1)
    return elementos.get(indice);
}

public int tamaño(){   //COMPLEJIDAD DE LA FUNCION: O(1)
    return tamaño;
}

public T maximo(){     //COMPLEJIDAD DE LA FUNCION: O(1)
    return elementos.getFirst();    
}

public ArrayList<Tupla<T,Integer>> sacar(int pos){
    T elemASacar;
    Tupla<T,Integer> tupla = new Tupla<T,Integer>(null, null);
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();
    if (pos == tamaño - 1) {
        tamaño--;
        elemASacar = elementos.removeLast();
        tupla = new Tupla<T,Integer>(elemASacar, null);
        res.add(tupla);
    }else if (tamaño == 0) {
        return null;
    }else if (tamaño < 3) {
        elemASacar = elementos.remove(pos);
        tupla = new Tupla<T,Integer>(elemASacar, null);
        res.add(tupla);
        if(tamaño == 2){
            tupla = new Tupla<T,Integer>(elementos.get(0), 0);
            res.add(tupla);
        }
        tamaño--;
        return res;
    }else{
        tamaño--;
        elemASacar = elementos.get(pos);
        tupla = new Tupla<T,Integer>(elemASacar, null);
        res.add(tupla);
        elementos.set(pos, elementos.removeLast());
        while (pos < tamaño) {
            if ((2*pos)+2 < tamaño) {
                int nueva_pos = siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
                tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
                res.add(tupla);
                pos = nueva_pos == pos ? tamaño : nueva_pos;
            } else if ((2*pos)+1 < tamaño) {
                int nueva_pos = siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
                tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
                res.add(tupla);
                pos = nueva_pos == pos ? tamaño : nueva_pos;
            } else {
                tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
                res.add(tupla);
                pos = tamaño;
            }
        }
    }
    return res;
}

public ArrayList<Tupla<T,Integer>> Agregar(T nuevo){     //COMPLEJIDAD DE LA FUNCION: O(log(E))
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();   
    Tupla<T,Integer> tupla;
    elementos.add(nuevo);   //O(1)
    tamaño++;               //O(1)
    int pos = tamaño - 1;   //O(1)
    while ( pos > 0 && !esHeap(new int[]{(pos-1)/2,pos, tamaño})) {     
        siftUp(new int[]{(pos-1)/2,pos});                               // O(1)
        tupla = new Tupla<T,Integer>(elementos.get(pos), pos);          // O(1)   
        res.add(tupla);                                                 // O(1)    
        pos = (pos-1)/2;                                                // O(1)     
    }
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  // O(1)
    res.add(tupla); // O(1)
    return res; //O(1)
}

public boolean esHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1)  
    if(heap[2] >= tamaño){
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) >= 0; //O(1)
    }else{
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) >= 0 && comparador.compare(elementos.get(heap[0]), elementos.get(heap[2])) >= 0; //O(1)
    }
}

public void heapify(){     //COMPLEJIDAD DE LA FUNCION: O(|E|)
    int pos = tamaño - 1;   //O(1)
    if (tamaño > 2){        
        if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){   
            siftDown(new int[]{(pos-1)/2,pos-1,pos});  //O(1)     
            pos-=2;     //O(1)
        }else{
            siftDown(new int[]{(pos-1)/2,pos,tamaño}); //O(1)
            pos-=1; // O(1)
        }
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2,pos,pos-1})){ 
            int indice = siftDown(new int[]{(pos-1)/2,pos,pos-1}); //O(1)
            pos-=2; //O(1)
            while((indice*2 + 1) < tamaño && !esHeap(new int[]{indice,indice*2 + 1,indice*2 + 2})){  
                indice = siftDown(new int[]{indice,indice*2 + 1,indice*2 + 2}); //O(1)
            }
        }
    }else if(tamaño == 2){
        siftDown(new int[]{0,1,2}); //O(1)
    }
}

public int siftUp(int[] elems){        //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(elems[0]);  // O(1)
    T hijo = elementos.get(elems[1]);   // O(1)

    if (comparador.compare(padre, hijo) < 0) { 
        elementos.set(elems[0], hijo);  // O(1)
        elementos.set(elems[1], padre); // O(1)
        return elems[0];    // O(1)
    }else{
        return elems[1];    //O(1)
    }
}

public int siftDown(int[] elems){    //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(elems[0]); //O(1)
    if(elems[2] >= tamaño){ 
            if(comparador.compare(padre, elementos.get(elems[1])) < 0){ 
                elementos.set(elems[0], elementos.get(elems[1]));   // O(1)
                elementos.set(elems[1], padre); //O(1)
                return elems[1];    //O(1)
            }
    }else{
        int hijoMayor = comparador.compare(elementos.get(elems[1]), elementos.get(elems[2])) > 0 ? elems[1] : elems[2]; //O(1) 
        if(comparador.compare(padre, elementos.get(hijoMayor)) < 0){   
            elementos.set(elems[0], elementos.get(hijoMayor));  //O(1)
            elementos.set(hijoMayor, padre);    //O(1)
            return hijoMayor;   //  O(1)
        }
    }
    return elems[0];
}

public ArrayList<Tupla<T,Integer>> reordenar(int id, int dir){  //COMPLEJIDAD DE LA FUNCION: O(log(E))
    int pos = id;     // O(1)
    Tupla<T,Integer> tupla;
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();   //O(1)
    if(dir == 0){
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2, pos,tamaño})){    
            siftUp(new int[]{(pos-1)/2, pos});  //O(1)
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //O(1)
            res.add(tupla); //O(1)
            pos = (pos-1)/2;    //O(1)
        }
    }else{
        while(pos*2+1 < tamaño && !esHeap(new int[]{pos, pos*2+1, pos*2+2})){
            int i = siftDown(new int[]{pos, pos*2+1, pos*2+2}); //O(1)
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //O(1)
            res.add(tupla); //O(1)
            pos = i;    //O(1)
        }
    }
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //O(1)
    res.add(tupla); //O(1)
    return res; //O(1)
}

public String toString(){     //COMPLEJIDAD DE LA FUNCION: O(|E|) 
    String res = "[";
    for (T t : elementos) {     //O(|E|)
        res += t.toString() + ",";  // O(1)
    }  
    return res + "]";   //O(1)
}


public static void main(String[] args) {
    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    
    Ciudad[] ciudades = new Ciudad[8];
    for (int i = 0; i < ciudades.length; i++) {
        ciudades[i] = new Ciudad(i);
        ciudades[i].incr_ganancia(i);
    }

    Heap<Ciudad> heap2 = new Heap<Ciudad>(ciudades, SuperavitComparator);
    System.out.println(heap2.sacar(0));
    Ciudad city = new Ciudad(8);
    city.incr_ganancia(10);
    heap2.Agregar(city);
    city.incr_perdida(11);
    city.setear_ref(0);
    heap2.reordenar(city.ref(), -1);
}
}
