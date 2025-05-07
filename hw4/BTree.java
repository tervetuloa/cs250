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
            return children.size() == 0;
        }
    }

    public void insert(Integer num) {
        if (root == null) {
            root = new Node();
            root.keys.add(num);
            root.timestamps.add(System.nanoTime());
            return;
        }
        
        if (root.keys.size() >= ORDER) {
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
            while (i >= 0 && num < node.keys.get(i)) {
                i--;
            }
            node.keys.add(i+1, num);
            node.timestamps.add(i+1, timestamp);
        } else {
            while (i >= 0 && num < node.keys.get(i)) {
                i--;
            }
            i++;
            
            if (node.children.get(i).keys.size() >= ORDER) {
                splitChild(node, i);
                if (num > node.keys.get(i)) {
                    i++;
                }
            }
            
            insertNonFull(node.children.get(i), num, timestamp);
        }
    }
    
    private void splitChild(Node parent, int childIndex) {
        Node child = parent.children.get(childIndex);
        Node newChild = new Node();
        
        int mid = ORDER / 2;
        parent.keys.add(childIndex, child.keys.get(mid));
        parent.timestamps.add(childIndex, child.timestamps.get(mid));
        
        newChild.keys.addAll(child.keys.subList(mid+1, child.keys.size()));
        newChild.timestamps.addAll(child.timestamps.subList(mid+1, child.timestamps.size()));
        child.keys.subList(mid, child.keys.size()).clear();
        child.timestamps.subList(mid, child.timestamps.size()).clear();
        
        if (!child.isLeaf()) {
            newChild.children.addAll(child.children.subList(mid+1, child.children.size()));
            child.children.subList(mid+1, child.children.size()).clear();
        }
        
        parent.children.add(childIndex+1, newChild);
    }

    public Boolean remove(Integer num) {
        if (root == null) return false;
        return remove(root, num);
    }
    
    private boolean remove(Node node, Integer num) {
        int idx = Collections.binarySearch(node.keys, num);
        
        if (idx >= 0) {
            if (node.isLeaf()) {
                node.keys.remove(idx);
                node.timestamps.remove(idx);
                return true;
            }
            return false;
        } else {
            if (node.isLeaf()) return false;
            idx = -idx - 1;
            return remove(node.children.get(idx), num);
        }
    }

    public Long get(Integer num) {
        if (root == null) return null;
        Node current = root;
        while (true) {
            int idx = Collections.binarySearch(current.keys, num);
            if (idx >= 0) {
                return current.timestamps.get(idx);
            }
            if (current.isLeaf()) {
                return null;
            }
            current = current.children.get(-idx - 1);
        }
    }

    public Integer findMaxDepth() {
        return calculateDepth(root, true);
    }

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