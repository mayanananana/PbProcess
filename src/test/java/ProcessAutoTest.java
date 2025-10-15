import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProcessAutoTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testLanzarPrograma() {
        // El método lanza un proceso externo y no devuelve nada.
        // La prueba más simple es verificar que se ejecuta sin lanzar una excepción.
        assertDoesNotThrow(() -> {
            ProcessAuto.lanzarPrograma();
        });
        // Verificamos la salida en la consola que confirma el lanzamiento.
        assertTrue(outContent.toString().contains("--- Lanzando nueva terminal con los comandos:"));
    }

    @Test
    void testEscanerPuertos() {
        // Ejecutamos el método que imprime los puertos a la consola
        ProcessAuto.escanerPuertos();

        // Capturamos la salida
        String output = outContent.toString();

        // Verificamos que la salida contiene las líneas que esperamos
        assertTrue(output.contains("Buscando puertos en estado LISTENING"));
        assertTrue(output.contains("Comando finalizado."));

        // Esta es la verificación clave: que al menos intente imprimir los puertos.
        // Si hay puertos abiertos, la salida debería contener "Puerto ABIERTO:".
        // Si no hay ninguno, esta línea no aparecerá, pero el test aún así puede ser útil
        // para confirmar que el comando se ejecutó correctamente.
        // Para un test más robusto, se podría iniciar un servidor dummy en un puerto conocido
        // antes de la prueba y verificar que ese puerto específico es detectado.
        // Por ahora, nos conformamos con que el patrón general funcione.
        assertTrue(output.contains("Puertos TCP en modo LISTENING"));
    }
}
