// Implement Kruskal algorithm and priority queue using minimum heap

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Define a class to represent an edge in the graph
class Edge implements Comparable<Edge> {
    int source, destination, weight;

    // Constructor to initialize the edge with source, destination, and weight
    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    // Implement the Comparable interface to compare edges based on their weights
    @Override
    public int compareTo(Edge e) {
        return Integer.compare(this.weight, e.weight);
    }
}

// Define the KruskalAlgorithm class
class KruskalAlgorithm {

    // Find operation in Union-Find data structure
    public static int find(int[] parent, int x) {
        // If the current element is not the root, recursively find the root
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    // Union operation in Union-Find data structure
    private static void union(int[] parent, int[] rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);

        // Merge two sets based on their ranks
        if (rootX != rootY) {
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootX] = rootY;
                rank[rootY]++;
            }
        }
    }

    // Kruskal's algorithm to find the Minimum Spanning Tree
    public static List<Edge> Kruskal(List<Edge> edges, int vertices) {

        // List to store the edges in the Minimum Spanning Tree
        List<Edge> minimumSpanningTree = new ArrayList<>();
        
        // Sort the edges in ascending order based on weights
        Collections.sort(edges);
        
        // Initialize the Union-Find data structure
        int parent[] = new int[vertices];
        int rank[] = new int[vertices];

        for (int i = 0; i < vertices; i++) {
            parent[i] = i;  // Each vertex is initially its own parent
            rank[i] = 0;    // Initialize the rank of each set to 0
        }

        // Iterate through the sorted edges
        for (Edge edge : edges) {
            int rootSrc = find(parent, edge.source);
            int rootDest = find(parent, edge.destination);

            // Check if adding the edge would create a cycle
            if (rootSrc != rootDest) {
                minimumSpanningTree.add(edge);  // Add the edge to the Minimum Spanning Tree
                union(parent, rank, rootSrc, rootDest);  // Union the sets of source and destination vertices
            }
        }

        // Return the Minimum Spanning Tree
        return minimumSpanningTree;
    }

    // Main method to test the Kruskal's algorithm
    public static void main(String[] args) {
        // Create a sample graph with edges and vertices
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 3));
        edges.add(new Edge(1, 2, 2));
        edges.add(new Edge(1, 3, 1));
        edges.add(new Edge(2, 3, 5));

        int vertices = 5;  // Number of vertices in the graph

        // Call the Kruskal algorithm to find the Minimum Spanning Tree
        List<Edge> minimumSpanningTree = Kruskal(edges, vertices);

        // Print the edges in the Minimum Spanning Tree
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.source + " - " + edge.destination + " : " + edge.weight);
        }
    }
}

