/*
 * Copyright (c) 2024, Workday, Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the Apache License version 2.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the Apache License version 2 for
 * more details (a copy is included in the LICENSE file that accompanied this code).
 *
 * Please contact Workday, 6110 Stoneridge Mall Road Pleasanton, CA 94588 USA
 * or visit www.workday.com if you need additional information or have any
 * questions.
 */
package com.workday.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
 * <strong>ConjoinedTwinTree</strong> class is a graph data structure that consists of 2 tree data structures which
 * has separate root and branch nodes but leaf nodes are shared by the 2 trees. ConjoinedTwinTree data structure is
 * used to store multidimensional data in 2-dimensional form, for example a Pivot Table. This class consists of 2
 * root nodes which gives a starting point to add branch nodes and leaf nodes and has method operations to traverse,
 * search, sort and filter throughout the tree structure.
 * <p>
 * <strong>Usage Example:</strong>
 * <pre>
 *     ConjoinedTwinTree twinTree = new ConjoinedTwinTree();
 *     TreeNode a = twinTree.getRootNode1().addBranch("A");
 *     TreeNode b = twinTree.getRootNode1().addBranch("B");
 *     TreeNode p = twinTree.getRootNode2().addBranch("P");
 *     TreeNode q = twinTree.getRootNode2().addBranch("Q");
 *     a.addLeaf(1, p);
 *     a.addLeaf(2, q);
 *     b.addLeaf(3, p);
 *     b.addLeaf(4, q);
 * </pre>
 * The code example above will generate ConjoinedTwinTree as follow:
 * <pre>
 *                root
 *               /   \
 *             "P" - "Q"
 *              |     |
 *       "A" -- 1 --- 2
 *      / |     |     |
 * root   |     |     |
 *      \ |     |     |
 *       "B" -- 3 --- 4
 * </pre>
 *
 * @see TreeNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
public class ConjoinedTwinTree {

    private final TreeNode rootNode1;
    private final TreeNode rootNode2;

    /** Conjoined Twin Tree Nodes Location */
    public enum LOCATION {
        /** Branch and Leaf Nodes */
        ALL,
        /** Branch Nodes Only */
        BRANCH,
        /** Leaf Nodes Only */
        LEAF
    }

    /**
     * Creates an instance of ConjoinedTwinTree.
     */
    public ConjoinedTwinTree() {
        this.rootNode1 = new BranchNode<>();
        this.rootNode2 = new BranchNode<>();
    }

    /**
     * Gets the first root node.
     *
     * @return First root node
     */
    public TreeNode getRootNode1() {
        return this.rootNode1;
    }

    /**
     * Gets the second root node.
     *
     * @return Second root node
     */
    public TreeNode getRootNode2() {
        return this.rootNode2;
    }

    /**
     * Search tree node value(s) from the tree hierarchy in parallel from both root nodes.
     *
     * @param valueMatcher The Tree Node value matcher function
     * @param location {@link LOCATION} within the tree structure
     *
     * @return List of Tree Nodes if found, otherwise, it will return an empty list
     */
    public List<TreeNode> search(final Predicate<TreeNode> valueMatcher, final LOCATION location) {
        final Optional<Set<TreeNode>> visited = (location == LOCATION.ALL)
            ? Optional.of(Collections.synchronizedSet(new HashSet<>()))
            : Optional.empty();
        if (location == LOCATION.LEAF) {
            return search(valueMatcher, this.rootNode1, visited, location);
        }
        else {
            return Stream.of(this.rootNode1, this.rootNode2).parallel().flatMap(
                node -> search(valueMatcher, node, visited, location).stream()).collect(
                Collectors.toList());
        }
    }

    /**
     * Search tree node value(s) from the tree hierarchy from a given Tree Node.
     *
     * @param valueMatcher The Tree Node value matcher function
     * @param startNode The starting Tree Node
     * @param visited True if node has been visited during the search. False otherwise
     * @param location {@link LOCATION} within the tree structure
     *
     * @return a Tree Node if found, otherwise, it will return null
     */
    protected List<TreeNode> search(final Predicate<TreeNode> valueMatcher,
                                    final TreeNode startNode,
                                    final Optional<Set<TreeNode>> visited,
                                    final LOCATION location) {
        final List<TreeNode> searchResults = new ArrayList<>();
        final Deque<TreeNode> treeNodes = new ArrayDeque<>(startNode.getChildren());
        // Performs a breadth-first-search algorithm by traversing through the children of branch node on each level.
        while (!treeNodes.isEmpty()) {
            final TreeNode treeNode = treeNodes.poll();
            // If the node has been visited by other search thread we can skip it.
            if (visited.isEmpty() || !visited.get().contains(treeNode)) {
                visited.ifPresent(visit -> visit.add(treeNode));
                // If the node is not visible we can skip it.
                if (treeNode.isVisible()) {
                    if (valueMatcher.test(treeNode)) {
                        searchResults.add(treeNode);
                    }
                    // Add the children to the queue if exists.
                    final List<TreeNode> children = treeNode.getChildren();
                    children.stream().findFirst().ifPresent(child -> {
                        if (location == LOCATION.ALL || (location == LOCATION.BRANCH && child instanceof BranchNode)
                            || location == LOCATION.LEAF) {
                            treeNodes.addAll(children);
                        }
                    });
                }
            }
        }

        return searchResults;
    }

