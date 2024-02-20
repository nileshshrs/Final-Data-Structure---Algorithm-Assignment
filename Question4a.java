// a)
// You are given a 2D grid representing a maze in a virtual game world. The grid is of size m x n and consists of 
// different types of cells:
// 'P' represents an empty path where you can move freely. 'W' represents a wall that you cannot pass through. 'S' 
// represents the starting point. Lowercase letters represent hidden keys. Uppercase letters represent locked doors.
// You start at the starting point 'S' and can move in any of the four cardinal directions (up, down, left, right) to 
// adjacent cells. However, you cannot walk through walls ('W').
// As you explore the maze, you may come across hidden keys represented by lowercase letters. To unlock a door 
// represented by an uppercase letter, you need to collect the corresponding key first. Once you have a key, you can 
// pass through the corresponding locked door.
// For some 1 <= k <= 6, there is exactly one lowercase and one uppercase letter of the first k letters of the English 
// alphabet in the maze. This means that there is exactly one key for each door, and one door for each key. The letters used to represent the keys and doors follow the English alphabet order.
// Your task is to find the minimum number of moves required to collect all the keys and reach the exit point. 
// The exit point is represented by 'E'. If it is impossible to collect all the keys and reach the exit, return -1.
// Example:
// Input: grid = [ ['S','P','P','P'], ['W','P','P','E'], ['P','b','W','P'], ['P','P','P','P'] ]
// Input: grid = ['SPaPP','WWWPW','bPAPB']
// Output: 8
// The goal is to Collect all key

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

class MazeSolver {

    // Function to find the minimum number of moves required to collect keys 'a' to
    // 'f'
    public static int minMovesToCollectKeys(char[][] grid) {
        int m = grid.length; // Number of rows in the grid
        int n = grid[0].length; // Number of columns in the grid
        int keysCount = 0; // Count of total keys in the maze
        Set<Character> targetKeys = new HashSet<>(); // Set to store target keys ('a' to 'f')
        for (char[] row : grid) {
            for (char cell : row) {
                if ('a' <= cell && cell <= 'f') {
                    keysCount++; // Increment the key count for each lowercase letter 'a' to 'f'
                    targetKeys.add(cell); // Add the lowercase letter to the set of target keys
                }
            }
        }

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // Possible movement directions

        Set<String> visited = new HashSet<>(); // Set to track visited states during BFS
        Queue<State> queue = new ArrayDeque<>(); // Queue for BFS

        // Find the starting position 'S' and initialize the BFS queue
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    queue.add(new State(i, j, 0, new HashSet<>())); // Add the starting state to the queue
                    visited.add(i + "-" + j + "-"); // Mark the starting state as visited
                    break;
                }
            }
        }

        // BFS loop
        while (!queue.isEmpty()) {
            State current = queue.poll(); // Dequeue the current state

            // Check if all keys 'a' to 'f' are collected
            if (current.collectedKeys.size() == keysCount) {
                return current.steps; // Return the minimum number of steps if all keys are collected
            }

            // Explore possible next states in all directions
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                // Check if the next position is valid and not a wall
                if (isValid(nx, ny, m, n) && grid[nx][ny] != 'W') {
                    char cell = grid[nx][ny];

                    // If the cell contains a new key 'a' to 'f', add it to the set of collected
                    // keys
                    if ('a' <= cell && cell <= 'f' && !current.collectedKeys.contains(cell)) {
                        Set<Character> newCollectedKeys = new HashSet<>(current.collectedKeys);
                        newCollectedKeys.add(cell);
                        String newState = nx + "-" + ny + "-" + newCollectedKeys;
                        if (!visited.contains(newState)) {
                            queue.add(new State(nx, ny, current.steps + 1, newCollectedKeys));
                            visited.add(newState);
                        }
                    }
                    // If the cell contains a locked door 'A' to 'F' and the corresponding key is
                    // collected, proceed
                    else if ('A' <= cell && cell <= 'F'
                            && current.collectedKeys.contains(Character.toLowerCase(cell))) {
                        String newState = nx + "-" + ny + "-" + current.collectedKeys;
                        if (!visited.contains(newState)) {
                            queue.add(new State(nx, ny, current.steps + 1, current.collectedKeys));
                            visited.add(newState);
                        }
                    }
                    // If the cell is a path or already collected key 'a' to 'f', proceed
                    else if (cell == 'P' || ('a' <= cell && cell <= 'f' && current.collectedKeys.contains(cell))) {
                        String newState = nx + "-" + ny + "-" + current.collectedKeys;
                        if (!visited.contains(newState)) {
                            queue.add(new State(nx, ny, current.steps + 1, current.collectedKeys));
                            visited.add(newState);
                        }
                    }
                }
            }
        }

        return -1; // Return -1 if it's impossible to collect all keys 'a' to 'f' and reach the
                   // exit
    }

    // Function to check if a position is valid within the grid
    private static boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    // Class representing the state of the game
    static class State {
        int x, y, steps;
        Set<Character> collectedKeys;

        // Constructor for creating a new state
        public State(int x, int y, int steps, Set<Character> collectedKeys) {
            this.x = x;
            this.y = y;
            this.steps = steps;
            this.collectedKeys = collectedKeys;
        }
    }

    // Main function for testing the maze solver
    public static void main(String[] args) {
        char[][] grid = {
                { 'S', 'P', 'a', 'P', 'P' },
                { 'W', 'W', 'W', 'P', 'W' },
                { 'b', 'P', 'B', 'P', 'C' }
        };

        int result = minMovesToCollectKeys(grid);
        System.out.println(result); // Output: 8
    }
}
