/**
 * La clase ProcessThreads representa una tarea simple que puede ser ejecutada por un hilo.
 * Implementa la interfaz Runnable, lo que obliga a definir el método run(),
 * que contiene la lógica que se ejecutará en segundo plano.
 */
public class ProcessThreads implements Runnable {

    // Nombre para identificar esta tarea y diferenciarla de otras.
    private String nombre;

    /**
     * Constructor para crear una nueva tarea con un nombre específico.
     * @param nombre El nombre que se le asignará a esta tarea.
     */
    public ProcessThreads(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Este es el método principal que se ejecutará cuando el hilo comience.
     * Contiene la lógica de la tarea: en este caso, contar de 1 a 10.
     */
    @Override
    public void run() {
        // Bucle que simula una tarea de conteo.
        for (int i = 1; i <= 10; i++) {
            // Imprime el nombre de la tarea y el número actual del conteo.
            System.out.println("Tarea " + nombre + ": contando - " + i);

            try {
                // Pausa la ejecución del hilo por 100 milisegundos.
                // Esto ayuda a simular que la tarea está haciendo un trabajo
                // y permite que otros hilos tengan la oportunidad de ejecutarse.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Si el hilo es interrumpido mientras "duerme", se lanza esta excepción.
                // Imprimimos un mensaje y terminamos la ejecución del hilo.
                System.err.println("La tarea " + nombre + " fue interrumpida.");
                Thread.currentThread().interrupt(); // Restablece el estado de interrupción
                return; // Finaliza la ejecución del método run
            }
        }
        // Mensaje que se muestra cuando la tarea ha completado su bucle.
        System.out.println("--- Tarea " + nombre + " ha finalizado. ---");
    }
}