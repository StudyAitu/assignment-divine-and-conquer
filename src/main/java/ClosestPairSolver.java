import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Point[] py = px.clone();
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));

        double result = solveRecursive(px, py, metrics);
        metrics.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }

    private static double solveRecursive(Point[] px, Point[] py, Metrics metrics) {
        metrics.enterRecursion();
        int n = px.length;
        if (n <= 3) {
            metrics.exitRecursion();
            return bruteForce(px);
        }
        int mid = n / 2;
        Point midPoint = px[mid];
        Point[] lx = Arrays.copyOfRange(px, 0, mid);
        Point[] rx = Arrays.copyOfRange(px, mid, n);

        List<Point> lyList = new ArrayList<>();
        List<Point> ryList = new ArrayList<>();
        for (Point p : py) {
            if (p.x <= midPoint.x) lyList.add(p);
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
        int stripSize = strip.size();
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && (strip.get(j).y - strip.get(i).y) < stripMin; j++) {
                metrics.comparisons++;
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < stripMin) stripMin = dist;
            }
        }
        metrics.exitRecursion();
        return Math.min(delta, stripMin);
    }
}