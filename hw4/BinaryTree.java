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

    public void insert(Integer num) {
        root = insert(root, num, System.currentTimeMillis());
    }

    private Node insert(Node node, Integer num, Long timestamp) {
        if (node == null) {
            size++;
            return new Node(num, timestamp);
        }

        if (num <= node.value) {
            node.left = insert(node.left, num, timestamp);
        } else {
            node.right = insert(node.right, num, timestamp);
        }
        return node;
    }

    public Boolean remove(Integer num) {
        int initialSize = size;
        root = remove(root, num);
        return size != initialSize;
    }

    private Node remove(Node node, Integer num) {
        if (node == null) return null;

        if (num < node.value) {
            node.left = remove(node.left, num);
        } else if (num > node.value) {
            node.right = remove(node.right, num);
        } else {
            if (node.right == null) {
                size--;
                return node.left;
            } else if (node.left == null) {
                size--;
                return node.right;
            }

            node.value = minValue(node.right);
            node.right = remove(node.right, node.value);
        }
        return node;
    }

    private Integer minValue(Node node) {
        if (node.left == null) return node.value;
        return minValue(node.left);
    }

    public Long get(Integer num) {
        Node node = get(root, num);
        return node == null ? -1L : node.timestamp;
    }

    private Node get(Node node, Integer num) {
        if (node == null) return null;
        if (num.equals(node.value)) return node;
        if (num < node.value) return get(node.right, num);
        return get(node.left, num);
    }

    public Integer findMaxDepth() {
        if (root == null) return -1;
        return maxDepth(root) - 1;
    }

    private int maxDepth(Node node) {
        if (node == null) return 0;
        return Math.max(maxDepth(node.left), maxDepth(node.right));
    }

    public Integer findMinDepth() {
        if (root == null) return 0;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;
        
        while (!queue.isEmpty()) {
            depth++;
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                if (current.left == null || current.right == null) {
                    return depth;
                }
                queue.add(current.left);
                queue.add(current.right);
            }
        }
        return depth;
    }
}