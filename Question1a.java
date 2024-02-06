// a)
// You are a planner working on organizing a series of events in a row of n venues. Each venue can be decorated with one of the k available themes. However, 
// adjacent venues should not have the same theme. The cost of decorating each venue with a certain theme varies.
// The costs of decorating each venue with a specific theme are represented by an n x k cost matrix. For example, costs [0][0] 
// represents the cost of decorating venue 0 with theme 0, and costs[1][2] represents the cost of decorating venue 1 with theme 2. Your task is 
// to find the minimum cost to decorate all the venues while adhering to the adjacency constraint.
// For example, given the input costs = [[1, 5, 3], [2, 9, 4]], the minimum cost to decorate all the venues is 5. One possible arrangement is decorating venue 0 with theme 0 and venue 1 with theme 2, resulting in a minimum cost of 1 + 4 = 5. Alternatively, decorating venue 0 with theme 2 and venue 1 with theme 0 also yields a minimum cost of 3 + 2 = 5.
// Write a function that takes the cost matrix as input and returns the minimum cost to decorate all the venues while satisfying the adjacency constraint.
// Please note that the costs are positive integers.
// Example: Input: [[1, 3, 2], [4, 6, 8], [3, 1, 5]] Output: 7
// Explanation: Decorate venue 0 with theme 0, venue 1 with theme 1, and venue 2 with theme 0. Minimum cost: 1 + 6 + 1 = 7.

public class Question1a {
    public int calcuateDecorationCost(int cost[][]) {
        int n = cost.length; // assuming that someone does not send a null value we can say that n is the
                             // lengt of rows of venue

        // same way k is themes and n x k is cost for applying a theme on a venue
        // so assuming this we get that k = cost[0].length assumiing one venue can be
        // appied with multiple themes and has different cost

        return calculateMinCost(cost, n - 1, -1); // calling the calculate min cost function
        // passing n-1 because n is the lenght and array's index starts with 0 so if the
        // length is 3 then array is something like [0,1,2] showing this to visualize
        // how array is indexed not to be mistaken with value inside the array
        // passing -1 as theme because at the start we assume that no theme is selected
        // for venue
    }

    public int calculateMinCost(int[][] cost, int venue, int theme) {
        // making sure that venue does not go below 0

        // you can also add a validation for checking if costs sent is null or themes
        // are less than 0
        if (venue < 0) { // (venu<0 || cost.length== null || cost[0].length==0) but i'm assuming data
                         // cannot be null

            return 0;
        }

        int minCost = Integer.MAX_VALUE; // setting maximum possible value for integer that is possible
        for (int i = 0; i < cost[venue].length; i++) {
            // running a loop here
            // (theme == -1 || theme != i) this will assume that theme ==-1 and theme !=i
            // are the same so loop will run no matter what
            if (theme == -1 || theme != i) {
                int venueCost = cost[venue][i];
                // will only give the the last row of the array or arrays, that is [3,1,5] and
                // this will serve as the base case to going through recursion after the first
                // base case being -1 as no theme was selected
                int recursiveCost = calculateMinCost(cost, venue - 1, i);// using recursion and passing the venue as 2
                minCost = Math.min(minCost, venueCost + recursiveCost);
                // then 1 then 0 and i as 0, 1,2 to calculate
                // the min cost of [3,1,5] i.e. 1 and so on untill the recursion ends when the
                // counter hits 0

                // System.out.println(minCost);
            }
        }

        return minCost;// returning minimum cost
    }

    public static void main(String[] args) {
        Question1a q = new Question1a();
        int[][] costs = {
                { 1, 3, 2 },
                { 4, 6, 8 },
                { 3, 1, 5 },
                // { 9, 8, 5 } // adding this should give me a min cost of 12
        };
        System.out.println(q.calcuateDecorationCost(costs)); // printing min cost
    }
}