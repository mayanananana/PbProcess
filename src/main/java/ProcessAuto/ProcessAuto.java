package ProcessAuto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase ProcessAuto proporciona métodos para interactuar con procesos del sistema operativo.
 * <p>
 * Permite lanzar comandos en una nueva terminal, ejecutar procesos en paralelo,
 * redirigir la salida estándar y de error, y ejecutar comandos capturando su salida.
 * Esta clase está diseñada para facilitar la automatización de tareas que requieren
 * la ejecución de herramientas de línea de comandos externas.
 *
 */
public class ProcessAuto {

    /**
     * Constructor por defecto para la clase ProcessAuto.
     * No realiza ninguna operación especial.
     */
    public ProcessAuto() {
    }

    // ------------------- METODO 1 lanzarPrograma -------------------
    /**
     * Lanza una nueva ventana de la terminal de comandos (`cmd.exe`) y ejecuta comandos predefinidos en ella.
     * <p>
     * Este método construye y ejecuta un comando que utiliza `start cmd.exe /k` para abrir una nueva consola independiente.
     * El comando `echo` muestra un mensaje de bienvenida y `ipconfig` muestra la configuración de red.
     * Gracias al argumento `/k`, la nueva ventana de terminal permanecerá abierta después de que los comandos se hayan ejecutado.
     * <p>
     * <b>Nota:</b> El método no espera a que la nueva ventana de terminal sea cerrada por el usuario.
     *
     * @see ProcessBuilder
     * @see Process#waitFor()
     */
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

    // ------------------- METODO 2 lanzarContadores -------------------

    /**
     * Lanza varios contadores en paralelo utilizando hilos.
     * Cada contador se ejecuta en un hilo separado, lo que permite que se ejecuten de forma concurrente.
     * Este método, como parte de la clase {@link ProcessAuto}, demuestra la capacidad de la aplicación para gestionar
     * la ejecución de múltiples tareas simultáneamente, en este caso, representadas por la clase {@link ProcessThreads}.
     *
     *
     * La implementación crea un número determinado de instancias de {@link ProcessThreads},
     * las asigna a nuevos hilos ({@link Thread}) y los inicia.
     */
    public static void lanzarContadores() {
        System.out.println("--- Lanzando contadores en paralelo ---");

        // Número de hilos/contadores que se lanzarán.
        int numeroDeContadores = 3;

        // Lista para mantener una referencia a los hilos creados.
        List<Thread> hilos = new ArrayList<>();

        for (int i = 1; i <= numeroDeContadores; i++) {
            // Crea una nueva instancia de la tarea, asignándole un nombre único.
            ProcessThreads tarea = new ProcessThreads("Contador-" + i);

            // Crea un nuevo hilo y le pasa la tarea a ejecutar.
            Thread hilo = new Thread(tarea);

            // Inicia la ejecución del hilo. Esto llama al método run() de la tarea.
            hilo.start();

            // Añade el hilo a la lista para poder gestionarlo después.
            hilos.add(hilo);
        }

        // Bucle para esperar a que todos los hilos terminen su ejecución.
        for (Thread hilo : hilos) {
            try {
                // El método join() bloquea la ejecución del hilo actual (el principal)
                // hasta que el hilo sobre el que se llama (hilo) haya terminado.
                hilo.join();
            } catch (InterruptedException e) {
                System.err.println("El hilo principal fue interrumpido mientras esperaba.");
                // Restablece el estado de interrupción del hilo principal.
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("--- Todos los contadores han finalizado. ---");
    }

    // ------------------- METODO 3 redireccionSalida -------------------

    /**
     * Ejecuta un comando del sistema y redirige su salida estándar y de error a ficheros.
     * La salida estándar del proceso se guarda en `standard_output.txt` y la salida de error en `error.txt`.
     * Si los ficheros ya existen, su contenido será sobrescrito.
     *
     * @param comando El comando del sistema que se va a ejecutar (por ejemplo, "ipconfig" o "ls -l").
     *                El comando se divide por espacios para separar el programa de sus argumentos.
     * @see ProcessBuilder#redirectOutput(File)
     * @see ProcessBuilder#redirectError(File)
     */
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

        // ------------------- METODO 4 parametrosDinamicos -------------------
    /**
     * Ejecuta un comando del sistema de forma dinámica, permitiendo añadir
     * parámetros opcionales basados en la entrada.
     * <p>
     * Este método está diseñado para construir un comando (como 'ping') donde algunos
     * argumentos, como el número de paquetes, pueden incluirse o no. La salida
     * del comando ejecutado se mostrará directamente en la consola de esta aplicación.
     * </p>
     *
     * @param comando El comando principal a ejecutar (ej. "ping").
     * @param paquetes El valor para el parámetro "-n". Si es "N" (o "n"), el parámetro se omite.
     * @param host El host o dirección IP de destino para el comando.
     */
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

    // ------------------- METODO 5 ejecucionComando -------------------

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

    // ------------------- METODO 6 escanerPuertos -------------------

    /**
     * Escanea la máquina local en busca de puertos TCP abiertos en estado de "escucha" (LISTENING).
     * <p>
     * Ejecuta el comando `netstat -ano`, analiza su salida con una expresión regular para identificar
     * los puertos relevantes y los imprime en la consola.
     *
     * @see Runtime#exec(String)
     * @see Pattern
     */
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
