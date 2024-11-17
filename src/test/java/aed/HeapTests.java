package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HeapTests {

    Tupla<Ciudad,Handler>[] ciudades = new Tupla[13];
    Tupla<Ciudad,Handler>[] ciudades1 = new Tupla[15];
    Comparator<Ciudad> superavitComparador;

    @BeforeEach
    void init(){
        //Reiniciamos los valores de los elementos del heap a testear antes de cada test
        superavitComparador = Comparator.comparing(Ciudad::superavit);
        Integer[] ganancias = {10,29,40,17,33,59,53,51,81,79,98,97,99};
        Integer[] ganancias1 = {10,29,40,17,33,59,53,51,81,79,98,97,99,100,1};

        for (int i=0;i<ciudades.length;i++){
            Ciudad ciudadI = new Ciudad(i);
            ciudades[i] = new Tupla<Ciudad,Handler>(ciudadI, ciudadI.handler());
            ciudades[i].ObtenerPrimero().incr_ganancia(ganancias[i]);
        }    
        
        for (int i=0;i<ciudades1.length;i++){
            Ciudad ciudadI = new Ciudad(i);
            ciudades1[i] = new Tupla<Ciudad,Handler>(ciudadI, ciudadI.handler());
            ciudades1[i].ObtenerPrimero().incr_ganancia(ganancias1[i]);
        } 
    }

    void assertPosEsperadas(Heap<Ciudad> heap, int[] posEsperadas){
        for (int i = 0; i < heap.tamaño(); i++) {
            assertEquals(posEsperadas[heap.obtenerElem(i).id()], heap.obtenerElem(i).handler().ref());
        }
    }

    void assertSetEquals(int[] s1, int[] s2) {
        assertEquals(s1.length, s2.length);
        for (int e1 : s1) {
            boolean encontrado = false;
            for (int e2 : s2) {
                if (e1 == e2) encontrado = true;
            }
            assertTrue(encontrado, "No se encontró el elemento " +  e1 + " en el arreglo " + s2.toString());
        }
    }

    @Test
    void heap_incompleto(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades,superavitComparador);
        //me fijo que el tamaño y el maximo sean correctos
        assertEquals(13,heap.tamaño());
        Ciudad ciudadEsperada = new Ciudad(12);
        ciudadEsperada.handler().set_ref(0);
        ciudadEsperada.incr_ganancia(99);
        assertEquals(ciudadEsperada, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{12,9,11,8,10,5,6,7,3,4,1,2,0};
        assertPosEsperadas(heap, posicionesEsperadas);
    }

    @Test
    void heap_completo(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades1,superavitComparador);
        //me fijo que el tamaño y el maximo sean correctos
        assertEquals(15,heap.tamaño());
        Ciudad ciudadEsperada = new Ciudad(13);
        ciudadEsperada.handler().set_ref(0);
        ciudadEsperada.incr_ganancia(100);
        assertEquals(ciudadEsperada, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{13,10,12,8,9,11,6,7,3,1,4,0,5,2,14};
        int[] elems = new int[heap.tamaño()];
        for (int i = 0; i < elems.length; i++) {
            elems[i] = heap.elementos().get(i).id();
        }
        assertSetEquals(posicionesEsperadas, elems);
    }

    @Test
    void añadir_elemento(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades,superavitComparador);
        
        //agrego elemento 100,me fijo el tamaño, el máximo y que todos los elementos queden en las posiciones correctas
        Ciudad ciudad100 = new Ciudad(13);
        ciudad100.incr_ganancia(100);
        Tupla<Ciudad,Handler> elemento100 = new Tupla<Ciudad,Handler>(ciudad100, ciudad100.handler());
        heap.Añadir(elemento100);

        assertEquals(14, heap.tamaño());
        assertEquals(ciudad100, heap.maximo());
        
        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{12,9,11,8,10,5,13,7,3,4,1,6,2,0};
        assertPosEsperadas(heap, posicionesEsperadas);
    }
    
    @Test
    void reordenar_elementos(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades,superavitComparador);

        heap.obtenerElem(0).incr_perdida(98);

        heap.Reordenar(0, false);
        Ciudad maxNuevo = new Ciudad(10);
        maxNuevo.handler().set_ref(0);
        maxNuevo.incr_ganancia(98);

        assertEquals(13, heap.tamaño());
        assertEquals(maxNuevo, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{12,9,11,8,10,5,6,3,1,4,0,2,7};
        assertPosEsperadas(heap, posicionesEsperadas);
    }
    
    @Test
    void sacar_elemento(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades,superavitComparador);

        //saco el maximo,me fijo el tamaño y el nuevo maximo
        heap.Sacar(0);
        Ciudad maxNuevo = new Ciudad(10);
        maxNuevo.handler().set_ref(0);
        maxNuevo.incr_ganancia(98);

        assertEquals(12, heap.tamaño());
        assertEquals(maxNuevo, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{7,9,11,8,10,5,6,3,1,4,0,2};
        assertPosEsperadas(heap, posicionesEsperadas);
    }

}
