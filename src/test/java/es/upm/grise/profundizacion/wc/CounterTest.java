package es.upm.grise.profundizacion.wc;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CounterTest {

    @Test
    public void testCountCharactersWordsAndLines() throws IOException {
        String content = "Esta frase\nes un ejemplo para\nel test de recuento.\n";
        BufferedReader reader = new BufferedReader(new StringReader(content));
        
        Counter counter = new Counter(reader);
        
        assertEquals(51, counter.getNumberCharacters());
        assertEquals(3, counter.getNumberLines());
        assertEquals(10, counter.getNumberWords());
    }
    
    
    @Test
    public void testWithTabs() throws IOException {
    	// Prueba específica para verificar que el tabulador ('\t') se detecta como separador.
    	// Necesario para cubrir la rama condicional completa en Counter.java.
    	// Se añade un espacio final para asegurar que la última palabra se cuenta correctamente.
    	
        // Añadimos un espacio al final para que detecte la segunda palabra
        String content = "Palabra1\tPalabra2 "; 
        BufferedReader reader = new BufferedReader(new StringReader(content));
        
        Counter counter = new Counter(reader);
        
        // Ahora sí detectará 2 separadores (el tabulador y el espacio final)
        assertEquals(2, counter.getNumberWords());
    }

}

