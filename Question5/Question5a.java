// a)	Implement ant colony algorithm solving travelling a salesman problem


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Class representing an Ant Colony for solving the Traveling Salesman Problem
class AntColony {
    private double[][] pheromones; // Matrix to store pheromone levels on edges
    private double[][] distances; // Matrix representing distances between nodes
    private int nAnts; // Number of ants in the colony
    private double decay; // Rate at which pheromones decay
    private double alpha; // Weight of pheromones in decision-making
    private double beta; // Weight of distances in decision-making

    // Constructor to initialize the AntColony with distances, number of ants, decay rate, alpha, and beta values
    public AntColony(double[][] distances, int nAnts, double decay, double alpha, double beta) {
        this.distances = distances;
        this.nAnts = nAnts;
        this.decay = decay;
        this.alpha = alpha;
        this.beta = beta;

        int nNodes = distances.length;
        this.pheromones = new double[nNodes][nNodes];

        // Initialize pheromones to 1.0
        for (int i = 0; i < nNodes; i++) {
            Arrays.fill(pheromones[i], 1.0);
        }
    }

    // Method to find the optimal tour by running the ant colony optimization algorithm
    public List<Integer> findOptimalTour() {
        int nNodes = distances.length;
        List<Integer> bestTour = null;
        double bestTourLength = Double.POSITIVE_INFINITY;

        // Run the optimization algorithm for a fixed number of iterations
        for (int iteration = 0; iteration < 100; iteration++) {
            List<List<Integer>> antTours = generateAntTours();
            updatePheromones(antTours);

            // Iterate over each ant's tour and update the best tour if needed
            for (List<Integer> tour : antTours) {
                double tourLength = calculateTourLength(tour);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = new ArrayList<>(tour);
                }
            }

            // Decay pheromones after each iteration
            for (int i = 0; i < nNodes; i++) {
                for (int j = 0; j < nNodes; j++) {
                    pheromones[i][j] *= decay;
                }
            }
        }

        return bestTour;
    }

    // Method to generate tours for each ant in the colony
    private List<List<Integer>> generateAntTours() {
        int nNodes = distances.length;
        List<List<Integer>> antTours = new ArrayList<>();

        // Iterate over each ant in the colony
        for (int ant = 0; ant < nAnts; ant++) {
            List<Integer> tour = new ArrayList<>();
            boolean[] visited = new boolean[nNodes];
            int startNode = new Random().nextInt(nNodes);

            tour.add(startNode);
            visited[startNode] = true;

            // Build the tour for each ant
            for (int step = 1; step < nNodes; step++) {
                int nextNode = selectNextNode(tour, visited);
                tour.add(nextNode);
                visited[nextNode] = true;
            }

            antTours.add(tour);
        }

        return antTours;
    }

    // Method to select the next node for an ant based on pheromone and distance information
    private int selectNextNode(List<Integer> tour, boolean[] visited) {
        int currentNode = tour.get(tour.size() - 1);
        int nNodes = distances.length;
        double[] probabilities = new double[nNodes];
        double sum = 0;

        // Calculate probabilities for unvisited nodes
        for (int nextNode = 0; nextNode < nNodes; nextNode++) {
            if (!visited[nextNode]) {
                double pheromone = Math.pow(pheromones[currentNode][nextNode], alpha);
                double distance = Math.pow(1.0 / distances[currentNode][nextNode], beta);
                probabilities[nextNode] = pheromone * distance;
                sum += probabilities[nextNode];
            }
        }

        // Roulette wheel selection
        double rouletteWheel = new Random().nextDouble() * sum;
        double cumulativeProbability = 0;

        for (int nextNode = 0; nextNode < nNodes; nextNode++) {
            if (!visited[nextNode]) {
                cumulativeProbability += probabilities[nextNode];
                if (cumulativeProbability >= rouletteWheel) {
                    return nextNode;
                }
            }
        }

        // In case of rounding errors
        return -1;
    }

    // Method to update pheromones based on ant tours
    private void updatePheromones(List<List<Integer>> antTours) {
        int nNodes = distances.length;

        // Decay existing pheromones
        for (int i = 0; i < nNodes; i++) {
            for (int j = 0; j < nNodes; j++) {
                pheromones[i][j] *= (1 - decay);
            }
        }

        // Update pheromones based on ant tours
        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);

            for (int i = 0; i < nNodes - 1; i++) {
                int fromNode = tour.get(i);
                int toNode = tour.get(i + 1);
                pheromones[fromNode][toNode] += 1.0 / tourLength;
                pheromones[toNode][fromNode] += 1.0 / tourLength;
            }
        }
    }

    // Method to calculate the length of a tour based on distances
    private double calculateTourLength(List<Integer> tour) {
        double length = 0;

        for (int i = 0; i < tour.size() - 1; i++) {
            int fromNode = tour.get(i);
            int toNode = tour.get(i + 1);
            length += distances[fromNode][toNode];
        }

        return length;
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        // Example usage:
        double[][] distances = {
                {0, 2, 3, 4},
                {2, 0, 5, 6},
                {3, 5, 0, 7},
                {4, 6, 7, 0}
        };

        int nAnts = 5;
        double decay = 0.1;
        double alpha = 1;
        double beta = 2;

        AntColony antColony = new AntColony(distances, nAnts, decay, alpha, beta);
        List<Integer> optimalTour = antColony.findOptimalTour();

        System.out.println("Optimal Tour: " + optimalTour);
    }
}
