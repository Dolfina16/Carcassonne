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
    heapify();  //O(n)
    int i = 0;
    for (Tupla<T,Handler> t : elementos) {
        t.ObtenerSegundo().set_ref(i);
        i++;
    }            //      tF = 5 + n(5) + n --> O(n)
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

// public ArrayList<Tupla<T,Integer>> sacar(int pos){
//     T elemASacar;
//     Tupla<T,Integer> tupla = new Tupla<T,Integer>(null, null);
//     ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();
//     if (pos == tamaño - 1) {
//         tamaño--;
//         elemASacar = elementos.remove(0).ObtenerPrimero();
//         tupla = new Tupla<T,Integer>(elemASacar, null);
//         res.add(tupla);
//     }else if (tamaño == 0) {
//         return null;
//     }else if (tamaño < 3) {
//         elemASacar = elementos.remove(pos).ObtenerPrimero();
//         tupla = new Tupla<T,Integer>(elemASacar, null);
//         res.add(tupla);
//         if(tamaño == 2){
//             tupla = new Tupla<T,Integer>(elementos.get(0), 0);
//             res.add(tupla);
//         }
//         tamaño--;
//         return res;
//     }else{
//         tamaño--;
//         elemASacar = elementos.get(pos);
//         tupla = new Tupla<T,Integer>(elemASacar, null);
//         res.add(tupla);
//         elementos.set(pos, elementos.remove(0));
//         while (pos < tamaño) {
//             if ((2*pos)+2 < tamaño) {
//                 int nueva_pos = siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
//                 tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
//                 res.add(tupla);
//                 pos = nueva_pos == pos ? tamaño : nueva_pos;
//             } else if ((2*pos)+1 < tamaño) {
//                 int nueva_pos = siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
//                 tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
//                 res.add(tupla);
//                 pos = nueva_pos == pos ? tamaño : nueva_pos;
//             } else {
//                 tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
//                 res.add(tupla);
//                 pos = tamaño;
//             }
//         }
//     }
//     return res;
// }

// public ArrayList<Tupla<T,Integer>> Agregar(T nuevo){     //COMPLEJIDAD DE LA FUNCION: O(log(n))
//     ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>(); //1
//     Tupla<T,Integer> tupla; //1
//     elementos.add(nuevo);   //1
//     tamaño++;               //1
//     int pos = tamaño - 1;   //1
//     while ( pos > 0 && !esHeap(new int[]{(pos-1)/2,pos, tamaño})) {     // 5, log(n) iteraciones
//         siftUp(new int[]{(pos-1)/2,pos});                               // 2
//         tupla = new Tupla<T,Integer>(elementos.get(pos), pos);          // 3   
//         res.add(tupla);                                                 // 1    
//         pos = (pos-1)/2;                                                // 2     
//     }                                                                   // t = 5 + log(n)(8+5)
//     tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  // 1
//     res.add(tupla); // 1
//     return res; //1         tF = 7 + 5 + log(n)13 = 12 + log(n)13 --> O(log(n))
// }       

public boolean esHeap(int[] heap){   //COMPLEJIDAD DE LA FUNCION: O(1)
    T padre = elementos.get(heap[0]).ObtenerPrimero();
    T hijo_izq = elementos.get(heap[0]).ObtenerPrimero();
    if(heap[2] >= tamaño){ // 2
        return comparador.compare(padre, hijo_izq) >= 0; //1
    }else{
        T hijo_der = elementos.get(heap[2]).ObtenerPrimero();
        return comparador.compare(padre, hijo_izq) >= 0 && comparador.compare(padre, hijo_der) >= 0; //1
    }   // tF = 2 + 1 = 3 --> O(1)
}

