// b)
// You are given an integer n representing the total number of individuals. Each individual is identified by a unique ID 
// from 0 to n-1. The individuals have a unique secret that they can share with others.
// The secret-sharing process begins with person 0, who initially possesses the secret. Person 0 can share the secret with 
// any number of individuals simultaneously during specific time intervals. Each time interval is represented by a tuple (start, end) where start and end are non-negative integers indicating the start and end times of the interval.
// You need to determine the set of individuals who will eventually know the secret after all the possible secret-sharing intervals
// have occurred.
// Example:
// Input: n = 5, intervals = [(0, 2), (1, 3), (2, 4)], firstPerson = 0
// Output: [0, 1, 2, 3, 4]
// Explanation:
// In this scenario, we have 5 individuals labeled from 0 to 4.
// The secret-sharing process starts with person 0, who has the secret at time 0. At time 0, person 0 can share the secret with 
// any other person. Similarly, at time 1, person 0 can also share the secret. At time 2, person 0 shares the secret again, and 
// so on.
// Given the intervals [(0, 2), (1, 3), (2, 4)], we can observe that during these intervals, person 0 shares the secret with 
// every other individual at least once.
// Hence, after all the secret-sharing intervals, individuals 0, 1, 2, 3, and 4 will eventually know the secret.


import java.util.HashSet;
import java.util.Set;

public class Question2b {
    /*
     * according to question lets say 0 can share secret with only 2 and first
     * person is 0 that has a secret
     * then he needs to share the secret with everyone according to the question but
     * how does he do it ?
     * so we create a set for it so that the first person does not share the secret
     * with same person again and again
     * according to question {{0,2},{1,3},{2,4}} so first person shares his secret
     * first time with 2 in the first interval that is
     * first interval is intervals[0] not to confuse it with interval in the for
     * each loop
     * ;
     */

    public Set<Integer> shareSecretWithIndividuals(int n, int firstPerson, int intervals[][]) {

        Set<Integer> result = new HashSet<>(); // to prevent the repition of people who know the secret
        result.add(firstPerson);

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // System.out.println(start); // this gives something like [0,2][1,3] and [2,4]
            // System.out.println(end); // if you print the start and end together instead of doing it in different lines like i did


            // again going into loop to get the persons who the first person shares the secret in the first interval
            //since we know that start is 0 then and the start is interval[0] is also 0 so we know that the first person 0 shared secret with is 2
            // on the second iteration 2 has to share secret with another person [1,3] which should be something lik e[0,2][2,4],[2,1][1,3]
            //since 2 shares secret multiple times to fulfill the condition of output [0, 1, 2, 3, 4] ([2,1] is an assumption)
            //or else 1 would not be able to know the secret and 3 will not be able to know the secret simultaenously
            //so 1 gets added since condition is its either person is greater than start or less than end from first iteration i.e (0,2) then
            //on second iteration its (1,3)  then you have to get 1 to fulfill the above condition again loop runs and 4
            for (int person : interval) {
                if (start <= person && person <= end) {
                    result.add(person);
                    // System.out.println(result);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = { { 0, 2 }, { 1, 3 }, { 2, 4 } };
        int firstPerson = 0;

        Question2b q = new Question2b();
        Set<Integer> output = q.shareSecretWithIndividuals(n, firstPerson, intervals);
        System.out.println("output: " + output);

    }
}
