// a)
// You are the manager of a clothing manufacturing factory with a production line of super sewing machines. The
//  production line consists of n super sewing machines placed in a line. Initially, each sewing machine has 
// a certain number of dresses or is empty.
// For each move, you can select any m (1 <= m <= n) consecutive sewing machines on the production line and 
// pass one dress from each selected sewing machine to its adjacent sewing machine simultaneously.
// Your goal is to equalize the number of dresses in all the sewing machines on the production line. 
// You need to determine the minimum number of moves required to achieve this goal. If it is not possible 
// to equalize the number of dresses, return -1.
// Input: [2, 1, 3, 0, 2]
// Output: 5
// Example 1:
// Imagine you have a production line with the following number of dresses in each sewing machine: 
// [2, 1, 3, 0, 2]. The production line has 5 sewing machines.
// Here's how the process works:
// 1. Initial state: [2, 1, 3, 0, 2]
// 2. Move 1: Pass one dress from the second sewing machine to the first sewing machine, resulting in [2, 2, 2, 0, 2]
// 3. Move 2: Pass one dress from the second sewing machine to the first sewing machine, resulting in [3, 1, 2, 0, 2]
// 4. Move 3: Pass one dress from the third sewing machine to the second sewing machine, resulting in [3, 2, 1, 0, 2]
// 5. Move 4: Pass one dress from the third sewing machine to the second sewing machine, resulting in [3, 3, 0, 0, 2]
// 6. Move 5: Pass one dress from the fourth sewing machine to the third sewing machine, resulting in [3, 3, 1, 0, 1]
// After these 5 moves, the number of dresses in each sewing machine is equalized to 1. Therefore, the minimum number of 
// moves required to equalize the number of dresses is 5.

public class Question2a {

    // input as an arguements is dresses here
    // machines we have is 5
    // according to what question says we can pass a dress from 1 to 5 in an
    // iterative way so 1 => 2, 2=>3 and so on till 5 since (1<=m<=n)
    // n is number of sewing machines i.e. 5 and m is moves
    public int movesToEqualize(int[] input) {
        int totaldresses = 0; // this is required so to help check if we can make all the sewing machines have
                              // equal number of dresses
        int n = input.length;

        for (int dress : input) {
            totaldresses += dress; // foreach loop to get total number of dresses out of the array of dresses
        }

        if (totaldresses % n != 0) {
            return -1; // this is needed so we know if the dresses can be divided equally
            // into machines if it gives -1 that means there is no optimal solution
        }

        // the code block after if block will not run but if we run it it will result
        // after commenting the if block but the output wont be 5

        int moves = 0;
        int target = totaldresses / n;

        for (int dress : input) {
            double difference = dress - target;// to make sure if the work divided within each machine is equal can be
                                               // negative, in this case we turn it into positive i guess ?
            difference = Math.ceil(difference); // to get
            System.out.println("dress before change " + dress);
            System.out.println(dress + "-" + target + "=" + difference);
            moves += Math.abs(difference);
        }

        return moves;
    }

    public static void main(String[] args) {
        int[] dresses = { 2, 1, 3, 0, 3 }; // if you add another 3 then it would be divided equally i think with results
                                           // being {0,1,0,2,1,1} for the optimal solution though it feels iffy ish~~~
                                           // with moves required being 6
        Question2a q = new Question2a();
        int result = q.movesToEqualize(dresses);

        System.out.println(result);// result is 6 instead of 5 because totaldress % n is not equal to 0
    }

}
