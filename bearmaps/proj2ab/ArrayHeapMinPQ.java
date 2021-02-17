package bearmaps.proj2ab;

/**
 * @author Zhiwei_Zhou
 * @create 2021-02-03 21:23
 */
import java.lang.reflect.Parameter;
import java.util.*;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.Stopwatch;
import com.sun.tools.javac.Main;
import edu.princeton.cs.algs4.StdRandom;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PQNode> minPQ;
    private HashMap<PQNode, Integer> index;

    private class PQNode implements Comparable<PQNode> {
        T key;
        double priority;

        public PQNode(T key, double priority) {
            this.key = key;
            this.priority = priority;
        }

        void setpriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PQNode o) {
            if(o == null) {
                return 0;
            }
            return Double.compare(this.priority, o.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PQNode pqNode = (PQNode) o;
            return Objects.equals(key, pqNode.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    public ArrayHeapMinPQ() {
        minPQ = new ArrayList<>();
        minPQ.add(null);
        index = new HashMap<>();
    }

    private int parent(int k) {
        return k/2;
    }

    private int leftChild(int k) {
        return k*2;
    }

    private int rightChild(int k) {
        return k*2 + 1;
    }

    private void swimup(int k) { //how to deal with equil problem
        if(minPQ.get(k).compareTo(minPQ.get(parent(k))) < 0) {
            swap(k, parent(k));
            swimup(parent(k));
        }
    }

    private void swimdown(int k) {  //how to deal with equil problem
        if(leftChild(k) <= size() && minPQ.get(k).compareTo(minPQ.get(leftChild(k))) > 0) {
            swap(k, leftChild(k));
            swimdown(leftChild(k));
        } else if(rightChild(k) <= size() && minPQ.get(k).compareTo(minPQ.get(rightChild(k))) > 0) {
            swap(k, rightChild(k));
            swimdown(rightChild(k));
        }
    }

    private void swap(int present, int goal) {
        PQNode p = minPQ.get(present);
        minPQ.set(present, minPQ.get(goal));
        minPQ.set(goal, p);
        index.put(minPQ.get(present), present);
        index.put(minPQ.get(goal), goal);
    }

    @Override
    public void add(T item, double priority) {
        PQNode next = new PQNode(item, priority);
        if(contains(next.key)) {
            throw new IllegalArgumentException("PQ already contains the item." + next.key);
        }
        minPQ.add(next);
        index.put(next, size());
        swimup(size());
    }

    @Override
    public boolean contains(T item) {
        PQNode p = new PQNode(item, 0.0);
        return index.containsKey(p);
    }

    @Override
    public T getSmallest() {
        if(size() == 0) {
            throw new NoSuchElementException("PQ contains nothing");
        }
        return minPQ.get(1).key;
    }

    @Override
    public T removeSmallest() {
        if(size() == 0) {
            throw new NoSuchElementException("PQ contains nothing");
        }
        swap(1, size());
        PQNode p = minPQ.remove(size());
        index.remove(p);
        swimdown(1);
        return p.key;
    }

    @Override
    public int size() {
        return minPQ.size()-1;
    }

    @Override
    public void changePriority(T item, double priority) {
        if(!contains(item)) throw new NoSuchElementException("PQ doesnt contain this item");
        PQNode p = new PQNode(item, priority);
        int a = index.get(p);
        minPQ.get(a).setpriority(priority);
        swimup(a);
        swimdown(a);
    }
}
