package aed;

import java.util.ArrayList;
import java.util.Comparator;

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
    heapify(tamaño-1);
}

public static int log2(int N){
    int result = (int)(Math.log(N) / Math.log(2));

    return result;
}

public T maximus(){
    return elementos.getFirst();
}

public T sacarMaximo(){
    T hashirama;
    if (tamaño == 0) {
        return null;
    }else if (tamaño < 3) {
        hashirama = elementos.removeFirst();
        tamaño--;
        return hashirama;
    }else{
        tamaño--;
        hashirama = elementos.getFirst();
        elementos.set(0, elementos.removeLast());
        int pos = 0;
        while (pos <= (Math.pow(2, Math.floor(log2(tamaño))) - 2)) {
            if ((2*pos)+2 < tamaño) {
                T elemMovedizo = elementos.get(pos);
                siftDown(new int[]{pos,(2*pos)+1,(2*pos)+2});
                if (elementos.get(pos).equals(elemMovedizo)) {
                    //no me movi
                    pos = tamaño;
                }else if (elementos.get((2*pos)+1).equals(elemMovedizo)) {
                    //me fui al hijo izq
                    pos = (2*pos)+1;
                }else{
                    //me fui al hijo der
                    pos = (2*pos)+2;
                }
            } else if ((2*pos)+1 < tamaño) {
                T elemMovedizo = elementos.get(pos);
                siftDown(new int[]{pos,(2*pos)+1});
                if (elementos.get(pos).equals(elemMovedizo)) {
                    //no me movi
                    pos = tamaño;
                }else{
                    //me fui al hijo
                    pos = (2*pos)+1;
                }
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


public void heapify(int pos){
    if(tamaño > 2 && pos>1){
        if(elementos.get((pos-1)/2) == elementos.get((pos-2)/2)){
            siftDown(new int[]{(pos-1)/2,pos-1,pos});
            heapify(pos-=2);
        }else{
            siftDown(new int[]{(pos-1)/2,pos});
            heapify(pos-=1);
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

public void siftDown(int[] elems){
    T padre = elementos.get(elems[0]);
    if(elems.length == 2){
            if(comparador.compare(padre, elementos.get(elems[1])) < 0){
                elementos.set(elems[0], elementos.get(elems[1]));
                elementos.set(elems[1], padre);
            }
    }else if(elems.length == 3){
        int hijoMayor = comparador.compare(elementos.get(elems[1]), elementos.get(elems[2])) > 0 ? elems[1] : elems[2];
        if(comparador.compare(padre, elementos.get(hijoMayor)) < 0){
            elementos.set(elems[0], elementos.get(hijoMayor));
            elementos.set(hijoMayor, padre);
        }
    }
}


public static void main(String[] args) {
    Comparator<Ciudad> SuperavitComparator = Comparator.comparing(Ciudad::superavit);
    Ciudad ciudad0 = new Ciudad(0);
    Ciudad ciudad1 = new Ciudad(1);
    Ciudad ciudad2 = new Ciudad(2);
    ciudad0.incr_ganancia(200); //200 super
    ciudad1.incr_ganancia(10); //10 super
    ciudad2.incr_perdida(400); //-400 super
    Heap<Ciudad> heap = new Heap<Ciudad>(new Ciudad[]{ciudad2,ciudad1,ciudad0}, SuperavitComparator);
    Ciudad gotica = new Ciudad(3);
    gotica.incr_perdida(-3000);
    // System.out.println(heap.sacarMaximo());
    heap.Anhadir(gotica);
}

}
