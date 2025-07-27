# 🌳 Conjoined Tree — Data Structure for Multi-Dimensional Modeling

**Invented by Edward Suryadi**  
Patent-pending · Open source · Designed for dynamic, multi-axis data handling

---

## 🚀 What It Is

The **Conjoined Tree** (also known as the Conjoined Twin Tree when limited to two axes) is a novel graph-based data structure with:

- **Multiple root nodes** (e.g., for rows, columns, time, dimension X)
- **Branch hierarchies** under each root
- **Shared leaf nodes** that have one parent in each axis hierarchy

This enables dynamic insertions, deletions, sorting, and filtering along any axis—without realigning or moving entire rows/columns. It shines in pivot‑table–style datasets and multi-dimensional data models.

---

## 🔁 Key Advantages

- Sort/filter/insert/delete from any axis — no global rearrangement needed
- Supports **shared leaf nodes**, making it ideal for **sparse or intersecting data**
- Naturally models **multi-dimensional relationships**
- Scales up to **N dimensions**, not just rows × columns

---

## 💡 Why It Matters

| Traditional Structure | Conjoined Tree |
|-----------------------|----------------|
| 2D Arrays / Matrices | Unfolds to N dimensions |
| Pivot operations require full row/column reordering | Sort/filter at axis node-level only |
| Sparse data requires many empty cells | Sparsity handled natively via shared leaves |
| Moves data to rearrange views | Structures views without copying leaf data |

---

## 🧪 Example Use Case: Pivot Table Replacement

Consider you have the following pivot table:

|       |     | **P** |       | **Q** |       |
|-------|-----|-------|-------|-------|-------|
|       |     | **a** | **b** | **a** | **b** |
| **A** | 1   |   4   |   5   |   9   |  15   |
|       | 2   |   2   |   6   |  10   |  14   |
| **B** | 1   |   1   |   8   |  11   |  16   |
|       | 2   |   3   |   7   |  12   |  13   |

Without Conjoined Tree:
- Sorting by column P‑a requires full row movement

With Conjoined Tree:
- Reorder just the P‑a branch
- Shared leaf nodes maintain alignment automatically

This creates more efficient pivot operations with minimal data manipulation.

---

## 🧠 Use Cases

- Pivot table engines
- OLAP cubes
- Multi-dimensional sparse matrix modeling
- Machine learning feature stores
- Graph-based knowledge representations
- UI backends for complex data grids

---

## ⚙️ Java Implementation (Current)

You’ll find Java code under `java/` that implements the **two-axis version** of the Conjoined Tree. It covers:
- Dual-root tree structure
- Traversal methods
- Branch sorting and leaf alignment

Contributions extending this to **three or more axes (N-dimensional)** or in **other programming languages** are highly encouraged.

---

## 🛠️ Getting Started

1. **Clone the repo**
   ```bash
   git clone https://github.com/esuryadi/conjoined-tree.git
   ```
2.	Explore the Java implementation at conjoined-tree/java/
3.	Run tests and benchmarks
4.	Extend, refactor, or port to your preferred language
5.	Contribute back via pull requests or discussions

---

## 🔍 Documentation & Resources
- 📄 **Full technical spec:** [Confluence Documentation](https://edsuryadi.atlassian.net/wiki/external/NzM1Mjk1NDUwOWU4NGIwZDg5MGQxYWMzMTdhN2M4YWM)
- 🧾 **Patent filings:** US 18/676076, 18/676081, 18/676086 (Inventor: Edward Suryadi; Assigned to Workday, Inc.)
- 🧠 **Blog article:** “The Tree with Two Roots: A New Data Structure for Multi-Dimensional Thinking” ([LinkedIn Pulse](https://www.linkedin.com/pulse/tree-two-roots-new-data-structure-multi-dimensional-thinking-suryadi-czycc/))

---

## 💬 Get Involved

**Want to:**
- Extend to N dimensions?
- Add language implementations (Python, C#, JavaScript)?
- Build visualization tools or GUIs?
- Share real-world use cases?

Open an issue or a pull request and join the conversation!

---

## 🏷️ License

Licensed under the Apache License, Version@2.0

---

© 2025 Edward Suryadi  
Inventor, developer, and evangelist of the Conjoined Tree structure