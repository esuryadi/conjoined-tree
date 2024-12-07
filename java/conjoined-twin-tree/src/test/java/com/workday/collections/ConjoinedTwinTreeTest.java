package com.workday.collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <strong>ConjoinedTwinTreeTest</strong> class is a unit-test for all {@link ConjoinedTwinTree} method operations.
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConjoinedTwinTreeTest {

    private static ConjoinedTwinTree<String, Integer> twinTree;

    @BeforeAll
    static void setup() {
        twinTree = new ConjoinedTwinTree<>();
    }

    /*
     * A test-case for the ConjoinedTwinTree creation where we asserting that the default 2 root nodes are created.
     */
    @Test
    @Order(1)
    void testCreateConjoinedTwinTree() {
        assertNotNull(twinTree.getRootNode1());
        assertNotNull(twinTree.getRootNode2());
    }

    /*
     * A test-case for adding branches into ConjoinedTwinTree.
     */
    @Test
    @Order(2)
    void testAddBranches() {
        final TreeNode<String, Integer> A = twinTree.getRootNode1().addBranch("A");
        final TreeNode<String, Integer> B = twinTree.getRootNode1().addBranch("B");
        final TreeNode<String, Integer> P = twinTree.getRootNode2().addBranch("P");
        final TreeNode<String, Integer> Q = twinTree.getRootNode2().addBranch("Q");
        assertArrayEquals(new TreeNode[] { A, B }, twinTree.getRootNode1().getChildren().toArray());
        assertArrayEquals(new TreeNode[] { P, Q }, twinTree.getRootNode2().getChildren().toArray());
    }

    /*
     * A test-case for adding leaf nodes into ConjoinedTwinTree
     */
    @Test
    @Order(3)
    void testAddLeaves() {
        final TreeNode<String, Integer> five = twinTree.getRootNode1().getChildren().get(0).addLeaf(5,
            twinTree.getRootNode2().getChildren().get(0), false);
        final TreeNode<String, Integer> three = twinTree.getRootNode1().getChildren().get(0).addLeaf(3,
            twinTree.getRootNode2().getChildren().get(1), false);
        final TreeNode<String, Integer> four = twinTree.getRootNode1().getChildren().get(1).addLeaf(4,
            twinTree.getRootNode2().getChildren().get(0), false);
        final TreeNode<String, Integer> one = twinTree.getRootNode1().getChildren().get(1).addLeaf(1,
            twinTree.getRootNode2().getChildren().get(1), false);

        assertArrayEquals(new TreeNode[] { five, three },
            twinTree.getRootNode1().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { four, one },
            twinTree.getRootNode1().getChildren().get(1).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { five, four },
            twinTree.getRootNode2().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, one },
            twinTree.getRootNode2().getChildren().get(1).getChildren().toArray());
    }

    /*
     * A test-case to insert branches both in the beginning of the list and in the middle of the list.
     */
    @Test
    @Order(4)
    void testInsertBranches() {
        final TreeNode<String, Integer> A = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> B = twinTree.getRootNode1().getChildren().get(1);
        final TreeNode<String, Integer> P = twinTree.getRootNode2().getChildren().get(0);
        final TreeNode<String, Integer> Q = twinTree.getRootNode2().getChildren().get(1);
        final TreeNode<String, Integer> C = twinTree.getRootNode1().addBranch("C", 0);
        final TreeNode<String, Integer> R = twinTree.getRootNode2().addBranch("R", 1);

        assertArrayEquals(new TreeNode[] { C, A, B }, twinTree.getRootNode1().getChildren().toArray());
        assertArrayEquals(new TreeNode[] { P, R, Q }, twinTree.getRootNode2().getChildren().toArray());
    }

    /*
     * A test-case to insert leaf node in the beginning of the list and in the middle of the list.
     */
    @Test
    @Order(5)
    void testInsertLeaves() {
        final TreeNode<String, Integer> C = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> R = twinTree.getRootNode2().getChildren().get(1);

        final TreeNode<String, Integer> five = twinTree.getRootNode1().getChildren().get(1).getChildren().get(0);
        final TreeNode<String, Integer> three = twinTree.getRootNode1().getChildren().get(1).getChildren().get(1);
        final TreeNode<String, Integer> four = twinTree.getRootNode1().getChildren().get(2).getChildren().get(0);
        final TreeNode<String, Integer> one = twinTree.getRootNode1().getChildren().get(2).getChildren().get(1);
        final TreeNode<String, Integer> eight = C.addLeaf(8, R, false);

        final TreeNode<String, Integer> seven = C.addLeaf(7, twinTree.getRootNode2().getChildren().get(0),
            0, false);
        final TreeNode<String, Integer> six = C.addLeaf(6, twinTree.getRootNode2().getChildren().get(2), 1, false);
        final TreeNode<String, Integer> ten = R.addLeaf(10, twinTree.getRootNode1().getChildren().get(1),
            0, false);
        final TreeNode<String, Integer> nine = R.addLeaf(9, twinTree.getRootNode1().getChildren().get(2),
            1, false);

        assertArrayEquals(new TreeNode[] { seven, six, eight },
            twinTree.getRootNode1().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { five, three, ten },
            twinTree.getRootNode1().getChildren().get(1).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { four, one, nine },
            twinTree.getRootNode1().getChildren().get(2).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { five, four, seven },
            twinTree.getRootNode2().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { ten, nine, eight },
            twinTree.getRootNode2().getChildren().get(1).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, one, six },
            twinTree.getRootNode2().getChildren().get(2).getChildren().toArray());
    }

    /*
     * A test-case for deleting a branch and assert that all the branch descendants.
     */
    @Test
    @Order(6)
    void testDeleteABranch() {
        final TreeNode<String, Integer> P = twinTree.getRootNode2().getChildren().get(0);

        final TreeNode<String, Integer> six = twinTree.getRootNode1().getChildren().get(0).getChildren().get(1);
        final TreeNode<String, Integer> eight = twinTree.getRootNode1().getChildren().get(0).getChildren().get(2);
        final TreeNode<String, Integer> three = twinTree.getRootNode1().getChildren().get(1).getChildren().get(1);
        final TreeNode<String, Integer> ten = twinTree.getRootNode1().getChildren().get(1).getChildren().get(2);
        final TreeNode<String, Integer> one = twinTree.getRootNode1().getChildren().get(2).getChildren().get(1);
        final TreeNode<String, Integer> nine = twinTree.getRootNode1().getChildren().get(2).getChildren().get(2);

        twinTree.getRootNode2().delete(P);

        assertArrayEquals(new TreeNode[] { six, eight },
            twinTree.getRootNode1().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, ten },
            twinTree.getRootNode1().getChildren().get(1).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { one, nine },
            twinTree.getRootNode1().getChildren().get(2).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { ten, nine, eight },
            twinTree.getRootNode2().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, one, six },
            twinTree.getRootNode2().getChildren().get(1).getChildren().toArray());
    }

    /*
     * A test-case for deleting a leaf node and assert that when a leaf node is deleted from one parent, it will also
     * be deleted from the other parent.
     */
    @Test
    @Order(7)
    void testDeleteALeaf() {
        final TreeNode<String, Integer> B = twinTree.getRootNode1().getChildren().get(2);

        final TreeNode<String, Integer> six = twinTree.getRootNode1().getChildren().get(0).getChildren().get(0);
        final TreeNode<String, Integer> eight = twinTree.getRootNode1().getChildren().get(0).getChildren().get(1);
        final TreeNode<String, Integer> three = twinTree.getRootNode1().getChildren().get(1).getChildren().get(0);
        final TreeNode<String, Integer> ten = twinTree.getRootNode1().getChildren().get(1).getChildren().get(1);
        final TreeNode<String, Integer> one = twinTree.getRootNode1().getChildren().get(2).getChildren().get(0);
        final TreeNode<String, Integer> nine = twinTree.getRootNode1().getChildren().get(2).getChildren().get(1);

        B.delete(nine);

        assertArrayEquals(new TreeNode[] { six, eight },
            twinTree.getRootNode1().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, ten },
            twinTree.getRootNode1().getChildren().get(1).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { one },
            twinTree.getRootNode1().getChildren().get(2).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { ten, eight },
            twinTree.getRootNode2().getChildren().get(0).getChildren().toArray());
        assertArrayEquals(new TreeNode[] { three, one, six },
            twinTree.getRootNode2().getChildren().get(1).getChildren().toArray());
    }

    /*
     * A test-case to search a tree node by a given value.
     */
    @Test
    @Order(8)
    void testSearch() {
        final List<TreeNode<String, Integer>> one = twinTree.search((B, L) -> Objects.equals(L, 1), ConjoinedTwinTree.LOCATION.ALL);
        final List<TreeNode<String, Integer>> nine = twinTree.search((B, L) -> Objects.equals(L, 9), ConjoinedTwinTree.LOCATION.LEAF);
        final List<TreeNode<String, Integer>> R = twinTree.search((B, L) -> Objects.equals("R", B),
            ConjoinedTwinTree.LOCATION.ALL);
        final List<TreeNode<String, Integer>> P = twinTree.search((B, L) -> Objects.equals("P", B),
            ConjoinedTwinTree.LOCATION.BRANCH);

        assertFalse(one.isEmpty());
        assertEquals(1, one.get(0).getLeafValue());
        assertTrue(nine.isEmpty());
        assertFalse(R.isEmpty());
        assertEquals("R", R.get(0).getBranchValue());
        assertTrue(P.isEmpty());
    }

    /*
     * A test-case to traverse branches from 2 different root node and assert results that return the list of branches
     * from both side.
     */
    @Test
    @Order(9)
    @SuppressWarnings("unchecked")
    void testTraverseBranch() {
        final List<TreeNode<String, Integer>> children1 = new LinkedList<>(
            twinTree.getRootNode1().getChildren());
        final List<TreeNode<String, Integer>> children2 = new LinkedList<>(
            twinTree.getRootNode2().getChildren());

        twinTree.getRootNode1().getChildren().removeAll(children1);
        twinTree.getRootNode2().getChildren().removeAll(children2);

        final TreeNode<String, Integer> X = twinTree.getRootNode1().addBranch("X");
        final TreeNode<String, Integer> Y = twinTree.getRootNode1().addBranch("Y");
        final TreeNode<String, Integer> Z = twinTree.getRootNode2().addBranch("Z");

        X.getChildren().addAll(children1);
        Z.getChildren().addAll(children2);
        X.getChildren().stream().map(BranchNode.class::cast).forEach(child -> child.setParent(X));
        Z.getChildren().stream().map(BranchNode.class::cast).forEach(child -> child.setParent(Z));

        final TreeNode<String, Integer> C = children1.get(0);
        final TreeNode<String, Integer> A = children1.get(1);
        final TreeNode<String, Integer> B = children1.get(2);
        final TreeNode<String, Integer> E = Y.addBranch("E");
        final TreeNode<String, Integer> D = Y.addBranch("D");
        final TreeNode<String, Integer> R = children2.get(0);
        final TreeNode<String, Integer> Q = children2.get(1);

        final List<List<TreeNode<String, Integer>>> rows = twinTree.traverseBranches(twinTree.getRootNode1());
        final List<List<TreeNode<String, Integer>>> cols = twinTree.traverseBranches(twinTree.getRootNode2());

        assertFalse(rows.isEmpty());
        assertFalse(cols.isEmpty());
        assertArrayEquals(new TreeNode[] { X, C }, rows.get(0).toArray());
        assertArrayEquals(new TreeNode[] { X, A }, rows.get(1).toArray());
        assertArrayEquals(new TreeNode[] { X, B }, rows.get(2).toArray());
        assertArrayEquals(new TreeNode[] { Y, E }, rows.get(3).toArray());
        assertArrayEquals(new TreeNode[] { Y, D }, rows.get(4).toArray());
        assertArrayEquals(new TreeNode[] { Z, R }, cols.get(0).toArray());
        assertArrayEquals(new TreeNode[] { Z, Q }, cols.get(1).toArray());
    }

    /*
     * A test-case to traverse the leaf nodes and returning the leaf nodes position with their respective branch
     * node location and asserting the location map is correct.
     */
    @Test
    @Order(10)
    void testTraverseLeaves() {
        final TreeNode<String, Integer> X = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> Y = twinTree.getRootNode1().getChildren().get(1);
        final TreeNode<String, Integer> Z = twinTree.getRootNode2().getChildren().get(0);
        final TreeNode<String, Integer> C = X.getChildren().get(0);
        final TreeNode<String, Integer> A = X.getChildren().get(1);
        final TreeNode<String, Integer> B = X.getChildren().get(2);
        final TreeNode<String, Integer> E = Y.getChildren().get(0);
        final TreeNode<String, Integer> D = Y.getChildren().get(1);
        final TreeNode<String, Integer> R = Z.getChildren().get(0);
        final TreeNode<String, Integer> Q = Z.getChildren().get(1);

        final TreeNode<String, Integer> six = C.getChildren().get(0);
        final TreeNode<String, Integer> eight = C.getChildren().get(1);
        final TreeNode<String, Integer> three = A.getChildren().get(0);
        final TreeNode<String, Integer> ten = A.getChildren().get(1);
        final TreeNode<String, Integer> one = B.getChildren().get(0);
        final TreeNode<String, Integer> twelve = E.addLeaf(12, R, false);
        final TreeNode<String, Integer> fifteen = E.addLeaf(15, Q, false);
        final TreeNode<String, Integer> sixteen = D.addLeaf(16, R, false);
        final TreeNode<String, Integer> twenty = D.addLeaf(20, Q, false);

        final List<List<TreeNode<String, Integer>>> rows = twinTree.traverseBranches(twinTree.getRootNode1());
        final Map<List<TreeNode<String, Integer>>, Map<List<TreeNode<String, Integer>>, TreeNode<String, Integer>>> leafMap = twinTree.traverseLeafs(
            rows);

        assertFalse(leafMap.isEmpty());
        assertEquals(six, leafMap.get(List.of(X, C)).get(List.of(Z, Q)));
        assertEquals(eight, leafMap.get(List.of(X, C)).get(List.of(Z, R)));
        assertEquals(three, leafMap.get(List.of(X, A)).get(List.of(Z, Q)));
        assertEquals(ten, leafMap.get(List.of(X, A)).get(List.of(Z, R)));
        assertEquals(one, leafMap.get(List.of(X, B)).get(List.of(Z, Q)));
        assertEquals(twelve, leafMap.get(List.of(Y, E)).get(List.of(Z, R)));
        assertEquals(fifteen, leafMap.get(List.of(Y, E)).get(List.of(Z, Q)));
        assertEquals(sixteen, leafMap.get(List.of(Y, D)).get(List.of(Z, R)));
        assertEquals(twenty, leafMap.get(List.of(Y, D)).get(List.of(Z, Q)));
    }

    /*
     * A test case to sort the leaf nodes children from one root branch and re-traverse the other branch backward to
     * get the reordered sorted branches then we assert that the reordered branches are in the right order.
     */
    @Test
    @Order(11)
    void testSortLeafsAscendingAndTraverse() {
        final List<List<TreeNode<String, Integer>>> cols = twinTree.traverseBranches(twinTree.getRootNode2());
        final List<List<TreeNode<String, Integer>>> rows = twinTree.traverseBranches(twinTree.getRootNode1());
        final TreeNode<String, Integer> R = ((LinkedList<TreeNode<String, Integer>>) cols.get(
            0)).getLast();

        final TreeNode<String, Integer> X = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> Y = twinTree.getRootNode1().getChildren().get(1);
        final TreeNode<String, Integer> C = X.getChildren().get(0);
        final TreeNode<String, Integer> A = X.getChildren().get(1);
        final TreeNode<String, Integer> B = X.getChildren().get(2);
        final TreeNode<String, Integer> E = Y.getChildren().get(0);
        final TreeNode<String, Integer> D = Y.getChildren().get(1);

        final TreeNode<String, Integer> ten = R.getChildren().get(0);
        final TreeNode<String, Integer> eight = R.getChildren().get(1);
        final TreeNode<String, Integer> twelve = R.getChildren().get(2);
        final TreeNode<String, Integer> sixteen = R.getChildren().get(3);

        final List<List<TreeNode<String, Integer>>> sortedRows = twinTree.sortLeafsAndTraverse(rows, R,
            Comparator.comparing(TreeNode::getLeafValue));

        assertFalse(sortedRows.isEmpty());
        assertArrayEquals(new TreeNode[] { eight, ten, twelve, sixteen }, R.getChildren().toArray());
        assertArrayEquals(new TreeNode[] { X, B }, sortedRows.get(0).toArray());
        assertArrayEquals(new TreeNode[] { X, C }, sortedRows.get(1).toArray());
        assertArrayEquals(new TreeNode[] { X, A }, sortedRows.get(2).toArray());
        assertArrayEquals(new TreeNode[] { Y, E }, sortedRows.get(3).toArray());
        assertArrayEquals(new TreeNode[] { Y, D }, sortedRows.get(4).toArray());
    }

    /*
     * A test-case to do different group sorting by each level in different order direction and assert that each level
     * is correctly sorted.
     */
    @Test
    @Order(12)
    void testGroupSorting() {
        final TreeNode<String, Integer> X = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> Y = twinTree.getRootNode1().getChildren().get(1);
        final TreeNode<String, Integer> C = X.getChildren().get(0);
        final TreeNode<String, Integer> A = X.getChildren().get(1);
        final TreeNode<String, Integer> B = X.getChildren().get(2);
        final TreeNode<String, Integer> E = Y.getChildren().get(0);
        final TreeNode<String, Integer> D = Y.getChildren().get(1);

        final TreeNode<String, Integer> Z = twinTree.getRootNode2().getChildren().get(0);
        final TreeNode<String, Integer> R = Z.getChildren().get(0);
        final TreeNode<String, Integer> Q = Z.getChildren().get(1);

        final TreeNode<String, Integer> six = C.getChildren().get(0);
        final TreeNode<String, Integer> eight = C.getChildren().get(1);
        final TreeNode<String, Integer> three = A.getChildren().get(0);
        final TreeNode<String, Integer> ten = A.getChildren().get(1);
        final TreeNode<String, Integer> one = B.getChildren().get(0);
        final TreeNode<String, Integer> twelve = E.getChildren().get(0);
        final TreeNode<String, Integer> fifteen = E.getChildren().get(1);
        final TreeNode<String, Integer> sixteen = D.getChildren().get(0);
        final TreeNode<String, Integer> twenty = D.getChildren().get(1);

        final Comparator<TreeNode<String, Integer>> ASC = (t1, t2) -> (t1.getBranchValue() != null)
            ? t1.getBranchValue().compareTo(t2.getBranchValue())
            : t1.getLeafValue().compareTo(t2.getLeafValue());
        final Comparator<TreeNode<String, Integer>> DESC = (t1, t2) -> (t1.getBranchValue() != null)
            ?
            t1.getBranchValue().compareTo(t2.getBranchValue()) * -1
            : (t1.getLeafValue()).compareTo(t2.getLeafValue()) * -1;

        twinTree.sort(twinTree.getRootNode1(), DESC, ASC, DESC);

        final List<List<TreeNode<String, Integer>>> cols = twinTree.traverseBranches(twinTree.getRootNode2());
        final List<List<TreeNode<String, Integer>>> rows = twinTree.traverseBranches(twinTree.getRootNode1());

        assertArrayEquals(new TreeNode[] { Y, D }, rows.get(0).toArray());
        assertArrayEquals(new TreeNode[] { Y, E }, rows.get(1).toArray());
        assertArrayEquals(new TreeNode[] { X, A }, rows.get(2).toArray());
        assertArrayEquals(new TreeNode[] { X, B }, rows.get(3).toArray());
        assertArrayEquals(new TreeNode[] { X, C }, rows.get(4).toArray());
        assertArrayEquals(new TreeNode[] { twenty, sixteen }, D.getChildren().toArray());
        assertArrayEquals(new TreeNode[] { fifteen, twelve }, E.getChildren().toArray());
        assertArrayEquals(new TreeNode[] { ten, three }, A.getChildren().toArray());
        assertArrayEquals(new TreeNode[] { one }, B.getChildren().toArray());
        assertArrayEquals(new TreeNode[] { eight, six }, C.getChildren().toArray());

        List<List<TreeNode<String, Integer>>> sortedCols = twinTree.getSortedBranches(cols, D);

        assertArrayEquals(new TreeNode[] { Z, Q }, sortedCols.get(0).toArray());
        assertArrayEquals(new TreeNode[] { Z, R }, sortedCols.get(1).toArray());

        sortedCols = twinTree.getSortedBranches(cols, B);

        assertArrayEquals(new TreeNode[] { Z, R }, sortedCols.get(0).toArray());
        assertArrayEquals(new TreeNode[] { Z, Q }, sortedCols.get(1).toArray());
    }

    /*
     * A test-case to filter the tree node by setting the branch and leaf node visibility then assert if we can't find
     * the node after it's marked as not visible, and we also assert that when branch node is marked as invisible, the
     * invisibility will trickle down to its descendants.
     */
    @Test
    @Order(13)
    void testTreeFilter() {
        twinTree.filter(treeNode -> !Integer.valueOf(12).equals(treeNode.getLeafValue()),
            ConjoinedTwinTree.LOCATION.LEAF);
        final List<TreeNode<String, Integer>> twelve = twinTree.search((B, L) -> Objects.equals(L, 12),
            ConjoinedTwinTree.LOCATION.LEAF);

        assertTrue(twelve.isEmpty());

        twinTree.filter(treeNode -> !"A".equals(treeNode.getBranchValue()), ConjoinedTwinTree.LOCATION.BRANCH);
        final List<TreeNode<String, Integer>> A = twinTree.search((B, L) -> Objects.equals("A", B),
            ConjoinedTwinTree.LOCATION.BRANCH);
        final List<TreeNode<String, Integer>> ten = twinTree.search((B, L) -> Objects.equals(L, 10), ConjoinedTwinTree.LOCATION.LEAF);
        final List<TreeNode<String, Integer>> three = twinTree.search((B, L) -> Objects.equals(L, 3),
            ConjoinedTwinTree.LOCATION.LEAF);

        assertTrue(A.isEmpty());
        assertTrue(ten.isEmpty());
        assertTrue(three.isEmpty());

        final List<List<TreeNode<String, Integer>>> rows = twinTree.traverseBranches(twinTree.getRootNode1());
        final List<List<TreeNode<String, Integer>>> cols = twinTree.traverseBranches(twinTree.getRootNode2());
        final Map<List<TreeNode<String, Integer>>, Map<List<TreeNode<String, Integer>>, TreeNode<String, Integer>>> leafNodesMap = twinTree.traverseLeafs(
            cols);

        assertEquals(4, rows.size());
        assertEquals(2, leafNodesMap.get(cols.get(0)).size());
        assertEquals(4, leafNodesMap.get(cols.get(1)).size());
    }

    @Test
    @Order(14)
    void testBranchNodeGetAccumulatedValue() {
        final TreeNode<String, Integer> Y = twinTree.getRootNode1().getChildren().get(0);
        final TreeNode<String, Integer> X = twinTree.getRootNode1().getChildren().get(1);
        final TreeNode<String, Integer> A = X.getChildren().get(0);
        final TreeNode<String, Integer> B = X.getChildren().get(1);
        final TreeNode<String, Integer> C = X.getChildren().get(2);
        final TreeNode<String, Integer> D = Y.getChildren().get(0);
        final TreeNode<String, Integer> E = Y.getChildren().get(1);

        final TreeNode<String, Integer> Z = twinTree.getRootNode2().getChildren().get(0);
        final TreeNode<String, Integer> R = Z.getChildren().get(0);
        final TreeNode<String, Integer> Q = Z.getChildren().get(1);

        final BranchAccumulator<String, Integer> sumAccumulator = (leafNodes, parentBranch) -> leafNodes.stream().map(
            TreeNode::getLeafValue).reduce(0, Integer::sum);
        final Integer XQSubtotal = ((BranchNode<String, Integer>) X).getAccumulatedValues(sumAccumulator, Q);
        final Integer XRSubtotal = ((BranchNode<String, Integer>) X).getAccumulatedValues(sumAccumulator, R);
        final Integer YQSubtotal = ((BranchNode<String, Integer>) Y).getAccumulatedValues(sumAccumulator, Q);
        final Integer YRSubtotal = ((BranchNode<String, Integer>) Y).getAccumulatedValues(sumAccumulator, R);
        final Integer ZASubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, A);
        final Integer ZBSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, B);
        final Integer ZCSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, C);
        final Integer ZDSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, D);
        final Integer ZESubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, E);

        assertEquals(7, XQSubtotal);
        assertEquals(8, XRSubtotal);
        assertEquals(35, YQSubtotal);
        assertEquals(16, YRSubtotal);
        assertNull(ZASubtotal);
        assertEquals(1, ZBSubtotal);
        assertEquals(14, ZCSubtotal);
        assertEquals(36, ZDSubtotal);
        assertEquals(15, ZESubtotal);

        final TreeNode<String, Integer> S = Z.addBranch("S");
        S.addLeaf(11, B, true);
        S.addLeaf(19, C, false);
        S.addLeaf(14, D, true);
        S.addLeaf(2, E, false);

        final Integer XSSubtotal = ((BranchNode<String, Integer>) X).getAccumulatedValues(sumAccumulator, S);
        final Integer YSSubtotal = ((BranchNode<String, Integer>) Y).getAccumulatedValues(sumAccumulator, S);
        final Integer newZBSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, B);
        final Integer newZCSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, C);
        final Integer newZDSubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, D);
        final Integer newZESubtotal = ((BranchNode<String, Integer>) Z).getAccumulatedValues(sumAccumulator, E);

        assertEquals(30, XSSubtotal);
        assertEquals(16, YSSubtotal);
        assertEquals(12, newZBSubtotal);
        assertEquals(33, newZCSubtotal);
        assertEquals(50, newZDSubtotal);
        assertEquals(17, newZESubtotal);
    }
}