// public void heapify(){     //COMPLEJIDAD DE LA FUNCION: O(n)
//     int pos = tamaño - 1;   //2
//     if (tamaño > 2){   //1
//         if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){   //5
//             siftDown(new int[]{(pos-1)/2,pos-1,pos});  //2   
//             pos-=2;     //1
//         }else{
//             siftDown(new int[]{(pos-1)/2,pos,tamaño}); //2
//             pos-=1; // 1
//         }                   // t1 =  5 + 3 = 8
//         while(pos > 0 && !esHeap(new int[]{(pos-1)/2,pos,pos-1})){ // 6, n/2 iteraciones
//             int indice = siftDown(new int[]{(pos-1)/2,pos,pos-1}); //4
//             pos-=2; //1
//             while((indice*2 + 1) < tamaño && !esHeap(new int[]{indice,indice*2 + 1,indice*2 + 2})){ // 10, log(n) iteraciones
//                 indice = siftDown(new int[]{indice,indice*2 + 1,indice*2 + 2}); //1
//             }       //t3 = 10 + log(n)(1 + 10) = 10 + log(n)11 
//             // t2 = 6 + (n/2)((t3 + 5) + 6) = 6 + (n/2)(21 + log(n)11) 
//         }
//     }else if(tamaño == 2){ 
//         siftDown(new int[]{0,1,2}); //2
//         // t4 = 1 + (t1 + t2) 
//     }   // tF = 2 + t4  --> O((n/2)*log(n)) --> VER QUE HACEMOS CON ESTO 
// }

public Integer log2(int n){
    return (int)Math.ceil(Math.log(n)/Math.log(2)); 
}

public void heapify(){
    int pos = (int)(Math.pow(2, log2(tamaño)-1))-2;
}

public int siftUp(int[] elems){        //COMPLEJIDAD DE LA FUNCION: O(1)
    Tupla<T,Handler> padre = elementos.get(elems[0]);  // 2
    Tupla<T,Handler> hijo = elementos.get(elems[1]);   // 2

    if (comparador.compare(padre.ObtenerPrimero(), hijo.ObtenerPrimero()) < 0) { // 1 
        elementos.set(elems[0], hijo);  // 2
        elementos.set(elems[1], padre); // 2
        padre.ObtenerSegundo().set_ref(elems[1]);
        return elems[0];    // 2
    }else{
        return elems[1];    //2
    } // t = 1 + 6 = 7 
        // tF = 2 + 7 = 9 --> O(1) 
}

// public int siftDown(int[] elems){    //COMPLEJIDAD DE LA FUNCION: O(1)
//     T padre = elementos.get(elems[0]).ObtenerPrimero(); //2
//     Handler padreHandler = elementos.get(elems[0]).ObtenerSegundo();
//     Tupla<T,Handler> hijo_izq = elementos.get(elems[1]);

//     if(elems[2] >= tamaño){ // 2 
//             if(comparador.compare(padre, hijo_izq.ObtenerPrimero()) < 0){ // 2
//                 elementos.set(padreHandler.ref(), hijo_izq);   // 4
//                 hijo_izq.ObtenerSegundo().set_ref(elems[0]);
//                 elementos.set(elems[1], new Tupla<T,Handler>(padre,padreHandler)); //2
//                 return elems[1];    //2
//             }
//     }else{
//         Tupla<T,Handler> hijo_der = elementos.get(elems[2]);
//         Integer hijoMayor = comparador.compare(hijo_izq.ObtenerPrimero(), hijo_der.ObtenerPrimero()) > 0 ? elems[1] : elems[2]; // 6
//         if(comparador.compare(padre, elementos.get(hijoMayor).ObtenerPrimero()) < 0){    //3
//             elementos.set(elems[0], elementos.get(hijoMayor));  //1
//             elementos.set(elementos.get(hijoMayor).ObtenerSegundo().ref(), new Tupla<T,Handler>(padre,padreHandler));    //1
//             elementos.get(hijoMayor).ObtenerSegundo().set_ref(elems[0]);
//             return elems[hijoMayor];   //  1
//         }  
//         // t = 2 + 12 = 14
//     }
//     return elems[0]; //1     tF = 3 + 14 = 17 --> O(1)


