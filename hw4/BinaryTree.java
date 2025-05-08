package cs250.hw4;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree implements TreeStructure {
    private Node root;
    private int size;

    private class Node {
        Integer value;
        Long timestamp;
        Node left;
        Node right;

        Node(Integer value, Long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    @Override
    public void insert(Integer num) {
        root = insert(root, num, System.nanoTime());
    }

    private Node insert(Node node, Integer num, Long timestamp) {
        if (node == null) {
            size++;
            return new Node(num, timestamp);
        }

        if (num < node.value) {
            node.left = insert(node.left, num, timestamp);
        } else if (num > node.value) {
            node.right = insert(node.right, num, timestamp);
        }
        return node;
    }

    @Override
    public Boolean remove(Integer num) {
        int initialSize = size;
        root = remove(root, num);
        return size < initialSize;
    }

    private Node remove(Node node, Integer num) {
        if (node == null) return null;

        if (num < node.value) {
            node.left = remove(node.left, num);
        } else if (num > node.value) {
            node.right = remove(node.right, num);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            node.value = minValue(node.right);
            node.timestamp = get(node.value);
            node.right = remove(node.right, node.value);
        }
        return node;
    }

    private Integer minValue(Node node) {
        int minValue = node.value;
        while (node.left != null) {
            minValue = node.left.value;
            node = node.left;
        }
        return minValue;
    }

    @Override
    public Long get(Integer num) {
        Node node = get(root, num);
        return node == null ? null : node.timestamp;
    }

    private Node get(Node node, Integer num) {
        if (node == null) return null;
        if (num.equals(node.value)) return node;
        return num < node.value ? get(node.left, num) : get(node.right, num);
    }

    @Override
    public Integer findMaxDepth() {
        return maxDepth(root);
    }

    private int maxDepth(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(maxDepth(node.left), maxDepth(node.right));
    }

    @Override
    public Integer findMinDepth() {
        if (root == null) return 0;
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 1;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                if (current.left == null && current.right == null) {
                    return depth;
                }
                if (current.left != null) queue.add(current.left);
                if (current.right != null) queue.add(current.right);
            }
            depth++;
        }
        return depth;
    }
}