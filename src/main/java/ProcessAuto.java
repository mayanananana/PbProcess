import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessAuto {

    public ProcessAuto() {
    }

    // METODO 3

    public void redireccionSalida(String comando){
        // Es más robusto separar el comando y sus argumentos
        ProcessBuilder pb = new ProcessBuilder(comando.split("\\s+"));

        // Configura la redirección ANTES de iniciar el proceso
        pb.redirectOutput(new File("standard_output.txt")); // Corregido "standar"
        pb.redirectError(new File("error.txt"));

        try {
            System.out.println("Ejecutando comando: '" + comando + "'...");
            Process p = pb.start();
            int exitCode = p.waitFor(); // Espera a que termine
            System.out.println("Proceso finalizado con código: " + exitCode);
            System.out.println("La salida estándar se ha guardado en 'standard_output.txt'");
            System.out.println("La salida de error se ha guardado en 'error.txt'");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al ejecutar el comando: " + comando);
        }
    }
        // METODO 4
    public void parametrosDinamicos(String comando, String paquetes, String host){
        List<String> commandList = new ArrayList<>();
        commandList.add(comando);

        if (!"N".equalsIgnoreCase(paquetes)) {
            commandList.add("-n");
            commandList.add(paquetes);
        }
        commandList.add(host);

        ProcessBuilder pb = new ProcessBuilder(commandList);

        pb.inheritIO();

        try {
            System.out.println("Ejecutando comando: " + String.join(" ", commandList));
            Process p = pb.start();
            int exitCode = p.waitFor();
            System.out.println("\nProceso finalizado con código: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al ejecutar el comando: " + comando);
        }
    }
}
