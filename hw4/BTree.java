package cs250.hw4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTree implements TreeStructure {
    private static final int ORDER = 64;
    private Node root;

    private class Node {
        List<Integer> keys = new ArrayList<>();
        List<Long> timestamps = new ArrayList<>();
        List<Node> children = new ArrayList<>();

        boolean isLeaf() {
            return children.isEmpty();
        }
    }

    @Override
    public void insert(Integer num) {
        if (root == null) {
            root = new Node();
            root.keys.add(num);
            root.timestamps.add(System.nanoTime());
            return;
        }

        if (root.keys.size() == ORDER - 1) {
            Node newRoot = new Node();
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, num, System.nanoTime());
    }

    private void insertNonFull(Node node, Integer num, Long timestamp) {
        int i = node.keys.size() - 1;

        if (node.isLeaf()) {
            while (i >= 0 && num < node.keys.get(i)) i--;
            node.keys.add(i + 1, num);
            node.timestamps.add(i + 1, timestamp);
        } else {
            while (i >= 0 && num < node.keys.get(i)) i--;
            i++;
            
            if (node.children.get(i).keys.size() == ORDER - 1) {
                splitChild(node, i);
                if (num > node.keys.get(i)) i++;
            }
            insertNonFull(node.children.get(i), num, timestamp);
        }
    }

    private void splitChild(Node parent, int childIndex) {
        Node child = parent.children.get(childIndex);
        Node newChild = new Node();

        int medianIndex = (ORDER - 1) / 2;
        parent.keys.add(childIndex, child.keys.get(medianIndex));
        parent.timestamps.add(childIndex, child.timestamps.get(medianIndex));

        newChild.keys.addAll(child.keys.subList(medianIndex + 1, child.keys.size()));
        newChild.timestamps.addAll(child.timestamps.subList(medianIndex + 1, child.timestamps.size()));
        child.keys.subList(medianIndex, child.keys.size()).clear();
        child.timestamps.subList(medianIndex, child.timestamps.size()).clear();

        if (!child.isLeaf()) {
            newChild.children.addAll(child.children.subList(medianIndex + 1, child.children.size()));
            child.children.subList(medianIndex + 1, child.children.size()).clear();
        }

        parent.children.add(childIndex + 1, newChild);
    }

    @Override
    public Boolean remove(Integer num) {
        if (root == null) return false;
        boolean removed = remove(root, num);
        if (root.keys.isEmpty() && !root.isLeaf()) {
            root = root.children.get(0);
        }
        return removed;
    }

    private boolean remove(Node node, Integer num) {
        int idx = Collections.binarySearch(node.keys, num);
        
        if (idx >= 0) {
            if (node.isLeaf()) {
                node.keys.remove(idx);
                node.timestamps.remove(idx);
                return true;
            }
            
            Node predecessorChild = node.children.get(idx);
            if (predecessorChild.keys.size() >= (ORDER/2)) {
                Integer predecessor = getMax(predecessorChild);
                Long timestamp = get(predecessor);
                node.keys.set(idx, predecessor);
                node.timestamps.set(idx, timestamp);
                return remove(predecessorChild, predecessor);
            } else {
                Node successorChild = node.children.get(idx+1);
                if (successorChild.keys.size() >= (ORDER/2)) {
                    Integer successor = getMin(successorChild);
                    Long timestamp = get(successor);
                    node.keys.set(idx, successor);
                    node.timestamps.set(idx, timestamp);
                    return remove(successorChild, successor);
                } else {
                    mergeNodes(node, idx);
                    return remove(predecessorChild, num);
                }
            }
        }
        
        if (node.isLeaf()) return false;
        idx = -idx - 1;
        return remove(node.children.get(idx), num);
    }

    private Integer getMin(Node node) {
        while (!node.isLeaf()) node = node.children.get(0);
        return node.keys.get(0);
    }

    private Integer getMax(Node node) {
        while (!node.isLeaf()) node = node.children.get(node.children.size() - 1);
        return node.keys.get(node.keys.size() - 1);
    }

    private void mergeNodes(Node parent, int idx) {
        Node left = parent.children.get(idx);
        Node right = parent.children.get(idx+1);
        
        left.keys.add(parent.keys.get(idx));
        left.timestamps.add(parent.timestamps.get(idx));
        left.keys.addAll(right.keys);
        left.timestamps.addAll(right.timestamps);
        
        if (!left.isLeaf()) {
            left.children.addAll(right.children);
        }
        
        parent.keys.remove(idx);
        parent.timestamps.remove(idx);
        parent.children.remove(idx+1);
    }

    @Override
    public Long get(Integer num) {
        Node current = root;
        while (current != null) {
            int idx = Collections.binarySearch(current.keys, num);
            if (idx >= 0) return current.timestamps.get(idx);
            if (current.isLeaf()) return null;
            current = current.children.get(-idx - 1);
        }
        return null;
    }

    @Override
    public Integer findMaxDepth() {
        return calculateDepth(root, true);
    }

    @Override
    public Integer findMinDepth() {
        return calculateDepth(root, false);
    }

    private Integer calculateDepth(Node node, boolean isMax) {
        if (node == null) return 0;
        if (node.isLeaf()) return 1;
        
        int result = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Node child : node.children) {
            int childDepth = calculateDepth(child, isMax);
            result = isMax ? Math.max(result, childDepth) : Math.min(result, childDepth);
        }
        return result + 1;
    }
}