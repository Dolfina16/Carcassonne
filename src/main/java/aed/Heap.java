package aed;

import java.util.ArrayList;

public class Heap <T extends Comparable<T>> {
    private ArrayList<T> elementos;
    private int tamaño;

public Heap ( T[] arreglo){
    for (T t : arreglo) {
        elementos.add(t);
    }
}

public void siftDown(){
    int pos;
    if(tamaño > 1){
        pos = tamaño - 1;
        if((pos-1)/2 == (pos-2)/2){
            if(elementos.get((pos-1)/2).compareTo(elementos.get(pos)) < 1){
                
            }
        }
    }
}

}
