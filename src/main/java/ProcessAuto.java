import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase ProcessAuto proporciona métodos para interactuar con procesos del sistema operativo.
 * Permite lanzar comandos en una nueva terminal, ejecutar procesos en paralelo,
 * redirigir la salida estándar y de error, y ejecutar comandos capturando su salida.
 * Esta clase está diseñada para facilitar la automatización de tareas que requieren
 * la ejecución de herramientas de línea de comandos externas.
 */
public class ProcessAuto {

    public ProcessAuto() {
    }

    // METODO 1 - LanzarPrograma
    public static void lanzarPrograma(){
        try {

            String comandosParaEjecutar = "echo ¡Hola, bienvenido a la terminal! & ipconfig";
            String[] comandoFinal = {
                    "cmd.exe",
                    "/c",
                    "start",
                    "cmd.exe",
                    "/k",
                    comandosParaEjecutar
            };

            //System.out.println("\n--- Lanzando nueva terminal con los comandos: " + comandosParaEjecutar + " ---");
            System.out.println("\n--- Lanzando nueva terminal con los comandos: " + String.join(" ",comandoFinal) + " ---");

            ProcessBuilder pb = new ProcessBuilder(comandoFinal);
            Process proceso = pb.start();

            // Esperamos a que el proceso de lanzamiento (el primer cmd.exe /c start) termine.
            // Esto es casi instantáneo. No espera a que la nueva ventana se cierre.
            proceso.waitFor();

            System.out.println("--- Proceso de lanzamiento finalizado. ---");

        } catch (IOException | InterruptedException e) {
            System.out.println("Error al lanzar el programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // METODO 2 lanzarContadores



    public void lanzarContadoresEnParalelo() {
       /* System.out.println("--- Iniciando el método para lanzar contadores en paralelo ---");

        // 1. Creamos las dos tareas que se ejecutarán.
        //    Usamos la clase 'MiTarea' que está definida más abajo.
        MiTarea tarea1 = new MiTarea("A");
        MiTarea tarea2 = new MiTarea("B");

        // 2. Creamos los "trabajadores" (hilos) y le asignamos una tarea a cada uno.
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread(tarea2);

        // 3. Damos prioridades. Es una sugerencia para el sistema operativo.
        //    Le decimos que intente dar más atención al hilo1.
        hilo1.setPriority(Thread.MAX_PRIORITY);
        hilo2.setPriority(Thread.MIN_PRIORITY);

        // 4. ¡Damos la orden de empezar! Los hilos arrancan y ejecutan su tarea.
        System.out.println("Lanzando los hilos. Empezarán a contar ahora...");
        hilo1.start();
        hilo2.start();

        // El hilo principal (el que ha llamado a este método) imprime esto y termina.
        // ¡Pero los hilos 1 y 2 seguirán trabajando en segundo plano!
        System.out.println("--- El método 'lanzarContadoresEnParalelo' ha terminado de dar las órdenes ---");
   */ }

    // METODO 3 redireccionSalida

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
        // METODO 4 parametrosDinamicos
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

    // METODO 5

    /**
     * Ejecuta un comando del sistema operativo y captura su salida estándar y de error.
     * Este método utiliza {@link Runtime#exec(String)} para crear un nuevo proceso.
     * Espera a que el comando finalice y lee tanto la salida estándar como el flujo de error,
     * concatenando ambos en una única cadena de texto.
     *
     * @param comando El comando completo a ejecutar como una cadena de texto (ej. "ipconfig", "java -version").
     * @return Una cadena de texto que contiene la salida estándar y de error generada por el comando.
     *         Si ocurre un error durante la ejecución (IOException o InterruptedException),
     *         devuelve un mensaje de error detallado.
     * @author: Bruno Coloma
     */
    public static String ejecucionComando(String comando){

        StringBuilder salida = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(comando);
            p.waitFor();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String l;

            while ((l = stdInput.readLine()) != null){
                salida.append(l).append("\n");
            }
            while((l = stdError.readLine()) != null){

                salida.append(l).append("\n");

            }

        } catch (IOException | InterruptedException e) {

            System.out.println("Error al ejecutar el comando: " + e.getMessage());
            return "Error al ejecutar el comando: " + comando + "\n" + e.getMessage();
        }
        return salida.toString();
    }

    // METODO 6
    public static void escanerPuertos() {

        System.out.println("Buscando puertos en estado LISTENING");

        try {
            String comando = "netstat -ano";

            Process process = Runtime.getRuntime().exec(comando);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            StringBuilder netstatOutput = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                netstatOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Comando finalizado. ExitCode:" + exitCode);

            if (exitCode == 0) {
                System.out.println("\nPuertos TCP en modo LISTENING (Escucha):");

                Pattern pattern = Pattern.compile("TCP\\s+\\S+:(\\d+)\\s+.*LISTENING");
                Matcher matcher = pattern.matcher(netstatOutput.toString());

                while (matcher.find()) {
                    String port = matcher.group(1);
                    System.out.println("Puerto ABIERTO: " + port);
                }
            } else {
                System.err.println("Error al ejecutar netstat, el comando fallo");
            }

        } catch (
                IOException e) {
            System.err.println("Error de Entrada/Salida: Probablemente netstat encontro o hubo un problema a leer su salida");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("El proceso fue interrumpido");
            e.printStackTrace();
        }
    }




}
