import MonteCarlo.TaskCoordinatorPrx;
import MonteCarlo.WorkerServicePrx;
import MonteCarlo.WorkerService;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.ObjectAdapter;

public class Worker {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Por favor, especifique el ID del trabajador como argumento.");
            return;
        }

        try (Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            int workerId = Integer.parseInt(args[0]);

            MonteCarlo.WorkerService worker = new WorkerServiceI();

            // Crear un adaptador en el puerto correspondiente al worker ID
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("WorkerAdapter",
                    "default -h localhost -p " + (10001 + workerId));
            
            ObjectPrx object = adapter.add(worker, com.zeroc.Ice.Util.stringToIdentity("worker" + workerId));
            adapter.activate();


            // Obtener referencia al master para registrarse
            TaskCoordinatorPrx master = TaskCoordinatorPrx.checkedCast(
                    communicator.stringToProxy("master:default -h localhost -p 10000"));

            if (master != null) {
                // Obtener proxy del worker para registrarse en el master
                WorkerServicePrx workerProxy = WorkerServicePrx.checkedCast(object);

                // Registrar el worker en el master
                master.registerWorker(workerProxy);
                System.out.println("Worker " + workerId + " registrado en el master.");
            } else {
                System.err.println("No se pudo obtener el proxy del master. Asegúrate de que el master esté en ejecución.");
            }

            System.out.println("El trabajador " + workerId + " está listo y esperando tareas...");
            communicator.waitForShutdown();
        }
    }
}
