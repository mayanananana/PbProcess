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
                case 1:
                    redireccionSalida(sc, processAuto);
                    break;
                case 2:
                    parametrosDinamicos(sc, processAuto);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case -1:
                    break; // No hace nada, solo vuelve a iterar
                default:
                    System.out.println("Opción no válida");
            }
        } while (op != 0);
    }

    private static void parametrosDinamicos(Scanner sc, ProcessAuto processAuto) {
        System.out.println("Introduce el comando");
        String comando= sc.nextLine();

        System.out.println("Introduce los paquetes");
        System.out.println("En caso de no querer trabajar con paquetes, pulsa N");
        String paquetes= sc.nextLine();
        System.out.println("Introduce el host");
        String host= sc.nextLine();
        processAuto.parametrosDinamicos(comando, paquetes, host);
    }

    private static void redireccionSalida(Scanner sc, ProcessAuto processAuto) {
        System.out.println("Introduce el comando");
        System.out.println("Algunas opciones: -----");
        String comando = sc.nextLine();
        processAuto.redireccionSalida(comando);
    }

    public static void menu() {
        System.out.println("1. Redireccion de salida estandar y de error");
        System.out.println("2. Uso de parametros dinamicos");
        System.out.println("0. Salir");
    }
}