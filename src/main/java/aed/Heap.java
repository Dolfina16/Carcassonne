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
}

}
