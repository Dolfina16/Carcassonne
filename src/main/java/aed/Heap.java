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

public static int log2(int N){
    int result = (int)(Math.log(N) / Math.log(2));

    return result;
}

public T maximus(){
    return elementos.getFirst();
}

public T sacar(int pos){
    T hashirama;
    if (tamaño == 0) {
        return null;
    }else if (tamaño < 3) {
        hashirama = elementos.remove(pos);
        tamaño--;
        return hashirama;
    }else{
        tamaño--;
        hashirama = elementos.get(pos);
        elementos.set(pos, elementos.removeLast());
        while (pos <= (Math.pow(2, Math.floor(log2(tamaño))) - 2)) {
            if ((2*pos)+2 < tamaño) {
                T elemMovedizo = elementos.get(pos);
                siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
                if (elementos.get(pos).equals(elemMovedizo)) {
                    //no me movi
                    pos = tamaño;
                }else{
                    pos = elementos.get((2*pos)+1).equals(elemMovedizo) ? (2*pos)+1 : (2*pos)+2;
                }
            } else if ((2*pos)+1 < tamaño) {
                T elemMovedizo = elementos.get(pos);
                siftDown(new int[]{pos,(2*pos)+1});
                pos = elementos.get(pos).equals(elemMovedizo) ? tamaño : (2*pos)+1;
            } else {
                pos = tamaño;
            }
        }
    }
    return hashirama;
}

public void Anhadir(T nuevo){
    elementos.add(nuevo);
    tamaño++;
    int pos = tamaño - 1;
    while ( pos > 0 || ((pos-1)/2) >= 0 ) {
        T viejo = elementos.get(pos);
        siftUp(new int[]{(pos-1)/2,pos});
        if (elementos.get(pos).equals(viejo)) {
            pos = 0;
        }
        pos = (pos-1)/2;
    }
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
    if (tamaño > 2 && pos > 1){
        while(pos > 0){
            int indice = pos;
            int indiceDelQueCambio;
            if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){
                indiceDelQueCambio = siftDown(new int[]{(pos-1)/2,pos-1,pos});
                if (indiceDelQueCambio != (pos-1)/2) {
                    indice = indiceDelQueCambio;
                }
                pos -= 2;
            }else{
                indiceDelQueCambio = siftDown(new int[]{(pos-1)/2,pos});
                if (indiceDelQueCambio != (pos-1)/2) {
                    indice = indiceDelQueCambio;
                }
                pos -=1;
            }
            while((indice*2 + 1) < tamaño && !esHeap(new int[]{indice,indice*2 + 1,indice*2 + 2})){
                if(indice*2 + 2 < tamaño){
                    indice = siftDown(new int[]{indice,indice*2 + 1,indice*2 + 2});
                }else{
                    indice = siftDown(new int[]{indice,indice*2 + 1});
                }
            }
        }
    }else if(tamaño == 2){
        siftDown(new int[]{0,1});
    }
}

public void siftUp(int[] elems){
    T vader = elementos.get(elems[0]);
    T luke = elementos.get(elems[1]);

    if (comparador.compare(vader, luke) < 0) {
        elementos.set(elems[0], luke);
        elementos.set(elems[1], vader);
    }
}

public int siftDown(int[] elems){
    T padre = elementos.get(elems[0]);
    if(elems.length == 2){
            if(comparador.compare(padre, elementos.get(elems[1])) < 0){
                elementos.set(elems[0], elementos.get(elems[1]));
                elementos.set(elems[1], padre);
                return elems[1];
            }
    }else if(elems.length == 3){
        int hijoMayor = comparador.compare(elementos.get(elems[1]), elementos.get(elems[2])) > 0 ? elems[1] : elems[2];
        if(comparador.compare(padre, elementos.get(hijoMayor)) < 0){
            elementos.set(elems[0], elementos.get(hijoMayor));
            elementos.set(hijoMayor, padre);
            return hijoMayor;
        }
    }
    return elems[0];
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
        ciudades[i].incr_ganancia(i + 1);
    }
    Heap<Ciudad> heap2 = new Heap<Ciudad>(ciudades, SuperavitComparator);
    System.out.println(heap2.sacar(3));
}

}
