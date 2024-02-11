

// You are the captain of a spaceship and you have been assigned a mission to explore a distant galaxy. Your spaceship is equipped with a set of engines, where 
// each engine represented by a block. Each engine requires a specific amount of time to be built and can only be built by one engineer.
// Your task is to determine the minimum time needed to build all the engines using the available engineers. The engineers can either work on building an engine or 
// split into two engineers, with each engineer sharing the workload equally. Both decisions incur a time cost.
// The time cost of splitting one engineer into two engineers is given as an integer split. Note that if two engineers split at the same time, they split in parallel
//  so the cost would be split.
// Your goal is to calculate the minimum time needed to build all the engines, considering the time cost of splitting engineers.
// Input: engines= [3, 4, 5, 2]
// Split cost (k)=2
// Output: 4
// Example:
// Imagine you have the list of engines: [3, 4, 5, 2] and the split cost is 2. Initially, there is only one engineer available.
// The optimal strategy is as follows:
// 1. The engineer splits into two engineers, increasing the total count to two. (Time: 2)
// 2. Each engineer takes one engine, with one engineer building the engine that requires 3 units of time and the other engineer building the engine that requires 4 units of time.
// 3. Once the engineer finishes building the engine that requires 3 units of time, the engineer splits into two, increasing the total count to three. (Time: 4)
// 4. Each engineer takes one engine, with two engineers building the engines that require 2 and 5 units of time, respectively.
// Therefore, the minimum time needed to build all the engines using optimal decisions on splitting engineers and assigning them to engines is 4 units.
// Note: The splitting process occurs in parallel, and the goal is to minimize the total time required to build all the engines using the available engineers while considering 
// the time cost of splitting.
class MinimumTimeToBuildEngines {

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        int n = engines.length;

        // dp[i][j] represents the minimum time to build the first i engines with j engineers
        int[][] dp = new int[n + 1][n + 1];

        // Initialize dp array with infinity
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }

        // Base case: building 0 engines with 0 engineers takes 0 time
        for (int j = 0; j <= n; j++) {
            dp[0][j] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // Option 1: Splitting engineers
                for (int k = 1; k <= i; k++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[k - 1][j - 1], sum(engines, k, i)) + splitCost);
                }
            }
        }

        return dp[n][n];
    }

    private static int sum(int[] engines, int start, int end) {
        int result = 0;
        for (int i = start - 1; i < end; i++) {
            result += engines[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;

        int result = minTimeToBuildEngines(engines, splitCost);
        System.out.println("Minimum time needed to build all engines: " + result);
    }
}

