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
        Random rand = new Random();
        int[][] testCases = {{}, {5}, {1, 2, 3, 4, 5}, {5, 4, 3, 2, 1}, {2, 2, 2, 2, 2}, rand.ints(500, -1000, 1000).toArray()};
        for (int[] tc : testCases) {
            int[] expected = tc.clone();
            Arrays.sort(expected);
            int[] ms = tc.clone(); MergeSorter.sort(ms, metrics);
            assert Arrays.equals(ms, expected);
            int[] qs = tc.clone(); QuickSorter.sort(qs, metrics);
            assert Arrays.equals(qs, expected);
        }
    }

    private static void testSelect() {
        Metrics metrics = new Metrics();
        Random rand = new Random();
        for (int t = 0; t < 100; t++) {
            int n = rand.nextInt(200) + 1;
            int[] arr = rand.ints(n, -500, 500).toArray();
            int k = rand.nextInt(n);
            int[] sorted = arr.clone(); Arrays.sort(sorted);
            assert DeterministicSelector.select(arr, k, metrics) == sorted[k];
        }
    }

    private static void testClosestPair() {
        Metrics metrics = new Metrics();
        Random rand = new Random();
        for (int t = 0; t < 50; t++) {
            int n = rand.nextInt(100) + 2;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) points[i] = new Point(rand.nextDouble() * 100, rand.nextDouble() * 100);
            assert Math.abs(ClosestPairSolver.bruteForce(points) - ClosestPairSolver.findClosestPair(points, metrics)) < 1e-6;
        }
    }
}