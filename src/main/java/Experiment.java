import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class Experiment {
    private static final int[] SIZES = {100, 1000, 10000, 50000, 100000};
    private static final String[] TYPES = {"Random", "Sorted", "Reverse-Sorted", "Duplicate-Heavy"};

    public static void runAllExperiments() {
        String csvPath = "results/results.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            writer.println("Algorithm,InputType,Size,ExecutionTimeMs,MaxRecursionDepth,Comparisons,SwapsOrAllocations");
            Metrics metrics = new Metrics();
            Random rand = new Random(42);

            // Small deterministic warm-up reduces first-run JVM/JIT effects.
            int[] warmup = generateData(10000, "Random", rand);
            for (int i = 0; i < 3; i++) {
                MergeSorter.sort(warmup.clone(), metrics);
                QuickSorter.sort(warmup.clone(), metrics);
                DeterministicSelector.select(warmup.clone(), warmup.length / 2, metrics);
                ClosestPairSolver.findClosestPair(generatePoints(1000, rand), metrics);
            }

            for (int size : SIZES) {
                for (String type : TYPES) {
                    int[] data = generateData(size, type, rand);

                    int[] copyMS = data.clone();
                    MergeSorter.sort(copyMS, metrics);
                    writeMetric(writer, "MergeSort", type, size, metrics);

                    int[] copyQS = data.clone();
                    QuickSorter.sort(copyQS, metrics);
                    writeMetric(writer, "QuickSort", type, size, metrics);

                    int[] copySel = data.clone();
                    DeterministicSelector.select(copySel, size / 2, metrics);
                    writeMetric(writer, "DeterministicSelect", type, size, metrics);
                }

                Point[] points = generatePoints(size, rand);
                ClosestPairSolver.findClosestPair(points, metrics);
                writeMetric(writer, "ClosestPair", "Random2D", size, metrics);
            }
            System.out.println("Done! Saved to " + csvPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMetric(PrintWriter writer, String algo, String type, int size, Metrics metrics) {
        double timeMs = metrics.executionTimeNs / 1_000_000.0;
        writer.printf(Locale.US, "%s,%s,%d,%.4f,%d,%d,%d%n",
                algo, type, size, timeMs, metrics.maxRecursionDepth,
                metrics.comparisons, metrics.swapsOrAllocations);
    }

    private static int[] generateData(int size, String type, Random rand) {
        int[] arr = new int[size];
        switch (type) {
            case "Random":
                for (int i = 0; i < size; i++) arr[i] = rand.nextInt(1_000_000);
                break;
            case "Sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "Reverse-Sorted":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "Duplicate-Heavy":
                for (int i = 0; i < size; i++) arr[i] = rand.nextInt(10);
                break;
            default:
                throw new IllegalArgumentException("Unknown input type: " + type);
        }
        return arr;
    }

    private static Point[] generatePoints(int size, Random rand) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
        }
        return points;
    }
}