// }

public void siftDown(int pos){
    Tupla<T,Handler> t_padre = elementos.get(pos);
    T padre = t_padre.ObtenerPrimero();
    if(pos*2 + 2 >= tamaño && pos*2+1 < tamaño){
        Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1);
        T hijo_izq = t_hijo_izq.ObtenerPrimero();
        if(comparador.compare(padre, hijo_izq)<0){
            elementos.set(pos*2+1, t_padre);
            t_padre.ObtenerSegundo().set_ref(pos*2+1);
            elementos.set(pos,t_hijo_izq);
            t_hijo_izq.ObtenerSegundo().set_ref(pos);
        }
    }else if(pos*2 + 2 < tamaño){
        Tupla<T,Handler> t_hijo_izq = elementos.get(pos*2+1);
        T hijo_izq = t_hijo_izq.ObtenerPrimero();
        Tupla<T,Handler> t_hijo_der = elementos.get(pos*2+1);
        T hijo_der = t_hijo_der.ObtenerPrimero();
        Integer ref_hijo_mayor = comparador.compare(hijo_izq, hijo_der) > 0 ? pos*2+1 : pos*2+2;
        if(comparador.compare(padre, elementos.get(ref_hijo_mayor).ObtenerPrimero()) < 0){
            elementos.set(ref_hijo_mayor, t_padre);
            elementos.set(pos, elementos.get(ref_hijo_mayor));
            elementos.get(ref_hijo_mayor).ObtenerSegundo().set_ref(pos);
            siftDown(ref_hijo_mayor);
        }
        t_padre.ObtenerSegundo().set_ref(pos);
    }
}

// public ArrayList<Tupla<T,Integer>> reordenar(int id, int dir){  //COMPLEJIDAD DE LA FUNCION: O(log(n))
//     int pos = id;     // 1
//     Tupla<T,Integer> tupla; //1
//     ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();   //1
//     if(dir == 0){ //1
//         while(pos > 0 && !esHeap(new int[]{(pos-1)/2, pos,tamaño})){   // 5, log(n) iteraciones
//             siftUp(new int[]{(pos-1)/2, pos});  //2
//             tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //3
//             res.add(tupla); //1
//             pos = (pos-1)/2;    //3
//         }       // t1 = 5 + log(n)(9 + 5) = 5 + log(n)14
//     }else{
//         while(pos*2+1 < tamaño && !esHeap(new int[]{pos, pos*2+1, pos*2+2})){ //10, log(n) iteraciones
//             int i = siftDown(new int[]{pos, pos*2+1, pos*2+2}); //7
//             tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //3
//             res.add(tupla); //1
//             pos = i;    //1
//         }       // t2 = 10 + log(n)(12 + 10) = 10 + log(n)22
//     } // t3 = 1  + (10 + log(n)22)
//     tupla = new Tupla<T,Integer>(elementos.get(pos), pos);  //2
//     res.add(tupla); //1
//     return res; //1 
//     // tF = 6 + 1 + 10 + log(n)22 = 17 + log(n)22 --> O(log(n))
// }

// public String toString(){   
//     String res = "[";
//     for (T t : elementos) {     
//         res += t.toString() + ","; 
//     }  
//     return res + "]";   
// }


public static void main(String[] args) {
    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    
    Ciudad[] ciudades = new Ciudad[8];
    for (int i = 0; i < ciudades.length; i++) {
        ciudades[i] = new Ciudad(i);
        ciudades[i].incr_ganancia(i);
    }

    // Heap<Ciudad> heap2 = new Heap<Ciudad>(ciudades, SuperavitComparator);
    // System.out.println(heap2.sacar(0));
    // Ciudad city = new Ciudad(8);
    // city.incr_ganancia(10);
    // heap2.Agregar(city);
    // city.incr_perdida(11);
    // city.setear_ref(0);
    // heap2.reordenar(city.ref(), -1);
}
}
