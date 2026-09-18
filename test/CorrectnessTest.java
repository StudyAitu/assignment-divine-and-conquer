import java.util.Arrays;
import java.util.Random;

public class CorrectnessTest {
    public static void main(String[] args) {
        testSorting();
        testSelect();
        testClosestPair();
        System.out.println("ALL CORRECTNESS TESTS PASSED!");
    }

    private static void testSorting() {
        Metrics metrics = new Metrics();
        Random rand = new Random(42);
        int[][] testCases = {
                {}, {5}, {1, 2, 3, 4, 5}, {5, 4, 3, 2, 1},
                {2, 2, 2, 2, 2}, rand.ints(500, -1000, 1000).toArray()
        };
        for (int[] tc : testCases) {
            int[] expected = tc.clone();
            Arrays.sort(expected);

            int[] ms = tc.clone();
            MergeSorter.sort(ms, metrics);
            check(Arrays.equals(ms, expected), "MergeSort failed");

            int[] qs = tc.clone();
            QuickSorter.sort(qs, metrics);
            check(Arrays.equals(qs, expected), "QuickSort failed");
        }
    }

    private static void testSelect() {
        Metrics metrics = new Metrics();
        Random rand = new Random(42);
        for (int t = 0; t < 100; t++) {
            int n = rand.nextInt(200) + 1;
            int[] arr = rand.ints(n, -500, 500).toArray();
            int k = rand.nextInt(n);
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            int actual = DeterministicSelector.select(arr, k, metrics);
            check(actual == sorted[k], "Deterministic Select failed at test " + t);
        }

        // Explicit duplicate-heavy cases: important because 3-way partitioning handles them safely.
        int[] duplicates = new int[1000];
        Arrays.fill(duplicates, 7);
        check(DeterministicSelector.select(duplicates, 500, metrics) == 7,
                "Deterministic Select failed on all-duplicates case");
    }

    private static void testClosestPair() {
        Metrics metrics = new Metrics();
        Random rand = new Random(42);
        for (int t = 0; t < 50; t++) {
            int n = rand.nextInt(100) + 2;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(rand.nextDouble() * 100, rand.nextDouble() * 100);
            }
            double expected = ClosestPairSolver.bruteForce(points);
            double actual = ClosestPairSolver.findClosestPair(points, metrics);
            check(Math.abs(expected - actual) < 1e-6, "Closest Pair failed at test " + t);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
