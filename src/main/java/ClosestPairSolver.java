import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class ClosestPairSolver {
    public static double bruteForce(Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;
        int n = points.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDistance) minDistance = dist;
            }
        }
        return minDistance;
    }

    public static double findClosestPair(Point[] points, Metrics metrics) {
        metrics.reset();
        long startTime = System.nanoTime();
        if (points == null || points.length < 2) {
            metrics.executionTimeNs = System.nanoTime() - startTime;
            return Double.POSITIVE_INFINITY;
        }
        Point[] px = points.clone();
        Point[] py = points.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        metrics.swapsOrAllocations += points.length * 2L;

        double result = solveRecursive(px, py, metrics);
        metrics.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }

    private static double solveRecursive(Point[] px, Point[] py, Metrics metrics) {
        metrics.enterRecursion();
        int n = px.length;
        if (n <= 3) {
            double result = bruteForce(px);
            metrics.exitRecursion();
            return result;
        }

        int mid = n / 2;
        Point midPoint = px[mid];
        Point[] lx = Arrays.copyOfRange(px, 0, mid);
        Point[] rx = Arrays.copyOfRange(px, mid, n);

        Set<Point> leftSet = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        leftSet.addAll(Arrays.asList(lx));
        List<Point> lyList = new ArrayList<>(mid);
        List<Point> ryList = new ArrayList<>(n - mid);
        for (Point p : py) {
            if (leftSet.contains(p)) lyList.add(p);
            else ryList.add(p);
        }

        double dLeft = solveRecursive(lx, lyList.toArray(new Point[0]), metrics);
        double dRight = solveRecursive(rx, ryList.toArray(new Point[0]), metrics);
        double delta = Math.min(dLeft, dRight);

        List<Point> strip = new ArrayList<>();
        for (Point p : py) {
            if (Math.abs(p.x - midPoint.x) < delta) strip.add(p);
        }

        double stripMin = delta;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < stripMin; j++) {
                metrics.comparisons++;
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < stripMin) stripMin = dist;
            }
        }
        metrics.exitRecursion();
        return Math.min(delta, stripMin);
    }
}