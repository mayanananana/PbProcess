import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int op = 0;
        Scanner sc = new Scanner(System.in);
        ProcessAuto processAuto = new ProcessAuto();
        do {
            menu();
            try {
                op = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes introducir un número entero.");
                op = -1; // Para que no salga del bucle y lo vuelva a pedir
            }
            sc.nextLine(); // Limpiamos buffer

            switch (op) {
                case 1: ProcessAuto.lanzarPrograma();
                    break;
                case 2:
                        ProcessAuto.lanzarContadores();
                    break;
                case 3:
                    redireccionSalida(sc, processAuto);
                    break;
                case 4:
                    parametrosDinamicos(sc, processAuto);
                    break;
                case 5:
                    ejecutorComandos(sc);
                    break;
                case 6:
                    processAuto.escanerPuertos();
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case -1:
                    break; // No hace nada, solo vuelve a iterar
                default:
                    System.out.println("Opción no válida");
            }
        } while (op != 0);
        sc.close();
    }



    private static void ejecutorComandos(Scanner sc) {
        System.out.println("=============================================");
        System.out.println("    Ejecutor de Comandos de Consola");
        System.out.println("=============================================");
        System.out.print("Introduce el comando que quieres ejecutar (ej: ipconfig, dir, java -version): ");

        String comando = sc.nextLine();

        System.out.println("\nEjecutando '" + comando + "' y guardando el resultado...");
        String resultadoComando = ProcessAuto.ejecucionComando(comando);

        System.out.println("---------------------------------------------");
        System.out.println("  El resultado del comando se ha guardado.  ");
        System.out.println("  Mostrando el contenido de la variable:    ");
        System.out.println("---------------------------------------------");

        System.out.println(resultadoComando);

    }

    private static void parametrosDinamicos(Scanner sc, ProcessAuto processAuto) {
        System.out.println("Introduce el comando (ej: ping, tracert)");
        String comando= sc.nextLine();

        System.out.println("Introduce los paquetes (ej: -n 5, -c 5)");
        System.out.println("En caso de no querer trabajar con paquetes, pulsa N");
        String paquetes= sc.nextLine();
        System.out.println("Introduce el host (ej: google.com, 8.8.8.8)");
        String host= sc.nextLine();
        processAuto.parametrosDinamicos(comando, paquetes, host);
    }

    private static void redireccionSalida(Scanner sc, ProcessAuto processAuto) {

        System.out.println("Introduce el comando");
        System.out.println("Algunas opciones: java -version, adb devices, ls -l");
        String comando = sc.nextLine();
        processAuto.redireccionSalida(comando);
    }

    public static void menu() {
        System.out.println("1. Abrir terminal y ejecutar comando para obtener información sobre la red");
        System.out.println("2. Ejemplo de programación concurrente");
        System.out.println("3. Redireccion de salida estandar y de error");
        System.out.println("4. Uso de parametros dinamicos");
        System.out.println("5. Ejecutar un comando y guardar la salida en una variable");
        System.out.println("6. Verificar puertos abiertos ");
        System.out.println("0. Salir");
    }
}