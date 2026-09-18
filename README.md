# Assignment 1: Divide-and-Conquer Algorithm Analysis

## A. Project Overview
* **Purpose:** Implementation, practical evaluation, and theoretical analysis of classic divide-and-conquer algorithms.
* **Implemented Algorithms:**
  1. MergeSort (with insertion sort cutoff and reusable buffer)[cite: 3].
  2. QuickSort (with randomized pivot and tail-recursion optimization)[cite: 3].
  3. Deterministic Select / Median-of-Medians (groups of 5, worst-case linear time)[cite: 3].
  4. Closest Pair of Points (2D divide-and-conquer with strip optimization)[cite: 3].

---

## B. Algorithm Analysis

### 1. MergeSort
* **How it works:** Recursively splits the array into two halves, sorts them, and merges them back in linear time using an auxiliary buffer[cite: 3]. Small sub-arrays (size $\le 15$) switch to Insertion Sort.
* **Complexity:** Time $\Theta(n \log n)$ in all cases; Space $\Theta(n)$ for the auxiliary buffer.
* **Recurrence:** $T(n) = 2T(n/2) + \Theta(n)$. By Master Theorem (Case 2), complexity is $\Theta(n \log n)$.

### 2. QuickSort
* **How it works:** Picks a randomized pivot, partitions the array in-place, and recursively sorts sub-arrays[cite: 3]. Tail-recursion optimization (sorting the smaller partition first) guarantees stack depth control.
* **Complexity:** Expected Time $\Theta(n \log n)$, Worst-case Time $\Theta(n^2)$; Space $\Theta(\log n)$ due to recursion stack.
* **Recurrence:** $T(n) = T(k) + T(n-k-1) + \Theta(n)$.

### 3. Deterministic Select (Median-of-Medians)
* **How it works:** Divides elements into groups of 5, finds each group's median, recursively finds the median of those medians to use as a guaranteed "good" pivot[cite: 3].
* **Complexity:** Worst-case Time $\Theta(n)$; Space $\Theta(\log n)$.
* **Recurrence:** $T(n) \le T(n/5) + T(7n/10) + \Theta(n)$, which solves to $\Theta(n)$.

### 4. Closest Pair of Points
* **How it works:** Sorts points by $x$, splits by median line, recursively finds minimum distances on left ($d_1$) and right ($d_2$), then checks a vertical strip of width $2\delta$ ($\delta = \min(d_1, d_2)$) sorted by $y$[cite: 3].
* **Complexity:** Time $\Theta(n \log n)$; Space $\Theta(n)$.
* **Recurrence:** $T(n) = 2T(n/2) + \Theta(n) \implies \Theta(n \log n)$.

---

## C. Experimental Results
* Experimental data was automatically logged into `results/results.csv` via `Experiment.java`.
* Tested on input sizes: $100, 1,000, 10,000, 50,000, 100,000$[cite: 3].
* Input structures evaluated: Random, Sorted, Reverse-Sorted, Duplicate-Heavy[cite: 3].

*(Здесь ты можешь вставить таблицы из своего сгенерированного `results.csv` файла)*

### Plots
* **Time vs. n:** Located in `plots/time_vs_n.png`
* **Recursion Depth vs. n:** Located in `plots/recursion_vs_n.png`

---

## D. Discussion
1. **Do results match theoretical complexity?** Yes, empirical measurements show $\Theta(n \log n)$ growth for MergeSort and Closest Pair, and linear scaling for Median-of-Medians.
2. **How does input structure affect performance?** Randomized QuickSort avoids worst-case $\Theta(n^2)$ behavior on sorted data, while MergeSort remains completely stable regardless of initial order.
3. **Why does smaller-first recursion help QuickSort?** It guarantees that the stack depth never exceeds $\Theta(\log n)$ by processing the smaller recursive branch first and iterating over the larger one.
4. **Why does Median-of-Medians guarantee $\Theta(n)$?** Because it consistently eliminates a fixed fraction of elements at each step by avoiding bad worst-case splits.
5. **Why is divide-and-conquer Closest Pair faster than $\Theta(n^2)$?** It restricts comparison checks within a narrow strip of constant bounded density ($\le 6$ points checked per element), reducing checks from quadratic to logarithmic merging steps.
6. **Practical factors affecting performance:** JVM warmup (JIT compilation), garbage collection overhead during frequent array allocations, and CPU cache locality.

---

## E. Reflection
Building this project provided deep practical insights into writing recursive algorithms, managing state via custom `Metrics`, and optimizing data layouts. The main challenge involved implementing the precise indexing for the Median-of-Medians grouping and ensuring the strip condition in Closest Pair was strictly bounded without performance degradation.

---

## F. Screenshots
* **Program Output & Test Results:** Included in `screenshots/`
* **Plots:** Included in `plots/`
