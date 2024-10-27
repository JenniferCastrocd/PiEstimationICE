# Pi Estimation using ICE

This project implements the Monte Carlo method to estimate the value of π, using the Internet Communications Engine (ICE). The project follows a master-worker architecture, where a central coordinator distributes tasks to multiple workers, each calculating points in a quarter-circle to approximate the value of π.

## Team Members
- **Jennifer Castro**
- **Juan Camilo Corrales**
- **Mateo Berrio**
- **Juan Manuel Casanova**

# **Architecture**

1. **The master (coordinator)** distributes work across the network to multiple **workers** using ICE.
2. **Each worker** calculates a subset of random points, checks if each point falls within the quarter-circle, and returns the results to the master.
3. **The master** collects all results and calculates the estimated value of π.
