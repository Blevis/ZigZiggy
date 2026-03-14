package dsa.lab.alg.search;

import dsa.lab.model.Record;

public final class StudentSearches {
    private StudentSearches() { }

    public static SearchAlgorithm linearSearch() {
        return new SearchAlgorithm() {
            @Override public String name() { return "LinearSearch"; }

            @Override public int searchByKey(Record[] a, int key) {
                for (int i = 0; i < a.length; i++) {
                    if (a[i].key() == key) {
                        return i;
                    }
                }
                return -1;
            }
        };
    }

    public static SearchAlgorithm binarySearchAnyMatch() {
        return new SearchAlgorithm() {
            @Override public String name() { return "BinarySearch(any)"; }

            @Override public int searchByKey(Record[] a, int key) {
                int low = 0;
                int high = a.length - 1;

                while (low <= high) {
                    int mid = low + (high - low) / 2;

                    if (a[mid].key() == key) {
                        return mid;
                    } else if (a[mid].key() < key) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                return -1;
            }
        };
    }
}
