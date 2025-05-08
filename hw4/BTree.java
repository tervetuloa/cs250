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
        
        boolean isUnderfull() {
            return keys.size() < (ORDER/2) - 1;
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
        if (!root.isLeaf() && root.keys.isEmpty()) {
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
            } else {
                return removeFromInternal(node, idx);
            }
        } else {
            idx = -idx - 1;
            if (node.isLeaf()) return false;
            
            if (node.children.get(idx).keys.size() < (ORDER/2)) {
                fillChild(node, idx);
                if (node.keys.size() < idx + 1) {
                    idx = node.keys.size() - 1;
                }
            }
            
            return remove(node.children.get(idx), num);
        }
    }

    private boolean removeFromInternal(Node node, int idx) {
        Node leftChild = node.children.get(idx);
        if (leftChild.keys.size() >= (ORDER/2)) {
            Integer pred = getMax(leftChild);
            Long timestamp = get(pred);
            node.keys.set(idx, pred);
            node.timestamps.set(idx, timestamp);
            return remove(leftChild, pred);
        } else {
            Node rightChild = node.children.get(idx+1);
            if (rightChild.keys.size() >= (ORDER/2)) {
                Integer succ = getMin(rightChild);
                Long timestamp = get(succ);
                node.keys.set(idx, succ);
                node.timestamps.set(idx, timestamp);
                return remove(rightChild, succ);
            } else {
                mergeNodes(node, idx);
                return remove(leftChild, node.keys.get(idx));
            }
        }
    }

    private void fillChild(Node parent, int idx) {
        if (idx > 0 && parent.children.get(idx-1).keys.size() >= (ORDER/2)) {
            borrowFromLeft(parent, idx);
        } 
        else if (idx < parent.children.size()-1 && 
                 parent.children.get(idx+1).keys.size() >= (ORDER/2)) {
            borrowFromRight(parent, idx);
        }
        else {
            if (idx > 0) {
                mergeNodes(parent, idx-1);
            } else {
                mergeNodes(parent, idx);
            }
        }
    }

    private void borrowFromLeft(Node parent, int idx) {
        Node child = parent.children.get(idx);
        Node sibling = parent.children.get(idx-1);
        
        child.keys.add(0, parent.keys.get(idx-1));
        child.timestamps.add(0, parent.timestamps.get(idx-1));
        
        parent.keys.set(idx-1, sibling.keys.remove(sibling.keys.size()-1));
        parent.timestamps.set(idx-1, sibling.timestamps.remove(sibling.timestamps.size()-1));
        
        if (!child.isLeaf()) {
            child.children.add(0, sibling.children.remove(sibling.children.size()-1));
        }
    }

    private void borrowFromRight(Node parent, int idx) {
        Node child = parent.children.get(idx);
        Node sibling = parent.children.get(idx+1);
        
        child.keys.add(parent.keys.get(idx));
        child.timestamps.add(parent.timestamps.get(idx));
        
        parent.keys.set(idx, sibling.keys.remove(0));
        parent.timestamps.set(idx, sibling.timestamps.remove(0));
        
        if (!child.isLeaf()) {
            child.children.add(sibling.children.remove(0));
        }
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

    private Integer getMin(Node node) {
        while (!node.isLeaf()) node = node.children.get(0);
        return node.keys.get(0);
    }

    private Integer getMax(Node node) {
        while (!node.isLeaf()) node = node.children.get(node.children.size() - 1);
        return node.keys.get(node.keys.size() - 1);
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