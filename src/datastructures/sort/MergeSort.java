package datastructures.sort;

public class MergeSort {

    public static <T extends Comparable<T>> void sort(T[] arr) {
        if (arr == null || arr.length <= 1) return;
        @SuppressWarnings("unchecked")
        T[] tmp = (T[]) new Comparable[arr.length];
        sort(arr, tmp, 0, arr.length - 1);
    }

    private static <T extends Comparable<T>> void sort(T[] arr, T[] tmp, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) / 2;
        sort(arr, tmp, lo, mid);
        sort(arr, tmp, mid + 1, hi);
        merge(arr, tmp, lo, mid, hi);
    }

    private static <T extends Comparable<T>> void merge(T[] arr, T[] tmp, int lo, int mid, int hi) {
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (arr[i].compareTo(arr[j]) <= 0) tmp[k++] = arr[i++];
            else tmp[k++] = arr[j++];
        }
        while (i <= mid) tmp[k++] = arr[i++];
        while (j <= hi) tmp[k++] = arr[j++];
        for (int p = lo; p <= hi; p++) arr[p] = tmp[p];
    }
}
