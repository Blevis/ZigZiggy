name comes from George B. Dantzig if anyone is curious.

![Java](https://img.shields.io/badge/language-Java-orange)
![Algorithms](https://img.shields.io/badge/topic-Algorithms-blue)
![Benchmark](https://img.shields.io/badge/project-Benchmark-green)

# DSA Sprint Project – Sorting & Searching Benchmark

Implementation and benchmarking of classic sorting and searching algorithms for the Data Structures & Algorithms course.

The project evaluates algorithm performance using a custom benchmarking framework and analyzes both theoretical complexity and real execution times.

---

## Implemented Algorithms

### Sorting
- **Insertion Sort**
- **Merge Sort**
- **Quick Sort**

### Searching
- **Linear Search**
- **Binary Search (any-match)**

All algorithms operate on a dataset of `Record` objects and are benchmarked under controlled conditions.

---

## Benchmark Configuration

The benchmark was executed with the following parameters:

| Parameter | Value |
|-----------|------|
| Dataset size | 100,000 records |
| Dataset type | RANDOM |
| Warmup runs | 1 |
| Measured runs | 5 |
| Random seed | 12345 |

---

## Results Summary

| Algorithm | Avg Time (ms) | Min (ms) | Max (ms) |
|----------|---------------|----------|----------|
| Binary Search | 0.0057 | 0.0057 | 0.0069 |
| Linear Search | 1.4186 | 1.4037 | 2.9264 |
| Insertion Sort | 10503.9 | 10007.0 | 10741.6 |
| Merge Sort | 13.5673 | 13.3778 | 14.5073 |
| Quick Sort | 21.9362 | 15.7297 | 22.2189 |

Key observations:

- Binary search performs nearly instantly due to its **O(log n)** complexity.
- Linear search scales linearly with dataset size.
- Insertion sort becomes extremely slow for large inputs due to **O(n²)** complexity.
- Merge sort and quick sort perform significantly better with **O(n log n)** behavior.

---
# DSA Sprint Project – Sorting & Searching Benchmark

Implementation and benchmarking of classic sorting and searching algorithms for the Data Structures & Algorithms course.

The project evaluates algorithm performance using a custom benchmarking framework and analyzes both theoretical complexity and real execution times.

---

## Implemented Algorithms

### Sorting
- **Insertion Sort**
- **Merge Sort**
- **Quick Sort**

### Searching
- **Linear Search**
- **Binary Search (any-match)**

All algorithms operate on a dataset of `Record` objects and are benchmarked under controlled conditions.

---

## Benchmark Configuration

The benchmark was executed with the following parameters:

| Parameter | Value |
|-----------|------|
| Dataset size | 100,000 records |
| Dataset type | RANDOM |
| Warmup runs | 1 |
| Measured runs | 5 |
| Random seed | 12345 |

---

## Results Summary

| Algorithm | Avg Time (ms) | Min (ms) | Max (ms) |
|----------|---------------|----------|----------|
| Binary Search | 0.0057 | 0.0057 | 0.0069 |
| Linear Search | 1.4186 | 1.4037 | 2.9264 |
| Insertion Sort | 10503.9 | 10007.0 | 10741.6 |
| Merge Sort | 13.5673 | 13.3778 | 14.5073 |
| Quick Sort | 21.9362 | 15.7297 | 22.2189 |

Key observations:

- Binary search performs nearly instantly due to its **O(log n)** complexity.
- Linear search scales linearly with dataset size.
- Insertion sort becomes extremely slow for large inputs due to **O(n²)** complexity.
- Merge sort and quick sort perform significantly better with **O(n log n)** behavior.

---

## Contributors

- **Blevis Allushi** – Sorting algorithm implementation and optimization
- **Kristjan Seraj** – Searching algorithm implementation
- **Renato Zotaj** – Complexity analysis and testing
- **Leandra Latifi** – Validation and experimental insights

---

## Course & Lecturer
Evis Plaku  
Data Structures & Algorithms  
Metropolitan University of Tirana
