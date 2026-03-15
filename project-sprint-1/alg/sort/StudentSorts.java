package dsa.lab.alg.sort;

import dsa.lab.model.Record;

public final class StudentSorts {
    private StudentSorts() { }

    // Cutoff for small ranges (16 was pretty good, 32 was hit or miss)
    private static final int CUTOFF = 16;


       // Public factory methods
    public static SortAlgorithm insertionSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "InsertionSort"; }

            @Override public void sort(Record[] a) {
                // Handle null / trivial arrays
                if (a == null || a.length < 2) return;
                // Use shared insertion helper on the whole array
                insertion(a, 0, a.length - 1);
            }
        };
    }

    public static SortAlgorithm mergeSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "MergeSort"; }

            @Override public void sort(Record[] a) {
                if (a == null || a.length < 2) return;

                // Allocate single auxiliary array once
                Record[] aux = new Record[a.length];
                mergeSort(a, aux, 0, a.length - 1);
            }

            // Top-down mergesort with insertion cutoff and merge-skip optimization
            private void mergeSort(Record[] a, Record[] aux, int lo, int hi) {
                // small ranges -> insertion sort (faster for tiny arrays)
                if (hi - lo <= CUTOFF) {
                    insertion(a, lo, hi);
                    return;
                }

                int mid = lo + ((hi - lo) >>> 1);
                mergeSort(a, aux, lo, mid);
                mergeSort(a, aux, mid + 1, hi);

                // If already ordered, skip the merge
                if (a[mid].key() <= a[mid + 1].key()) return;

                merge(a, aux, lo, mid, hi);
            }

            // Merge using a single aux buffer
            private void merge(Record[] a, Record[] aux, int lo, int mid, int hi) {
                // copy the relevant range into aux
                System.arraycopy(a, lo, aux, lo, hi - lo + 1);

                int i = lo;        // pointer into left half (aux[lo..mid])
                int j = mid + 1;   // pointer into right half (aux[mid+1..hi])

                for (int k = lo; k <= hi; k++) {
                    if (i > mid) {
                        // left exhausted, take from right
                        a[k] = aux[j++];
                    } else if (j > hi) {
                        // right exhausted, take from left
                        a[k] = aux[i++];
                    } else {
                        // compare left and right first element (cache keys implicitly)
                        if (aux[i].key() <= aux[j].key()) {
                            a[k] = aux[i++];
                        } else {
                            a[k] = aux[j++];
                        }
                    }
                }
            }
        };
    }

    public static SortAlgorithm quickSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "QuickSort"; }

            @Override public void sort(Record[] a) {
                if (a == null || a.length < 2) return;
                quick3(a, 0, a.length - 1);
            }

            // Optimized 3-way quicksort:
            // - median-of-three pivot selection to avoid degenerate partitions on sorted inputs
            // - Dijkstra 3-way partition to handle duplicates fast
            // - insertion cutoff for tiny partitions
            // - tail-recursion elimination: recurse on the smaller partition and iterate on the larger
            private void quick3(Record[] a, int lo, int hi) {
                while (lo < hi) {
                    // small range -> insertion sort
                    if (hi - lo <= CUTOFF) {
                        insertion(a, lo, hi);
                        return;
                    }

                    // median-of-three: pick good pivot to avoid worst-case patterns
                    int mid = lo + ((hi - lo) >>> 1);
                    if (a[mid].key() < a[lo].key()) swap(a, mid, lo);
                    if (a[hi].key()  < a[lo].key()) swap(a, hi,  lo);
                    if (a[hi].key()  < a[mid].key()) swap(a, hi, mid);
                    // Now a[lo] <= a[mid] <= a[hi]; move median to lo to act as pivot
                    swap(a, mid, lo);
                    int pivotKey = a[lo].key();

                    // 3-way partition (Dijkstra)
                    int lt = lo;        // a[lo..lt-1] < pivot
                    int i  = lo + 1;    // a[lt..i-1] == pivot
                    int gt = hi;        // a[gt+1..hi] > pivot

                    while (i <= gt) {
                        int curKey = a[i].key();
                        if (curKey < pivotKey) {
                            swap(a, lt++, i++);
                        } else if (curKey > pivotKey) {
                            swap(a, i, gt--);
                        } else {
                            i++;
                        }
                    }

                    // Now: a[lo..lt-1] < pivot, a[lt..gt] == pivot, a[gt+1..hi] > pivot
                    // Recurse on smaller partition first to ensure O(log n) stack depth on average.
                    int leftSize  = lt - lo;
                    int rightSize = hi - gt;

                    if (leftSize < rightSize) {
                        quick3(a, lo, lt - 1);  // sort left (smaller) partition
                        lo = gt + 1;            // iterate to sort right (larger) partition
                    } else {
                        quick3(a, gt + 1, hi);  // sort right (smaller) partition
                        hi = lt - 1;            // iterate to sort left (larger) partition
                    }
                }
            }

            private void swap(Record[] a, int i, int j) {
                Record tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        };
    }

       // Shared helpers

    // Insertion sort for a range [lo..hi] (inclusive)
    // Uses cached key in the inner loop for fewer method calls
    private static void insertion(Record[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            Record cur = a[i];
            int curKey = cur.key();
            int j = i - 1;
            // Shift elements greater than curKey one position right to open slot for cur
            while (j >= lo && a[j].key() > curKey) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = cur;
        }
    }
}