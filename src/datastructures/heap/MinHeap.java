package datastructures.heap;

@SuppressWarnings("unchecked")
public class MinHeap<T extends Comparable<T>> {

    private T[] heap;
    private int size = 0;

    public MinHeap(int capacity) {
        heap = (T[]) new Comparable[capacity];
    }

    public void add(T val) {
        if (size == heap.length) return;
        heap[size] = val;
        bubbleUp(size++);
    }

    public T peek() {
        return size == 0 ? null : heap[0];
    }

    public T poll() {
        if (size == 0) return null;
        T root = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        bubbleDown(0);
        return root;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void bubbleUp(int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (heap[idx].compareTo(heap[parent]) >= 0) break;
            swap(idx, parent);
            idx = parent;
        }
    }

    private void bubbleDown(int idx) {
        while (true) {
            int left = idx * 2 + 1;
            int right = idx * 2 + 2;
            int smallest = idx;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) smallest = left;
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) smallest = right;

            if (smallest == idx) break;
            swap(idx, smallest);
            idx = smallest;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
}