    /**
     * Filter the ConjoinedTwinTree Tree Nodes from being traversed, searched and deleted by marking the visibility of
     * the Tree node.
     *
     * @param visibility Predicate function to determine the tree node visibility
     * @param location {@link LOCATION} within the tree structure
     */
    public void filter(final Predicate<TreeNode> visibility, final LOCATION location) {
        final Optional<Set<TreeNode>> visited = (location == LOCATION.ALL)
            ? Optional.of(Collections.synchronizedSet(new HashSet<>()))
            : Optional.empty();
        if (location == LOCATION.LEAF) {
            filter(visibility, this.rootNode1, visited, location);
        }
        else {
            Stream.of(this.rootNode1, this.rootNode2).parallel().forEach(
                node -> filter(visibility, node, visited, location));
        }
    }

    /**
     * Filter the ConjoinedTwinTree Tree Nodes from being traversed, searched and deleted by marking the visibility of
     * the Tree node starting from a given Tree node.
     *
     * @param visibility Predicate function to determine the tree node visibility
     * @param startNode The starting Tree node
     * @param visited True if node has been visited during the search. False otherwise
     * @param location {@link LOCATION} within the tree structure
     */
    protected void filter(final Predicate<TreeNode> visibility,
                          final TreeNode startNode,
                          final Optional<Set<TreeNode>> visited,
                          final LOCATION location) {
        final Deque<TreeNode> treeNodes = new ArrayDeque<>(startNode.getChildren());
        // Going through the tree node children on each level and setting the tree node visibility
        while (!treeNodes.isEmpty()) {
            final AbstractNode<?> treeNode = (AbstractNode<?>) treeNodes.poll();
            if (!visited.isPresent() || !visited.get().contains(treeNode)) {
                visited.ifPresent(visit -> visit.add(treeNode));
                treeNode.setVisible(visibility.test(treeNode));
                // If the tree node is not visible, mark all tree node descendants to be invisible.
                // Otherwise, add the children to the queue.
                if (treeNode.isVisible()) {
                    treeNode.getChildren().stream().findFirst().ifPresent(child -> {
                        if (location == LOCATION.ALL || (location == LOCATION.BRANCH && child instanceof BranchNode)
                            || location == LOCATION.LEAF) {
                            treeNodes.addAll(treeNode.getChildren());
                        }
                    });
                }
                else {
                    setInvisibleTreeNodes(treeNode.getChildren(), visited);
                }
            }
        }
    }

    // Mark all tree descendants to be invisible
    private void setInvisibleTreeNodes(final List<TreeNode> children,
                                       final Optional<Set<TreeNode>> visited) {
        children.forEach(child -> {
            visited.ifPresent(visit -> visit.add(child));
            ((AbstractNode<?>) child).setVisible(false);
            if (!child.getChildren().isEmpty()) {
                setInvisibleTreeNodes(child.getChildren(), visited);
            }
        });
    }

    /**
     * Sort each level of the tree node children according to the number Comparator functions from the starting tree node..
     *
     * @param treeNode The starting tree node
     * @param comparators List of Sort Comparator function
     */
    @SafeVarargs
    public final void sort(final TreeNode treeNode, final Comparator<TreeNode>... comparators) {
        if (comparators != null) {
            sort(treeNode, comparators, 0);
        }
    }

    // Sort recursively up to the number of available comparator
    private void sort(final TreeNode treeNode, final Comparator<TreeNode>[] comparators, final int i) {
        if (i < comparators.length && !treeNode.getClass().equals(LeafNode.class)) {
            treeNode.sort(comparators[i]);
            treeNode.getChildren().forEach(child -> sort(child, comparators, i + 1));
        }
    }

