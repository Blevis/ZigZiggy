package dsa.lab.alg.sort;

import dsa.lab.model.Record;

public final class StudentSorts {
    private StudentSorts() { }

    // Cutoff for small ranges (experiment between 16..32)
    private static final int CUTOFF = 32;

    /* ---------------------------
       Public factory methods
       --------------------------- */

    public static SortAlgorithm insertionSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "InsertionSort"; }

            @Override public void sort(Record[] a) {
                if (a == null || a.length < 2) return;
                insertion(a, 0, a.length - 1);
            }
        };
    }

    public static SortAlgorithm mergeSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "MergeSort"; }

            @Override public void sort(Record[] a) {
                if (a == null || a.length < 2) return;
                final int n = a.length;

                // single auxiliary array
                Record[] aux = new Record[n];

                // 1) initial insertion-sort on small blocks (reduces comparison cost later)
                for (int lo = 0; lo < n; lo += CUTOFF) {
                    int hi = Math.min(n - 1, lo + CUTOFF - 1);
                    insertion(a, lo, hi);
                }

                // 2) bottom-up iterative merge passes, doubling width each time
                Record[] src = a;
                Record[] dest = aux;
                for (int width = CUTOFF; width < n; width <<= 1) {
                    for (int lo = 0; lo < n; lo += (width << 1)) {
                        int mid = Math.min(lo + width, n);
                        int hi  = Math.min(lo + (width << 1), n);

                        if (mid >= hi) {
                            // nothing to merge, just copy remaining run
                            System.arraycopy(src, lo, dest, lo, hi - lo);
                            continue;
                        }

                        // If already ordered, copy straight through (huge win on nearly-sorted)
                        if (src[mid - 1].key() <= src[mid].key()) {
                            System.arraycopy(src, lo, dest, lo, hi - lo);
                            continue;
                        }

                        // merge src[lo:mid) and src[mid:hi) into dest[lo:hi)
                        mergeInto(src, dest, lo, mid, hi);
                    }

                    // swap roles
                    Record[] tmp = src;
                    src = dest;
                    dest = tmp;
                }

                // If final sorted array is not in original 'a', copy back
                if (src != a) {
                    System.arraycopy(src, 0, a, 0, n);
                }
            }

            // merge from 'src' into 'dest' using galloping for long runs
            private void mergeInto(Record[] src, Record[] dest, int lo, int mid, int hi) {
                int i = lo, j = mid, k = lo;

                while (i < mid && j < hi) {
                    if (src[i].key() <= src[j].key()) {
                        // gallop: bulk-copy the run from the left half that stays <= src[j]
                        int bound = gallopRight(src, src[j].key(), i, mid);
                        int len = bound - i;
                        System.arraycopy(src, i, dest, k, len);
                        k += len; i = bound;
                    } else {
                        // gallop: bulk-copy the run from the right half that stays < src[i]
                        int bound = gallopRight(src, src[i].key() - 1, j, hi);
                        int len = bound - j;
                        System.arraycopy(src, j, dest, k, len);
                        k += len; j = bound;
                    }
                }

                // copy leftovers
                if (i < mid) {
                    System.arraycopy(src, i, dest, k, mid - i);
                } else if (j < hi) {
                    System.arraycopy(src, j, dest, k, hi - j);
                }
            }

            // exponential search then binary search:
            // returns the first index in src[from..to) where key() > pivot
            private int gallopRight(Record[] src, int pivot, int from, int to) {
                int step = 1;
                int hi = from;
                while (hi < to && src[hi].key() <= pivot) {
                    from = hi;
                    hi += step;
                    step <<= 1;
                }
                // binary search in [from, min(hi, to))
                int lo = from, bound = Math.min(hi, to);
                while (lo < bound) {
                    int m = lo + ((bound - lo) >>> 1);
                    if (src[m].key() <= pivot) lo = m + 1;
                    else bound = m;
                }
                return lo;
            }
        };
    }

    public static SortAlgorithm quickSort() {
        return new SortAlgorithm() {
            @Override public String name() { return "QuickSort"; }

            @Override public void sort(Record[] a) {
                if (a == null || a.length < 2) return;
                quick3Optimized(a, 0, a.length - 1);
            }

            // optimized 3-way quicksort with median-of-three pivot and tail-call elimination
            private void quick3Optimized(Record[] a, int lo, int hi) {
                while (lo < hi) {
                    // if small range, finish with insertion sort
                    if (hi - lo <= CUTOFF) {
                        insertion(a, lo, hi);
                        return;
                    }

                    // median-of-three: sorts a[lo], a[mid], a[hi] and places median at hi-1
                    int mid = lo + ((hi - lo) >>> 1);
                    if (a[mid].key() < a[lo].key()) swap(a, mid, lo);
                    if (a[hi].key()  < a[lo].key()) swap(a, hi,  lo);
                    if (a[hi].key()  < a[mid].key()) swap(a, hi, mid);
                    // a[lo] <= a[mid] <= a[hi]; move median out of the way to hi-1
                    swap(a, mid, hi - 1);
                    int pivotKey = a[hi - 1].key();

                    // 3-way partition (Dijkstra) over the interior [lo+1 .. hi-2]
                    // a[lo] < pivot and a[hi] > pivot are already correct
                    int lt = lo + 1;    // a[lo+1..lt-1] < pivot
                    int i  = lo + 1;    // a[lt..i-1]   == pivot
                    int gt = hi - 2;    // a[gt+1..hi-2] > pivot

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

                    // restore pivot from hi-1 into its final position (gt+1)
                    swap(a, hi - 1, gt + 1);
                    // now: a[lo..lt-1] < pivot, a[lt..gt+1] == pivot, a[gt+2..hi] > pivot
                    int pivotEnd = gt + 1;

                    // recurse on smaller partition, iterate on larger
                    if ((lt - lo) < (hi - pivotEnd)) {
                        quick3Optimized(a, lo, lt - 1);
                        lo = pivotEnd + 1;
                    } else {
                        quick3Optimized(a, pivotEnd + 1, hi);
                        hi = lt - 1;
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

    /* ---------------------------
       Shared helpers
       --------------------------- */

    // insertion sort for a range [lo..hi] (uses straight insertion, with cached key)
    private static void insertion(Record[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            Record cur = a[i];
            int curKey = cur.key();
            int j = i - 1;
            while (j >= lo && a[j].key() > curKey) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = cur;
        }
    }
}