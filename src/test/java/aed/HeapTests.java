package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import aed.BestEffort.AntigComparador;

import org.junit.jupiter.api.BeforeEach;

public class HeapTests {

    Tupla<Ciudad,Handler>[] ciudades = new Tupla[13];
    Tupla<Ciudad,Handler>[] ciudades1 = new Tupla[15];
    Tupla<Ciudad,Handler>[] ciudades2 = new Tupla[8];
    Comparator<Ciudad> superavitComparador;

    @BeforeEach
    void init(){
        //Reiniciamos los valores de los elementos del heap a testear antes de cada test
        superavitComparador = Comparator.comparing(Ciudad::superavit);
        Integer[] ganancias = {10,29,40,17,33,59,53,51,81,79,98,97,99};
        Integer[] ganancias1 = {10,29,40,17,33,59,53,51,81,79,98,97,99,100,1};
        Integer[] ganancias2 = {10,29,40,17,33,59,53,51};

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

        for (int i=0;i<ciudades2.length;i++){
            Ciudad ciudadI = new Ciudad(i);
            ciudades2[i] = new Tupla<Ciudad,Handler>(ciudadI, ciudadI.handler());
            ciudades2[i].ObtenerPrimero().incr_ganancia(ganancias2[i]);
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

    void assertSetEquals1(ArrayList<Integer> s1, ArrayList<Integer> s2) {
        assertEquals(s1.size(), s2.size());
        int i = 0;
        while (i<s1.size()) {
            assertTrue(s1.get(i) == s2.get(i), s1.get(i) + " es distinto a " + s2.get(i));
            i++;
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
    void uno_ultimo_nivel(){
        Heap<Ciudad> heap = new Heap<Ciudad>(ciudades2,superavitComparador);
        //me fijo que el tamaño y el maximo sean correctos
        assertEquals(8,heap.tamaño());
        Ciudad ciudadEsperada = new Ciudad(5);
        ciudadEsperada.handler().set_ref(0);
        ciudadEsperada.incr_ganancia(59);
        assertEquals(ciudadEsperada, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        int[] posicionesEsperadas = new int[]{5,7,6,1,4,2,0,3};
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

    public class AntigComparador implements Comparator<Traslado>{
        @Override
        public int compare(Traslado tras1, Traslado tras2){  //COMPLEJIDAD DE LA FUNCION: O(1)
            return Integer.compare(-tras1.timestamp(),-tras2.timestamp());
        }
    }

    @Test
    void stress(){
        AntigComparador AntiguedadComparator = new AntigComparador();
        Tupla<Traslado,Handler>[] traslados = new Tupla[31];
        for (int i=traslados.length;i>0;i--){
            Traslado trasladoI = new Traslado(traslados.length-i+1, 1, 2, 2, i);
            traslados[31-i] = new Tupla<Traslado,Handler>(trasladoI, trasladoI.handlerAnti());
        }

        Heap<Traslado> heap = new Heap<Traslado>(traslados,AntiguedadComparator);

        //me fijo que el tamaño y el maximo sean correctos
        assertEquals(31,heap.tamaño());
        Traslado TrasladoEsperada = new Traslado(31,1,2,2,1);
        TrasladoEsperada.handlerAnti().set_ref(0);
        assertEquals(TrasladoEsperada, heap.maximo());

        //nos fijamos que todos los nodos esten en las posiciones esperadas
        ArrayList<Integer> elems = new ArrayList<Integer>();
        for (int i = 0; i < heap.tamaño(); i++) {
            elems.add(heap.obtenerElem(i).timestamp());
        }
        assertSetEquals1(new ArrayList<>(Arrays.asList(1,9,2,13,10,5,3,15,14,11,21,7,6,4,17,16,24,28,23,12,22,27,30,8,20,26,19,31,18,25,29)), elems);
    }

}
