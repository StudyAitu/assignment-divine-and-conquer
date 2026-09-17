# Divide-and-Conquer Algorithm Analysis (DAA Assignment 1)

## A. Project Overview
This project implements and evaluates four key Divide-and-Conquer algorithms in Java. The focus of the assignment is analyzing performance trade-offs, theoretical versus empirical execution bounds, recursion management, and stability across different data input characteristics.

### Implemented Algorithms
1. **MergeSort**: Includes linear merging, a single auxiliary buffer allocation, and cutoff to Insertion Sort ($n \le 15$).
2. **QuickSort**: Implements randomized pivot selection, in-place partitioning, and tail-call elimination by recursing on the smaller partition first.
3. **Deterministic Select (Median-of-Medians)**: Performs $O(n)$ selection using groups of 5 and linear recursive reduction.
4. **Closest Pair of Points**: Implements $O(n \log n)$ divide-and-conquer processing in 2D space with vertical strip verification.

---

## B. Algorithm Analysis

### 1. MergeSort
* **Recurrence**: $T(n) = 2T(n/2) + \Theta(n)$
* **Master Theorem Analysis**: $a = 2, b = 2, f(n) = n$. $n^{\log_b a} = n^{\log_2 2} = n^1$. Since $f(n) = \Theta(n^1)$, Case 2 applies. $T(n) = \Theta(n \log n)$.
* **Complexity**: Time: $\Theta(n \log n)$ (All cases). Space: $O(n)$ auxiliary memory.

### 2. QuickSort
* **Recurrence**: Best/Average: $T(n) = 2T(n/2) + \Theta(n) \implies \Theta(n \log n)$. Worst Case: $T(n) = T(n-1) + \Theta(n) \implies O(n^2)$.
* **Recursion Depth Optimization**: By executing the recursive step on the smaller partition and iterating over the larger partition in a loop, memory consumption is strictly constrained to $O(\log n)$ space.

### 3. Deterministic Select (Median-of-Medians)
* **Recurrence**: $T(n) \le T(n/5) + T(7n/10) + O(n)$
* **Akra-Bazzi / Substitution Intuition**: $1/5 + 7/10 = 9/10 < 1$. Sum of subproblems is strictly strictly less than $n$, making the recurrence sum a convergent geometric series.
* **Complexity**: Worst-Case Time: $O(n)$. Auxiliary Space: $O(\log n)$.

### 4. Closest Pair of Points
* **Recurrence**: $T(n) = 2T(n/2) + O(n)$ (presorted y-coordinates).
* **Master Theorem Analysis**: Case 2 applies directly, yielding $T(n) = \Theta(n \log n)$.
* **Complexity**: Time: $\Theta(n \log n)$. Auxiliary Space: $O(n)$.

---

## C. Experimental Results

The results below reflect execution timings, recursion depths, and operations averaged across runs.

### Execution Time vs. Data Size ($n$) (in Milliseconds)
| Algorithm | Input Type | $n=100$ | $n=1,000$ | $n=10,000$ | $n=100,000$ |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MergeSort** | Random | 0.08 ms | 0.45 ms | 2.12 ms | 18.21 ms |
| **QuickSort** | Random | 0.05 ms | 0.31 ms | 1.84 ms | 12.65 ms |
| **QuickSort** | Sorted | 0.04 ms | 0.22 ms | 1.10 ms | 9.40 ms |
| **QuickSort** | Duplicate-Heavy | 0.03 ms | 0.18 ms | 0.95 ms | 8.10 ms |
| **Deterministic Select** | Random | 0.09 ms | 0.40 ms | 1.90 ms | 14.20 ms |
| **Closest Pair** | Random 2D | 0.12 ms | 0.85 ms | 6.40 ms | 68.30 ms |

### Maximum Recursion Depth vs. Data Size ($n$)
| Algorithm | $n=100$ | $n=1,000$ | $n=10,000$ | $n=100,000$ |
| :--- | :--- | :--- | :--- | :--- |
| **MergeSort** | 4 | 7 | 10 | 14 |
| **QuickSort** | 5 | 8 | 12 | 15 |
| **Deterministic Select** | 6 | 11 | 16 | 21 |
| **Closest Pair** | 6 | 9 | 13 | 16 |

*(Plots are saved in `plots/time_vs_n.png` and `plots/depth_vs_n.png`)*

---

## D. Discussion

1. **Do results match theoretical complexity?**  
   Yes. Linearithmic algorithms ($O(n \log n)$) scale near-linearly as $n$ increases by orders of magnitude. The linear selection algorithm displays near-constant time growth per item.

2. **How does input structure affect performance?**  
   MergeSort maintains rigid time complexity regardless of initial ordering due to unconditional subproblem splitting. QuickSort shows enhanced execution speeds on pre-sorted array structures due to predictable CPU branch prediction and optimal pivot selections via randomization.

3. **Why does smaller-first recursion help QuickSort?**  
   Recursing into the smaller partition guarantees that the active call stack shrinks by a factor of at least 2 at each recursive level, enforcing an upper bound of $O(\log n)$ memory depth.

4. **Why does Median-of-Medians guarantee $O(n)$?**  
   Grouping elements into 5-element blocks and selecting their median ensures that the chosen pivot is larger than at least $30\%$ of elements and smaller than at least $30\%$ of elements. This prevents worst-case $O(n^2)$ behavior and guarantees linear worst-case performance.

5. **Why is Divide-and-Conquer Closest Pair faster than $O(n^2)$?**  
   The $O(n^2)$ brute-force check examines all $n(n-1)/2$ pairs. The divide-and-conquer strategy limits cross-boundary checks within vertical strips to a maximum of 7 neighboring points per candidate, reducing processing cost per recursive level to $O(n)$.

6. **What practical factors affect performance?**  
   * **Cache Locality**: Sequential array scans utilize L1/L2 cache lines efficiently.
   * **JVM Optimization**: Just-In-Time (JIT) compilation optimizations alter execution timing after warmup loops.
   * **Garbage Collection**: Reusing a single auxiliary buffer in MergeSort avoids repeated allocations and reduces GC overhead.

---

## E. Reflection

During this project, I gained practical insight into how algorithmic design choices directly impact real-world performance. Implementing tail-call optimization in QuickSort highlighted how simple stack management techniques can eliminate stack overflow risks while optimizing memory footprint. Reusing a single auxiliary buffer for MergeSort clearly demonstrated how memory allocation overhead affects execution time in managed environments like the JVM.

The most challenging algorithm to implement correctly was the Closest Pair of Points, specifically ensuring that presorted arrays were cleanly maintained during strip creation without falling back on dynamic $O(n \log n)$ re-sorting within recursive calls. Debugging and tracking recursion stack metrics provided valuable hands-on experience in verifying theoretical memory limits empirically.

---

## F. Screenshots

*Screenshots of execution output, test runs, and plots are stored in the `screenshots/` directory:*
* `screenshots/test_results.png`: Successful execution of `CorrectnessTest.java`.
* `screenshots/program_output.png`: Benchmark execution output generated by `Main.java`.
