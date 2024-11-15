package aed;

import java.util.ArrayList;
import java.util.Comparator;

public class Heap <T> {
    private ArrayList<T> elementos;
    private int tamaño;
    private Comparator<T> comparador;

// n = |elementos|

public Heap(T[] arreglo, Comparator<T> comparador){   //COMPLEJIDAD DE LA FUNCION: O(log(n)*n)
                                    // n = |arreglo|, |arreglo| va a ser igual a |elementos|
    elementos = new ArrayList<T>(); //1
    for (T t : arreglo) {       // 3, n iteraciones
        elementos.add(t);   // 1                          
        tamaño += 1;        // 1
    }                       // t = 3 + n(2 + 3)
    this.comparador = comparador; // 1
    heapify();  //O(log(n)*n) 
                //      tF = 5 + n(5) + log(n)n --> O(log(n)*n)
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

public ArrayList<Tupla<T,Integer>> Agregar(T nuevo){     //COMPLEJIDAD DE LA FUNCION: O(log(n))
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>(); //1
    Tupla<T,Integer> tupla; //1
    elementos.add(nuevo);   //1
    tamaño++;               //1
    int pos = tamaño - 1;   //1
    while ( pos > 0 && !esHeap(new int[]{(pos-1)/2,pos, tamaño})) {     // 5, log(n) iteraciones
        siftUp(new int[]{(pos-1)/2,pos});                               // 2
        tupla = new Tupla<T,Integer>(elementos.get(pos), pos);          // 3   
        res.add(tupla);                                                 // 1    
        pos = (pos-1)/2;                                                // 2     
    }                                                                   // t = 5 + log(n)(8+5)
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  // 1
    res.add(tupla); // 1
    return res; //1         tF = 7 + 5 + log(n)13 = 12 + log(n)13 --> O(log(n))
}       

public boolean esHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1)  
    if(heap[2] >= tamaño){ // 2
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) >= 0; //1
    }else{
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) >= 0 && comparador.compare(elementos.get(heap[0]), elementos.get(heap[2])) >= 0; //1
    }   // tF = 2 + 1 = 3 --> O(1)
}

public void heapify(){     //COMPLEJIDAD DE LA FUNCION: O(n)
    int pos = tamaño - 1;   //2
    if (tamaño > 2){   //1
        if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){   //5
            siftDown(new int[]{(pos-1)/2,pos-1,pos});  //2   
            pos-=2;     //1
        }else{
            siftDown(new int[]{(pos-1)/2,pos,tamaño}); //2
            pos-=1; // 1
        }                   // t1 =  5 + 3 = 8 
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2,pos,pos-1})){ // 6, n/2 iteraciones
            int indice = siftDown(new int[]{(pos-1)/2,pos,pos-1}); //4
            pos-=2; //1
            while((indice*2 + 1) < tamaño && !esHeap(new int[]{indice,indice*2 + 1,indice*2 + 2})){ // 10, log(n) iteraciones
                indice = siftDown(new int[]{indice,indice*2 + 1,indice*2 + 2}); //1
            }       //t3 = 10 + log(n)(1 + 10) = 10 + log(n)11 
            // t2 = 6 + (n/2)((t3 + 5) + 6) = 6 + (n/2)(21 + log(n)11) 
        }
    }else if(tamaño == 2){ 
        siftDown(new int[]{0,1,2}); //2
        // t4 = 1 + (t1 + t2) 

    }   // tF = 2 + t4  --> O((n/2)*log(n)) --> VER QUE HACEMOS CON ESTO 
}

public int siftUp(int[] elems){        //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(elems[0]);  // 2
    T hijo = elementos.get(elems[1]);   // 2

    if (comparador.compare(padre, hijo) < 0) { // 1 
        elementos.set(elems[0], hijo);  // 2
        elementos.set(elems[1], padre); // 2
        return elems[0];    // 2
    }else{
        return elems[1];    //2
    } // t = 1 + 6 = 7 
        // tF = 2 + 7 = 9 --> O(1) 
}

public int siftDown(int[] elems){    //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(elems[0]); //2
    if(elems[2] >= tamaño){ // 2 
            if(comparador.compare(padre, elementos.get(elems[1])) < 0){ // 2
                elementos.set(elems[0], elementos.get(elems[1]));   // 4
                elementos.set(elems[1], padre); //2
                return elems[1];    //2
            }
    }else{
        int hijoMayor = comparador.compare(elementos.get(elems[1]), elementos.get(elems[2])) > 0 ? elems[1] : elems[2]; // 6
        if(comparador.compare(padre, elementos.get(hijoMayor)) < 0){    //3
            elementos.set(elems[0], elementos.get(hijoMayor));  //1
            elementos.set(hijoMayor, padre);    //1
            return hijoMayor;   //  1
        }  
        // t = 2 + 12 = 14
    }
    return elems[0]; //1     tF = 3 + 14 = 17 --> O(1)


}

public ArrayList<Tupla<T,Integer>> reordenar(int id, int dir){  //COMPLEJIDAD DE LA FUNCION: O(log(n))
    int pos = id;     // 1
    Tupla<T,Integer> tupla; //1
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();   //1
    if(dir == 0){ //1
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2, pos,tamaño})){   // 5, log(n) iteraciones
            siftUp(new int[]{(pos-1)/2, pos});  //2
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //3
            res.add(tupla); //1
            pos = (pos-1)/2;    //3
        }       // t1 = 5 + log(n)(9 + 5) = 5 + log(n)14
    }else{
        while(pos*2+1 < tamaño && !esHeap(new int[]{pos, pos*2+1, pos*2+2})){ //10, log(n) iteraciones
            int i = siftDown(new int[]{pos, pos*2+1, pos*2+2}); //7
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //3
            res.add(tupla); //1
            pos = i;    //1
        }       // t2 = 10 + log(n)(12 + 10) = 10 + log(n)22
    } // t3 = 1  + (10 + log(n)22)
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //2
    res.add(tupla); //1
    return res; //1 
    // tF = 6 + 1 + 10 + log(n)22 = 17 + log(n)22 --> O(log(n))
}

public String toString(){   
    String res = "[";
    for (T t : elementos) {     
        res += t.toString() + ","; 
    }  
    return res + "]";   
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
