// You are provided with balanced binary tree with the target value k. return x number of values that are closest to the 
// given target k. provide solution in O(n)
// Note: You have only one set of unique values x in binary search tree that are closest to the target.
// Input: 

// K=3.8
// x=2
// Output: 3,4

import java.util.LinkedList;
import java.util.List;

// Definition of a binary tree and methods to create a BST and find closest values
class Tree {
    // Definition of a Node in the binary tree
    public static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    // Method to create a Binary Search Tree (BST)
    Node createBST(Node root, int data) {
        // If the tree is empty, create a new node with the given data
        if (root == null)
            return new Node(data);
        
        // If the data is less than the root's data, go to the left subtree
        if (data < root.data) {
            root.left = createBST(root.left, data);
        } 
        // If the data is greater than the root's data, go to the right subtree
        else if (data > root.data) {
            root.right = createBST(root.right, data);
        } 
        // If the data is a duplicate, print a message
        else {
            System.out.println("Duplicate entry of " + data);
        }
        return root;
    }

    // Inorder traversal to find the closest values to a target value
    private void findClosestValues(Node root, double target, int k, LinkedList<Integer> closest) {
        // Base case: If the current node is null, return
        if (root == null)
            return;

        // Recursively traverse the left subtree
        findClosestValues(root.left, target, k, closest);

        // If the list has reached its capacity of k elements, check if the farthest element should be removed
        if (closest.size() == k) {
            if (Math.abs(target - closest.peekFirst()) > Math.abs(target - root.data)) {
                closest.removeFirst();
            } else {
                // If the current element is not closer than the farthest in the list, stop the process
                return;
            }
        }
        // Add the current node's data to the list
        closest.add(root.data);

        // Recursively traverse the right subtree
        findClosestValues(root.right, target, k, closest);
    }

    // Public method to initiate the closest value search
    public List<Integer> findClosest(Node root, double target, int k) {
        // Create a linked list to store the closest values
        LinkedList<Integer> closest = new LinkedList<>();
        // Call the private helper method to find closest values
        findClosestValues(root, target, k, closest);
        return closest;
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        // Example usage:
        Tree tree = new Tree();
        Node root = null;

        int[] values = { 4, 2, 5, 1, 3 };

        // Create a BST with the given values
        for (int value : values) {
            root = tree.createBST(root, value);
        }

        // Specify the target value and the number of closest values to find
        double target = 3.8;
        int k = 2;

        // Find and print the closest values to the target value
        List<Integer> closestValues = tree.findClosest(root, target, k);
        System.out.println(closestValues);
    }
}
