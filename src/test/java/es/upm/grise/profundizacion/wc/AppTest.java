package es.upm.grise.profundizacion.wc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AppTest {

    private static Path testFile = Paths.get("ejemplo.txt");

    @BeforeAll
    public static void setup() throws IOException {
        Files.writeString(testFile, "kjdbvws wonvwofjw\n sdnfwijf ooj    kjndfohwouer 21374 vehf\n jgfosj\n\nskfjwoief ewjf\n\n\ndkfgwoihgpw vs wepfjwfin");
    }

    @AfterAll
    public static void teardown() {
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            System.err.println("Error deleting test file: " + e.getMessage());
            try {
                Thread.sleep(100);
                Files.deleteIfExists(testFile);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Failed to delete test file on retry: " + ex.getMessage());
            }
        }
    }


    @Test
    public void testUsageMessageWhenNoArgs() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] {});
        
        assertEquals("Usage: wc [-clw file]\n".trim(), output.toString().trim());
    }

    @Test
    public void testErrorWithMoreThanTwoArgs() {
    	// Prueba el caso de error cuando se pasan demasiados argumentos.
    	// Cubre la rama del 'if (args.length != 2)' en App.java.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // Ejecutamos con 3 argumentos
        App.main(new String[] {"-c", "file", "extra"});
        
        assertEquals("Wrong arguments!", output.toString().trim());
    }

    @Test
    public void testFileNotFound() {
    	// Simula el error de lectura pasando un fichero inexistente.
    	// Cubre el bloque 'catch' que maneja archivos no encontrados.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // Usamos un nombre de archivo que seguro no existe
        String fakeFile = "archivo_inexistente_12345.txt";
        App.main(new String[] {"-c", fakeFile});
        
        assertEquals("Cannot find file: " + fakeFile, output.toString().trim());
    }

    @Test
    public void testCommandsFormatError() {
    	// Verifica que el programa detecte si los comandos no empiezan por guion ('-').
    	// Cubre la validación de formato de comandos en App.java.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // El comando "c" no empieza por guion "-"
        App.main(new String[] {"c", "ejemplo.txt"});
        
        assertEquals("The commands do not start with -", output.toString().trim());
    }

    @Test
    public void testUnrecognizedCommand() {
    	// Prueba qué pasa si enviamos un comando desconocido (ej: 'x').
    	// Cubre la rama 'default' del switch en el bucle de procesamiento.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // El comando 'x' no existe en el switch
        App.main(new String[] {"-x", "ejemplo.txt"});
        
        assertEquals("Unrecognized command: x", output.toString().trim());
    }
    
    @Test
    public void testHappyPath() {
    	// Ejecución completa correcta (Happy Path).
    	// Verifica que, con argumentos válidos, el programa entra en los casos 'c', 'l' y 'w' 
    	// y genera salida sin errores.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        // "ejemplo.txt" se crea en el setup() con contenido real.
        // Pedimos las 3 cosas: caracteres (c), lineas (l) y palabras (w)
        App.main(new String[] {"-clw", "ejemplo.txt"});
        
        String out = output.toString();
        // Verificamos que la salida contiene el nombre del archivo
        // y que ha impreso resultados (tiene tabuladores)
        assertTrue(out.contains("ejemplo.txt"));
        assertTrue(out.contains("\t"));
    }


}
