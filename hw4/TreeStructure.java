package cs250.hw4;

public interface TreeStructure {
    void insert(Integer num);
    Boolean remove(Integer num);
    Long get(Integer num);
    Integer findMaxDepth();
    Integer findMinDepth();
}