    /**
     * Traverse the tree branches and transform the tree branch hierarchy into a list of branch node hierarchy in a table format.
     *
     * @param treeNode Starting tree node
     *
     * @return List of branch node hierarchy
     */
    public List<List<TreeNode>> traverseBranches(final TreeNode treeNode) {
        final List<List<TreeNode>> allBranches = new LinkedList<>();
        if (!treeNode.getChildren().isEmpty() && treeNode.getChildren().get(0) instanceof BranchNode) {
            treeNode.getChildren().forEach(childNode -> {
                // Skip traversing if child node is not visible
                if (childNode.isVisible()) {
                    traverseBranches(allBranches, new LinkedList<>(), childNode);
                }
            });
        }

        return allBranches;
    }

    // A recursive function to traverse the branch node hierarchy by its depth
    private void traverseBranches(final List<List<TreeNode>> allBranches,
                                  final List<TreeNode> branches,
                                  final TreeNode node) {
        branches.add(node);
        // If branch node has children, and they're not leaf nodes, traverse it recursively
        // Otherwise we're at the end of branch node and add them to the main list
        if (!node.getChildren().isEmpty() && node.getChildren().get(0) instanceof BranchNode) {
            node.getChildren().forEach(childNode -> {
                // Skip traversing if children node is not visible and add branches to the main list
                if (childNode.isVisible()) {
                    traverseBranches(allBranches, new LinkedList<>(branches), childNode);
                }
            });
        }
        else {
            // Add the branch node hierarchy when we hit the bottom of the tree
            allBranches.add(branches);
        }
    }

    /**
     * Traverse the tree leaf nodes and transform it into leaf node location map based on its branch node parents from
     * both root nodes.
     *
     * @param branches List or branch nodes hierarchy
     *
     * @return Leaf node location map to the two branch parents in the Conjoined Twin Tree
     */
    @SuppressWarnings("unchecked")
    public Map<List<TreeNode>, Map<List<TreeNode>, TreeNode>> traverseLeafs(final List<List<TreeNode>> branches) {
        // Iterate through its branch node hierarchy list
        return branches.stream().map(LinkedList.class::cast).map(branch -> {
            // Gets the last branch node in the hierarchy which is the parent of the leaf nodes.
            final TreeNode parent = ((BranchNode<?>) branch.getLast());
            // Iterate through the leaf node children
            final Map<List<TreeNode>, TreeNode> leafNodesMap = parent.getChildren().stream().map(
                LeafNode.class::cast).map(
                // If the leaf is not visible return null, otherwise, get the other parents of the leaf node which will
                // become the key in the leaf node map
                leaf -> (leaf.isVisible()) ? Pair.of(
                    (parent.equals(leaf.getParent1())) ? leaf.getParents2() : leaf.getParents1(),
                    leaf) : null).filter(Objects::nonNull).collect(
                LinkedHashMap::new, (map, value) -> map.put(value.getLeft(), value.getRight()), LinkedHashMap::putAll);
            return Pair.of(branch, leafNodesMap);
        }).collect(LinkedHashMap::new, (map, value) -> map.put(value.getLeft(), value.getRight()),
            LinkedHashMap::putAll);
    }

    /**
     * Sort the leaf children and do a backward traversal to get the list of reordered branches after leaf children are
     * sorted.
     *
     * @param unsortedBranches The unsorted branches
     * @param branch Last branch node
     * @param comparator Sort comparator function
     *
     * @return List of reordered branches after sorting
     */
    public List<List<TreeNode>> sortLeafsAndTraverse(final List<List<TreeNode>> unsortedBranches,
                                                     final TreeNode branch,
                                                     final Comparator<TreeNode> comparator) {
        branch.sort(comparator);
        return getSortedBranches(unsortedBranches, branch);
    }

    /**
     * Get the list of reordered branches from the sorted leaf nodes.
     *
     * @param unsortedBranches The unsorted branches
     * @param branchParent The branch parent of the sorted leaf nodes
     *
     * @return List of reordered branches of the other parents
     */
    public List<List<TreeNode>> getSortedBranches(final List<List<TreeNode>> unsortedBranches,
                                                  final TreeNode branchParent) {
        // Iterate through the sorted leaf children and backtrack to get the other parent of each leaf node
        final LinkedList<List<TreeNode>> sortedBranches = branchParent.getChildren().stream().map(
            LeafNode.class::cast).map(
            leaf -> (branchParent.equals(leaf.getParent1())) ? leaf.getParents2() : leaf.getParents1()).collect(
            LinkedList::new, List::add, List::addAll);
        // If the sorted branches has less count than unsorted branches, that means there are null values.
        // Therefore, we're going to add the null value branches in the beginning of sorted branches
        if (sortedBranches.size() < unsortedBranches.size()) {
            unsortedBranches.removeAll(sortedBranches);
            sortedBranches.addAll(0, unsortedBranches);
        }
        return sortedBranches;
    }
}
