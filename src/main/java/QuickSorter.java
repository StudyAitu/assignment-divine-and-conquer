import java.util.Random;

public class QuickSorter {
    private static final Random random = new Random();

    public static void sort(int[] a, Metrics metrics) {
        metrics.reset();
        long startTime = System.nanoTime();
        if (a != null && a.length > 1) {
            sortRecursive(a, 0, a.length - 1, metrics);
        }
        metrics.executionTimeNs = System.nanoTime() - startTime;
    }

    private static void sortRecursive(int[] a, int low, int high, Metrics metrics) {
        while (low < high) {
            metrics.enterRecursion();
            int pivotIndex = partition(a, low, high, metrics);
            if (pivotIndex - low < high - pivotIndex) {
                sortRecursive(a, low, pivotIndex - 1, metrics);
                metrics.exitRecursion();
                low = pivotIndex + 1;
            } else {
                sortRecursive(a, pivotIndex + 1, high, metrics);
                metrics.exitRecursion();
                high = pivotIndex - 1;
            }
        }
    }

    private static int partition(int[] a, int low, int high, Metrics metrics) {
        int randomIndex = low + random.nextInt(high - low + 1);
        swap(a, randomIndex, high, metrics);
        int pivot = a[high], i = low - 1;
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