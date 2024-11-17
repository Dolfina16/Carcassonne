package aed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class BestEffortTestsPropios {

    int cantCiudades;
    Traslado[] listaTraslados;
    ArrayList<Integer> actual;


    @BeforeEach
    void init(){
        //Reiniciamos los valores de las ciudades y traslados antes de cada test
        cantCiudades = 7;
        listaTraslados = new Traslado[] {
                                            new Traslado(1, 0, 1, 100, 10),
                                            new Traslado(2, 0, 1, 400, 20),
                                            new Traslado(3, 3, 4, 500, 50),
                                            new Traslado(4, 4, 3, 500, 11),
                                            new Traslado(5, 1, 0, 1000, 40),
                                            new Traslado(6, 1, 0, 1000, 41),
                                            new Traslado(7, 6, 3, 2000, 42)
                                        };
    }

    void assertSetEquals(ArrayList<Integer> s1, ArrayList<Integer> s2) {
        assertEquals(s1.size(), s2.size());
        for (int e1 : s1) {
            boolean encontrado = false;
            for (int e2 : s2) {
                if (e1 == e2) encontrado = true;
            }
            assertTrue(encontrado, "No se encontrÃ³ el elemento " +  e1 + " en el arreglo " + s2.toString());
        }
    }
    
    void assertSetEquals(int[] s1, int[] s2) {
        assertEquals(s1.length, s2.length);
        for (int e1 : s1) {
            boolean encontrado = false;
            for (int e2 : s2) {
                if (e1 == e2) encontrado = true;
            }
            assertTrue(encontrado, "No se encontrÃ³ el elemento " +  e1 + " en el arreglo " + s2.toString());
        }
    }
    
    @Test
    void despachar_sin_traslados(){
        Traslado[] listaVacia = new Traslado[]{};

        BestEffort sis = new BestEffort(this.cantCiudades, listaVacia);

        int[] idsDespachados = sis.despacharMasAntiguos(4);

        assertEquals(0, idsDespachados.length);
        assertSetEquals(new ArrayList<>(Arrays.asList()), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList()), sis.ciudadesConMayorPerdida());
    }

    @Test
    void despachar_con_mas_despachos_que_traslados(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        sis.despacharMasRedituables(8);

        assertSetEquals(new ArrayList<>(Arrays.asList(1, 6)), sis.ciudadesConMayorGanancia());
        assertSetEquals(new ArrayList<>(Arrays.asList(3)), sis.ciudadesConMayorPerdida());
    }

    @Test
    void chequear_ids_despacharRedituables(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        assertSetEquals(new int[]{7,6,5}, sis.despacharMasRedituables(3));
    }
    
    @Test
    void chequear_ids_despacharAntiguedad(){
        BestEffort sis = new BestEffort(this.cantCiudades, this.listaTraslados);

        assertSetEquals(new int[]{1,4,2}, sis.despacharMasAntiguos(3));
    }

    @Test
    void mayor_superavit_con_menor_id(){
        BestEffort sis = new BestEffort(this.cantCiudades,this.listaTraslados);
        sis.despacharMasRedituables(3);
        assertEquals(1, sis.ciudadConMayorSuperavit());
    }
}
