import MonteCarlo.TaskCoordinator;
import MonteCarlo.WorkerServicePrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MasterImplementation implements TaskCoordinator{
    private List<WorkerServicePrx> workerProxies = new ArrayList<>();


    @Override
    // Método para registrar un worker directamente a través de su proxy
    public void registerWorker(WorkerServicePrx workerProxy, Current current) {
        if (workerProxy != null) {
            workerProxies.add(workerProxy);
            System.out.println("Worker registrado: " + workerProxy);
        } else {
            System.err.println("Worker no válido.");
        }
    }

    @Override
    public float estimatePi(int numPoints, Current current) {
        int numWorkers = workerProxies.size();
        if (numWorkers == 0) {
            System.out.println("No hay workers registrados para realizar la estimación.");
            return 0.0f;
        }

        System.out.println("Estimando el valor de pi con " + numPoints + " puntos y " + numWorkers + " trabajadores...");
        int pointsPerWorker = numPoints / numWorkers;

        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < numWorkers; i++) {
            WorkerServicePrx worker = workerProxies.get(i);
            System.out.println("Solicitando al worker " + (i + 1) + " calcular " + pointsPerWorker + " puntos...");
            CompletableFuture<Integer> future = worker.calculatePointsAsync(pointsPerWorker);
            futures.add(future);
        }

        int totalPointsInCircle = 0;
        try {
            CompletableFuture<Void> allOf = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            );

            allOf.get();

            for (CompletableFuture<Integer> future : futures) {
                totalPointsInCircle += future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error al obtener resultados: " + e.getMessage());
            return 0.0f;
        }

        return 4.0f * totalPointsInCircle / numPoints;
    }
}
