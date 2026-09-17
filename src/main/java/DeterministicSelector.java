import java.util.Arrays;

public class DeterministicSelector {
    public static int select(int[] a, int k, Metrics metrics) {
        metrics.reset();
        long startTime = System.nanoTime();
        if (a == null || k < 0 || k >= a.length) throw new IllegalArgumentException();
        int[] copy = a.clone();
        int result = selectRecursive(copy, 0, copy.length - 1, k, metrics);
        metrics.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }

    private static int selectRecursive(int[] a, int low, int high, int k, Metrics metrics) {
        metrics.enterRecursion();
        if (low == high) {
            metrics.exitRecursion();
            return a[low];
        }
        int pivot = getMedianOfMedians(a, low, high, metrics);
        int pivotIndex = partitionAroundPivot(a, low, high, pivot, metrics);
        if (k == pivotIndex) {
            metrics.exitRecursion();
            return a[k];
        } else if (k < pivotIndex) {
            int res = selectRecursive(a, low, pivotIndex - 1, k, metrics);
            metrics.exitRecursion();
            return res;
        } else {
            int res = selectRecursive(a, pivotIndex + 1, high, k, metrics);
            metrics.exitRecursion();
            return res;
        }
    }

    private static int getMedianOfMedians(int[] a, int low, int high, Metrics metrics) {
        int n = high - low + 1;
        if (n <= 5) {
            Arrays.sort(a, low, high + 1);
            return a[low + n / 2];
        }
        int numGroups = (int) Math.ceil((double) n / 5);
        int[] medians = new int[numGroups];
        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);
            Arrays.sort(a, groupLow, groupHigh + 1);
            medians[i] = a[groupLow + (groupHigh - groupLow) / 2];
        }
        return selectRecursive(medians, 0, numGroups - 1, numGroups / 2, metrics);
    }

    private static int partitionAroundPivot(int[] a, int low, int high, int pivot, Metrics metrics) {
        for (int i = low; i <= high; i++) {
            if (a[i] == pivot) {
                swap(a, i, high, metrics);
                break;
            }
        }
        int i = low - 1;
        for (int j = low; j < high; j++) {
            metrics.comparisons++;
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j, metrics);
            }
        }
        swap(a, i + 1, high, metrics);
        return i + 1;
    }

    private static void swap(int[] a, int i, int j, Metrics metrics) {
        if (i != j) {
            int temp = a[i]; a[i] = a[j]; a[j] = temp;
            metrics.swapsOrAllocations++;
        }
    }
}