public class Metrics {
    public long executionTimeNs = 0;
    public int maxRecursionDepth = 0;
    public long comparisons = 0;
    public long swapsOrAllocations = 0;
    private int currentDepth = 0;

    public void reset() {
        executionTimeNs = 0;
        maxRecursionDepth = 0;
        comparisons = 0;
        swapsOrAllocations = 0;
        currentDepth = 0;
    }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxRecursionDepth) maxRecursionDepth = currentDepth;
    }

    public void exitRecursion() {
        currentDepth--;
    }
}