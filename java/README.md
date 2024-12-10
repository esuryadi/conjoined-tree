# conjoined-twin-tree

## Conjoined Twin Tree Data Structure

The Conjoined Twin Tree data structure is an implementation of Conjoined Tree data structure that has 2 roots which
represent 2 different axis. This data structure forms a 2-dimensional matrix that commonly used in a pivot table.

There are 2 version of Conjoined Twin Tree data structure implementation:
1. conjoined-twin-tree: Strict version of value type on branch and leaf nodes. All branches and leaves must have the same value type.
2. conjoined-twin-tree-variant: Flexible version of value type on branch and leaf nodes. Branches and leaves can have different value types.

Documentation Concept: https://edsuryadi.atlassian.net/wiki/external/NzM1Mjk1NDUwOWU4NGIwZDg5MGQxYWMzMTdhN2M4YWM

## Usage
```agsl
// Creates an instance of ConjoinedTwinTree class
ConjoinedTwinTree<String, Integer> twinTree = new ConjoinedTwinTree<>();

// Add new branches 
TreeNode<String, Integer> a = twinTree.getRootNode1().addBranch("A");
TreeNode<String, Integer> b = twinTree.getRootNode1().addBranch("B");
TreeNode<String, Integer> p = twinTree.getRootNode2().addBranch("P");
TreeNode<String, Integer> q = twinTree.getRootNode2().addBranch("Q");

// Add new leaves to the branches
a.addLeaf(1, p);
a.addLeaf(2, q);
b.addLeaf(3, p);
b.addLeaf(4, q);
```
The above code example would generate the following Conjoined Twin Tree structure:
```
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
Other Conjoined Twin Tree operations:
```agsl
// Search for a branch that has "foo" as a value within ConjoinedTwinTree
TreeNode<String, String> fooNode = twinTree.search((branch, leaf) -> "foo".equals(branch), LOCATION.BRANCH);

// Filter the ConjoinedTwinTree branch to hide a branch that has "foo" as a value
twinTree.filter(treeNode -> !"foo".equals(treeNode.getBranchValue()));

// Sort the ConjoinedTwinTree first level branch
twinTree.sort(twinTree.getRootNode1(), (node1, node2) -> node1.getBranchValue().compareTo(node2.getBranchValue()));

// Traverse the row branch axis of the ConjoinedTwinTree and return the list of branch hierarchy
List<List<TreeNode<?,?>>> rowNodes = twinTree.traverseBranches(twinTree.getRoot1());

// Traverse the column branch axis of the ConjoinedTwinTree and return the list of branch hierarchy
List<List<TreeNode<?,?>>> colNodes = twinTree.traverseBranches(twinTree.getRoot2());

// Traverse the leaf nodes of the ConjoinedTwinTree and return the map of the leaf nodes by its row and branch nodes location
Map<List<TreeNode<?,?>>, Map<List<TreeNode<?,?>>, TreeNode<?,?>>> leafNodesMap = twinTree.traverseLeafs(rowNodes);
```
For the variant version, simply remove the generic declaration. Other examples of Conjoined Tree operations can be found in the test cases.
