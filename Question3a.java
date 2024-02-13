// Question 3
// a) You are developing a student score tracking system that keeps track of scores from different assignments. The 
// ScoreTracker class will be used to calculate the median score from the stream of assignment scores. The class should have
//  the following methods:
//  ScoreTracker() initializes a new ScoreTracker object.
//  void addScore(double score) adds a new assignment score to the data stream.
//  double getMedianScore() returns the median of all the assignment scores in the data stream. If the number of scores 
// is even, the median should be the average of the two middle scores.
// Input:
// ScoreTracker scoreTracker = new ScoreTracker();
// scoreTracker.addScore(85.5); // Stream: [85.5]
// scoreTracker.addScore(92.3); // Stream: [85.5, 92.3]
// scoreTracker.addScore(77.8); // Stream: [85.5, 92.3, 77.8]
// scoreTracker.addScore(90.1); // Stream: [85.5, 92.3, 77.8, 90.1]
// double median1 = scoreTracker.getMedianScore(); // Output: 88.9 (average of 90.1 and 85.5)
// scoreTracker.addScore(81.2); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2]
// scoreTracker.addScore(88.7); // Stream: [85.5, 92.3, 77.8, 90.1, 81.2, 88.7]
// double median2 = scoreTracker.getMedianScore(); // Output: 86.95 (average of 88.7 and 85.5)

import java.util.Collections;
import java.util.PriorityQueue;

class ScoreTracker {
    private PriorityQueue<Double> minHeap;
    private PriorityQueue<Double> maxHeap;

    public ScoreTracker() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Collections.reverseOrder()); // maxheap: [85.5, 77.8] becomes something like this
                                                                   // with collection.reverseOrder()
    }

    void addScore(double score) {
        if (maxHeap.isEmpty() || score < maxHeap.peek()) {
            maxHeap.add(score); // the firs value 85.5 will always be added to the max heap
                                // then it will always compare the other values to 85.5 and if its less it will
                                // add to max heap else it will go to min heap i.e. value > 85.5 =>[88.7, 92.3,
                                // 90.1]
            // System.out.println("maxheap: " + maxHeap);

        } else {
            minHeap.add(score);
            // System.out.println("minheap: " + minHeap);

            // values that will be in min heap: [88.7, 92.3, 90.1]

        }
        balanceHeap();
        // System.out.println("after balance");
        // System.out.println("maxheap: " + maxHeap);
        // System.out.println("minheap: " + minHeap);
    }

    public double getMedianScore() {
        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2; // same as comment in line 66-67
        } else {
            return minHeap.peek();// middle value if it is odd number data set
        }
    }

    public void balanceHeap() {
        // max heap should always be bigger or equal in size to min heap
        // as median means middle number in a set of data i.e [1,2,3,4,5] the middle
        // number is 3
        // or median is the average of two middle numbers if the data is set of odd
        // numbers i.e. [1,2,3,4,5,6] median == 3+4/2 => 3.5
        if (maxHeap.size() > minHeap.size()) {
            minHeap.add(maxHeap.poll());
            // so that median is always the lower half of max heap that is max
        } else if (minHeap.size() > maxHeap.size() + 1) {
            maxHeap.add(minHeap.poll());
            // to satisfy the condition of median same as when doing the if block just the
            // opposite case
        }
    }

    public static void main(String[] args) {
        ScoreTracker st = new ScoreTracker();

        st.addScore(85.5);
        st.addScore(92.3);
        st.addScore(77.8);

        double median1 = st.getMedianScore();
        System.out.println("Median 1: " + median1);

        st.addScore(90.1);
        st.addScore(81.2);
        st.addScore(88.7);

        double median2 = st.getMedianScore();
        System.out.println("Median 2: " + median2);
    }
}
