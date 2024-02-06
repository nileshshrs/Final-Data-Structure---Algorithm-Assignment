import java.util.PriorityQueue;

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

public class Question1b {

    public int timeToBuildEngine(int[] engines, int splitCost) {

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // priority queue is just like queue but it removes the value from lowest to
        // highest, which is necessary for this to work
        // forgot what its called
        // using the for each loop to add engines to minHeap priority queue
        for (int engine : engines) {
            minHeap.add(engine);
        }

        // System.out.println(minHeap);
        int totalTime = 0; // we need to allocate as 0 as base cuz at the start no engine is built

        // according to question we have 4 engines being built so, we want to keep the
        // loop running till we have 1 engine left to build

        // so we use minHeap.size()>1

        while (minHeap.size() > 1) {
            int firstEngine = minHeap.poll();
            int secondEngine = minHeap.poll();

            int splitTime = (minHeap.size() > 0) ? splitCost : 0;
            // time is usually going to be 2 unless there is no engines
            // left to build

            // System.out.println(splitTime);

            // step time is basically when engineers split into 2 engineers then 3 and then
            // 4
            // whats happening is first 2 engins take 2, and 3 hrs to build so we are taking
            // maximum time it takes to complete the engines i.e. 5 then adding the 5 into
            // heap
            // then we have 4 and 5 engines and it becomes 7 hrs to complete
            // and then we have 5,7 note that 7 is not an engine but rather we are splitting
            // engineers to build the engine so it becomes 7 in overall since the last
            // engine being completed makes time 0
            // System.out.println(splitTime);

            int stepTime = Math.max(firstEngine, secondEngine) + splitTime;
            // System.out.println(stepTime);
            // System.out.println("firstEngine: " + firstEngine);
            // System.out.println("secondEngine: " + secondEngine);
            // System.out.println("splitTime: " + splitTime);
            // System.out.println("stepTime: " + stepTime);
            totalTime += stepTime;
            // System.out.println(totalTime);
            minHeap.add(stepTime);
            // System.out.println(minHeap);
        }

        // totalTime += minHeap.poll();

        return totalTime;
    }

    public static void main(String[] args) {
        Question1b q = new Question1b();
        int[] engines = { 3, 4, 5, 2 };
        int splitCost = 2;
        int result = q.timeToBuildEngine(engines, splitCost);
        System.out.println(result);
    }
}
