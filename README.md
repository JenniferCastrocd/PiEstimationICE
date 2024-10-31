# Pi Estimation using ICE

This project implements the Monte Carlo method to estimate the value of π, using the Internet Communications Engine (ICE). The project follows a master-worker architecture, where a central coordinator distributes tasks to multiple workers, each calculating points in a quarter-circle to approximate the value of π.

## Team Members
- **Jennifer Castro - A00400253**
- **Mateo Berrio -A00399877**
- **Juan Manuel Casanova -A00400090**
- **Juan Camilo Corrales -A00366910**

# **Architecture**

1. **The master (coordinator)** distributes work across the network to multiple **workers** using ICE.
2. **Each worker** calculates a subset of random points, checks if each point falls within the quarter-circle, and returns the results to the master.
3. **The master** collects all results and calculates the estimated value of π.

# Program Execution

1. First, point the terminal to be in monteCarlo

```
cd monteCarlo
```
2. Now, execute the master:

```
java -jar master/build/libs/master.jar
```

3. Next, open a new terminal (without killing the prior process) and execute a worker, be sure to indicate the id, and don't forget that ids are unique, so you can't enter the same id twice:

```
java -jar worker/build/libs/worker.jar 1
```

For creating more workers, you just need to open a new terminal and repeat the process for the number of desired workers that you want to implement. Remember not to duplicate ids.

4. Finally, open once again a new terminal and run the client:

```
java -jar client/build/libs/client.jar
```

follow the instructions and... happy coding!


*laudetur iesus christus*


