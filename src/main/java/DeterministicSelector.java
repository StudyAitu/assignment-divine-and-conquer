import java.util.Arrays;

public class DeterministicSelector {
    public static int select(int[] a, int k, Metrics metrics) {
        metrics.reset();
        long startTime = System.nanoTime();
        if (a == null || k < 0 || k >= a.length) throw new IllegalArgumentException();
        int[] copy = a.clone();
        metrics.swapsOrAllocations += copy.length;
        int result = selectRecursive(copy, 0, copy.length - 1, k, metrics);
        metrics.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }

    private static int selectRecursive(int[] a, int low, int high, int k, Metrics metrics) {
        metrics.enterRecursion();
        if (low == high) {
            int result = a[low];
            metrics.exitRecursion();
            return result;
        }

        int pivot = getMedianOfMedians(a, low, high, metrics);
        int[] equalRange = partitionAroundPivot(a, low, high, pivot, metrics);
        int lt = equalRange[0];
        int gt = equalRange[1];

        if (k < lt) {
            int res = selectRecursive(a, low, lt - 1, k, metrics);
            metrics.exitRecursion();
            return res;
        }
        if (k <= gt) {
            int res = a[k];
            metrics.exitRecursion();
            return res;
        }
        int res = selectRecursive(a, gt + 1, high, k, metrics);
        metrics.exitRecursion();
        return res;
    }

    private static int getMedianOfMedians(int[] a, int low, int high, Metrics metrics) {
        int n = high - low + 1;
        if (n <= 5) {
            Arrays.sort(a, low, high + 1);
            return a[low + n / 2];
        }

        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];
        metrics.swapsOrAllocations += numGroups;
        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);
            Arrays.sort(a, groupLow, groupHigh + 1);
            medians[i] = a[groupLow + (groupHigh - groupLow) / 2];
        }
        return selectRecursive(medians, 0, numGroups - 1, numGroups / 2, metrics);
    }

    private static int[] partitionAroundPivot(int[] a, int low, int high, int pivot, Metrics metrics) {
        int lt = low;
        int i = low;
        int gt = high;
        while (i <= gt) {
            metrics.comparisons++;
            if (a[i] < pivot) {
                swap(a, lt++, i++, metrics);
            } else {
                metrics.comparisons++;
                if (a[i] > pivot) swap(a, i, gt--, metrics);
                else i++;
            }
        }
        return new int[]{lt, gt};
    }

    private static void swap(int[] a, int i, int j, Metrics metrics) {
        if (i != j) {
            int temp = a[i]; a[i] = a[j]; a[j] = temp;
            metrics.swapsOrAllocations++;
        }
    }
}
