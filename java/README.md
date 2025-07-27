# ☕ Java Implementation — Conjoined Tree Data Structure

This module contains the **Java implementation** of the [Conjoined Tree](https://github.com/esuryadi/conjoined-tree) — 
a novel multi-root, shared-leaf graph structure designed for dynamic, multi-dimensional data modeling.

---

## 📦 What's Included

This package provides:

- `ConjoinedTwinTree.java` – Main class for managing the dual-root tree and also providing utility methods for traversal, filtering, and sorting
- `TreeNode.java` – Generic tree node interface (used by all node types)
- `AbstractNode.java`, `BranchNode.java`, `LeafNode.java` – Specialized node implementations
- `BranchAccumulator.java` – Branch value accumulator interface to aggregate leaf node values for a given branch
- Sample usage and traversal code

> ⚠️ This version is currently designed for **2 dimensions** (dual-root). An N-dimensional version is under consideration.

---

## 📌 Key Concepts

- **Two roots**: One for each axis (e.g., row and column)
- **Branch nodes**: Hierarchical containers under each root
- **Leaf nodes**: Shared by one branch from each axis

### 🌿 Leaf Nodes = Shared Data

Each leaf node has two parents: one from each dimension.  
This allows **sorting, filtering, and inserting in one dimension** without disrupting the other.

---

### ✅ How to Use

## 🧱 Instantiate the tree

```java
ConjoinedTwinTree<String, Integer> twinTree = new ConjoinedTwinTree<>();
```

## 🌲 Add branches and leaves

```java
// Add branches to each root (row & column)
TreeNode<String, Integer> rowA = twinTree.getRootNode1().addBranch("A");
TreeNode<String, Integer> rowB = twinTree.getRootNode1().addBranch("B");
TreeNode<String, Integer> colP = twinTree.getRootNode2().addBranch("P");
TreeNode<String, Integer> colQ = twinTree.getRootNode2().addBranch("Q");

// Add a shared leaf node
LeafNode<String, Integer> leaf1 = rowA.addLeaf(1, colP);
LeafNode<String, Integer> leaf2 = rowA.addLeaf(2, colQ);
LeafNode<String, Integer> leaf3 = rowB.addLeaf(3, colP);
LeafNode<String, Integer> leaf4 = rowB.addLeaf(4, colQ);
```

## 🔁 Traverse the tree

```java
// Traverse the row branch axis of the ConjoinedTwinTree and return the list of branch hierarchy
List<List<TreeNode<String, Integer>>> rowBranches = twinTree.traverseBranches(twinTree.getRoot1());

// Traverse the column branch axis of the ConjoinedTwinTree and return the list of branch hierarchy
List<List<TreeNode<String, Integer>>> colBranches = twinTree.traverseBranches(twinTree.getRoot2());

// Traverse the leaf nodes of the ConjoinedTwinTree and return the map of the leaf nodes by its row and branch nodes location
Map<List<TreeNode<String, Integer>>, Map<List<TreeNode<String, Integer>>, TreeNode<String, Integer>>> leafMap =
    twinTree.traverseLeafs(rowBranches);

// Access leaf at (row, column)
TreeNode<String, Integer> value = leafMap.get(rowBranches.get(0)).get(colBranches.get(0));
System.out.println("Leaf value: " + value.getValue());
```

## 🔁 Search, filter, or sort the tree
```java
// Search for a branch that has "foo" as a value within ConjoinedTwinTree
TreeNode<String, String> fooNode = twinTree.search((branch, leaf) -> "foo".equals(branch), LOCATION.BRANCH);

// Filter the ConjoinedTwinTree branch to hide a branch that has "foo" as a value
twinTree.filter(treeNode -> !"foo".equals(treeNode.getBranchValue()));

// Sort the ConjoinedTwinTree first level branch
twinTree.sort(twinTree.getRootNode1(), (node1, node2) -> node1.getBranchValue().compareTo(node2.getBranchValue()));
```

### 🧪 Sample Output
```text
               root
               /   \
             "P" - "Q"
              |     |
       "A" -- 1 --- 2
      / |     |     |
 root   |     |     |
      \ |     |     |
       "B" -- 3 --- 4
```

### 🔬 Extend It

You can expand the tree to:
- Sort leaf nodes dynamically
- Filter branches by custom predicates
- Add support for third, fourth, or N-th root nodes (WIP)

If you’d like to collaborate on an N-dimensional extension or alternative language version, please open a PR or issue.

### 👨‍💻 Contributed By
**Edward Suryadi**  
Inventor of the Conjoined Tree  
[LinkedIn](https://www.linkedin.com/in/edward-suryadi/) · [Article](https://www.linkedin.com/pulse/tree-two-roots-new-data-structure-multi-dimensional-thinking-suryadi-czycc/)