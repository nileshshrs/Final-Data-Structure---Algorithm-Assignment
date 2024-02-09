import java.util.LinkedList;
import java.util.List;

 class Tree {
    public static class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    // Method to create a BST
    Node createBST(Node root, int data) {
        if (root == null) return new Node(data);
        if (data < root.data) {
            root.left = createBST(root.left, data);
        } else if (data > root.data) {
            root.right = createBST(root.right, data);
        } else {
            System.out.println("Duplicate entry of " + data);
        }
        return root;
    }

    // Inorder traversal to find the closest values
    private void findClosestValues(Node root, double target, int k, LinkedList<Integer> closest) {
        if (root == null) return;

        findClosestValues(root.left, target, k, closest);

        // If we have more than k elements, check if we should remove the farthest
        if (closest.size() == k) {
            if (Math.abs(target - closest.peekFirst()) > Math.abs(target - root.data)) {
                closest.removeFirst();
            } else {
                // If the current element is not closer than the farthest in the list, stop the process
                return;
            }
        }
        closest.add(root.data);

        findClosestValues(root.right, target, k, closest);
    }

    // Public method to initiate the closest value search
    public List<Integer> findClosest(Node root, double target, int k) {
        LinkedList<Integer> closest = new LinkedList<>();
        findClosestValues(root, target, k, closest);
        return closest;
    }

    public static void main(String[] args) {
        Tree bst = new Tree();
        Node root = null;
        // Creating the BST
        root = bst.createBST(root, 40);
        root = bst.createBST(root, 30);
        root = bst.createBST(root, 20);
        root = bst.createBST(root, 50);
        root = bst.createBST(root, 60);
        root = bst.createBST(root, 10);
        root = bst.createBST(root, 35);

        double target = 33;
        int k = 3;
        List<Integer> closestValues = bst.findClosest(root, target, k);
        System.out.println("Closest values to " + target + " are: " + closestValues);
    }
}