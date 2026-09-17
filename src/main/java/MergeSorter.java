public class MergeSorter {
    private static final int INSERTION_SORT_CUTOFF = 15;

    public static void sort(int[] a, Metrics metrics) {
        metrics.reset();
        long startTime = System.nanoTime();
        if (a != null && a.length > 1) {
            int[] buffer = new int[a.length];
            metrics.swapsOrAllocations += a.length;
            sortRecursive(a, buffer, 0, a.length - 1, metrics);
        }
        metrics.executionTimeNs = System.nanoTime() - startTime;
    }

    private static void sortRecursive(int[] a, int[] buffer, int low, int high, Metrics metrics) {
        metrics.enterRecursion();
        if (high - low <= INSERTION_SORT_CUTOFF) {
            insertionSort(a, low, high, metrics);
            metrics.exitRecursion();
            return;
        }
        int mid = low + (high - low) / 2;
        sortRecursive(a, buffer, low, mid, metrics);
        sortRecursive(a, buffer, mid + 1, high, metrics);
        merge(a, buffer, low, mid, high, metrics);
        metrics.exitRecursion();
    }

    private static void merge(int[] a, int[] buffer, int low, int mid, int high, Metrics metrics) {
        System.arraycopy(a, low, buffer, low, high - low + 1);
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            metrics.comparisons++;
            if (i > mid) a[k] = buffer[j++];
            else if (j > high) a[k] = buffer[i++];
            else {
                metrics.comparisons++;
                if (buffer[j] < buffer[i]) a[k] = buffer[j++];
                else a[k] = buffer[i++];
            }
        }
    }

    private static void insertionSort(int[] a, int low, int high, Metrics metrics) {
        for (int i = low + 1; i <= high; i++) {
            int temp = a[i], j = i - 1;
            while (j >= low) {
                metrics.comparisons++;
                if (a[j] > temp) {
                    a[j + 1] = a[j];
                    metrics.swapsOrAllocations++;
                    j--;
                } else break;
            }
            a[j + 1] = temp;
        }
    }
}