package aed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Heap <T> {
    private ArrayList<T> elementos;
    private int tamaño;
    private Comparator<T> comparador;

public Heap(T[] arreglo, Comparator<T> comparador){
    elementos = new ArrayList<T>();
    for (T t : arreglo) {
        elementos.add(t);
        tamaño += 1;
    }
    this.comparador = comparador;
    heapify();
}

public T get_elem(int indice){
    return elementos.get(indice);
}

public int tamaño(){
    return tamaño;
}

public static int log2(int N){
    int result = (int)(Math.log(N) / Math.log(2));

    return result;
}

public T maximus(){
    return elementos.getFirst();
}

public ArrayList<Tupla<T,Integer>> sacar(int pos){
    T hashirama;
    Tupla<T,Integer> tupla = new Tupla<T,Integer>(null, null);
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();
    if (tamaño == 0) {
        return null;
    }else if (tamaño < 3) {
        hashirama = elementos.remove(pos);
        tamaño--;
        tupla = new Tupla<T,Integer>(hashirama, null);
        res.add(tupla);
        if(tamaño == 2){
            tupla = new Tupla<T,Integer>(elementos.get(0), 0);
            res.add(tupla);
        }
        return res;
    }else{
        tamaño--;
        hashirama = elementos.get(pos);
        tupla = new Tupla<T,Integer>(hashirama, null);
        res.add(tupla);
        elementos.set(pos, elementos.removeLast());
        while (pos < tamaño) {
            if ((2*pos)+2 < tamaño) {
                int nueva_pos = siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
                tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
                res.add(tupla);
                pos = nueva_pos == pos ? tamaño : nueva_pos;
            } else if ((2*pos)+1 < tamaño) {
                int nueva_pos = siftDown(new int[]{pos,(2*pos)+1});
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

public ArrayList<Tupla<T,Integer>> Anhadir(T nuevo){
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();
    Tupla<T,Integer> tupla;
    elementos.add(nuevo);
    tamaño++;
    int pos = tamaño - 1;
    while ( pos > 0 && !esHeap(new int[]{(pos-1)/2,pos, tamaño})) {
        siftUp(new int[]{(pos-1)/2,pos});
        tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
        res.add(tupla);
        pos = (pos-1)/2;
    }
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
    res.add(tupla);
    return res;
}

public boolean esHeap(int[] heap){
    if(heap[2] >= tamaño){
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) > 0;
    }else{
        return comparador.compare(elementos.get(heap[0]), elementos.get(heap[1])) > 0 && comparador.compare(elementos.get(heap[0]), elementos.get(heap[2])) > 0;
    }
}

public void heapify(){
    int pos = tamaño - 1;
    if (tamaño > 2){
        if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){
            siftDown(new int[]{(pos-1)/2,pos-1,pos});
            pos-=2;
        }else{
            siftDown(new int[]{(pos-1)/2,pos,tamaño});
            pos-=1;
        }
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2,pos,pos-1})){
            int indice = siftDown(new int[]{(pos-1)/2,pos,pos-1});
            pos-=2;
            while((indice*2 + 1) < tamaño && !esHeap(new int[]{indice,indice*2 + 1,indice*2 + 2})){
                indice = siftDown(new int[]{indice,indice*2 + 1,indice*2 + 2});
            }
        }
    }else if(tamaño == 2){
        siftDown(new int[]{0,1,2});
    }
}

public int siftUp(int[] elems){
    T vader = elementos.get(elems[0]);
    T luke = elementos.get(elems[1]);

    if (comparador.compare(vader, luke) < 0) {
        elementos.set(elems[0], luke);
        elementos.set(elems[1], vader);
        return elems[0];
    }else{
        return elems[1];
    }
}

public int siftDown(int[] elems){
    T padre = elementos.get(elems[0]);
    if(elems[2] >= tamaño){
            if(comparador.compare(padre, elementos.get(elems[1])) < 0){
                elementos.set(elems[0], elementos.get(elems[1]));
                elementos.set(elems[1], padre);
                return elems[1];
            }
    }else{
        int hijoMayor = comparador.compare(elementos.get(elems[1]), elementos.get(elems[2])) > 0 ? elems[1] : elems[2];
        if(comparador.compare(padre, elementos.get(hijoMayor)) < 0){
            elementos.set(elems[0], elementos.get(hijoMayor));
            elementos.set(hijoMayor, padre);
            return hijoMayor;
        }
    }
    return elems[0];
}

public ArrayList<Tupla<T,Integer>> reordenar(int id, int dir){
    int pos = id;
    Tupla<T,Integer> tupla;
    ArrayList<Tupla<T,Integer>> res = new ArrayList<Tupla<T,Integer>>();
    if(dir == 0){
        while(pos > 0 && !esHeap(new int[]{(pos-1)/2, pos,tamaño})){
            siftUp(new int[]{(pos-1)/2, pos});
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
            res.add(tupla);
            pos = (pos-1)/2;
        }
    }else{
        while(pos*2+1 < tamaño && !esHeap(new int[]{pos, pos*2+1, pos*2+2})){
            int i = siftDown(new int[]{pos, pos*2+1, pos*2+2});
            tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
            res.add(tupla);
            pos = i;
        }
    }
    tupla = new Tupla<T,Integer>(elementos.get(pos), pos);
    res.add(tupla);
    return res;
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
    //Ciudad ciudad0 = new Ciudad(0);
    //Ciudad ciudad1 = new Ciudad(1);
    //Ciudad ciudad2 = new Ciudad(2);
    //ciudad0.incr_ganancia(200); //200 super
    //ciudad1.incr_ganancia(10); //10 super
    //ciudad2.incr_perdida(400); //-400 super
    //Heap<Ciudad> heap = new Heap<Ciudad>(new Ciudad[]{ciudad2,ciudad1,ciudad0}, SuperavitComparator);
    //Ciudad gotica = new Ciudad(3);
    //gotica.incr_perdida(-3000);
    // System.out.println(heap.sacarMaximo());
    //heap.Anhadir(gotica);

    // Random rand = new Random();
    // Generate random integers in range 0 to 100 rand.nextInt(101)
    Ciudad[] ciudades = new Ciudad[8];
    for (int i = 0; i < ciudades.length; i++) {
        ciudades[i] = new Ciudad(i);
        ciudades[i].incr_ganancia(i);
    }

    Heap<Ciudad> heap2 = new Heap<Ciudad>(ciudades, SuperavitComparator);
    System.out.println(heap2.sacar(0));
    Ciudad city = new Ciudad(8);
    city.incr_ganancia(10);
    heap2.Anhadir(city);
    city.incr_perdida(11);
    city.set_ref(0);
    heap2.reordenar(city.ref(), -1);
}
}
