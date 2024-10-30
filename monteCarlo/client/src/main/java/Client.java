import com.zeroc.Ice.Communicator;

import MonteCarlo.TaskCoordinatorPrx;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Client {
    public static void main(String[] args) {
        try (Communicator communicator = com.zeroc.Ice.Util.initialize(args)){
            Scanner scanner = new Scanner(System.in);

            System.out.println("Cliente iniciado...");
            System.out.println("Escriba 'exit' para terminar o ingrese el número de puntos a calcular.");

            while (true) {
                System.out.print("\nIngrese el número de puntos a calcular: ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Finalizando cliente...");
                    break;
                }

                try {
                    int numPoints = Integer.parseInt(input);
                    if (numPoints <= 0) {
                        System.out.println("Por favor, ingrese un número positivo de puntos.");
                        continue;
                    }
                    // Realizar la estimación de Pi sin especificar el número de workers
                    requestPiEstimation(communicator, numPoints).get();
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido o 'exit' para terminar.");
                } catch (Exception e) {
                    System.err.println("Error en la ejecución: " + e.getMessage());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static CompletableFuture<Void> requestPiEstimation(Communicator communicator, int numPoints) {
        // Realizar la conexión con el servidor
        TaskCoordinatorPrx master = TaskCoordinatorPrx.checkedCast(
                communicator.stringToProxy("master:default -h localhost -p 10000"));

        if (master != null) {
            System.out.println("Solicitando estimación de Pi con " + numPoints + " puntos...");

            // Enviar solicitud de estimación sin especificar el número de workers
            return master.estimatePiAsync(numPoints)
                    .thenAccept(result -> {
                        System.out.println("\nResultados:");
                        System.out.println("------------");
                        System.out.println("Estimación de Pi: " + result);
                        System.out.println("Valor real de Pi: " + Math.PI);
                        System.out.println("Error absoluto: " + Math.abs(result - Math.PI));
                        System.out.println("------------");
                    });
        }
        return CompletableFuture.completedFuture(null);
    }

}
