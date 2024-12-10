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
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

/**
 * <strong>BranchNode</strong> class is a {@link TreeNode} implementation class that has a parent and children node.
 *
 * @param <T> Tree node value type
 *
 * @see AbstractNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
public class BranchNode<T>
    extends AbstractNode<T> {

    private TreeNode parent;
    private boolean updated;
    private final Map<TreeNode, ? super Object> accumulatedValuesMap = new HashMap<>();

    /**
     * Creates an instance of BranchNode with no value.
     */
    public BranchNode() {
        super(null);
    }

    /**
     * Creates an instance of BranchNode for a given branch value.
     *
     * @param value Branch node value
     */
    public BranchNode(final T value) {
        super(value, true);
    }

    /**
     * Creates an instance of BranchNode for a given value and parent node.
     *
     * @param value Branch node value
     * @param parent Parent node
     */
    public BranchNode(final T value, final TreeNode parent) {
        super(value, true);
        this.parent = parent;
    }

    /**
     * Creates an instance of BranchNode for a given value, visibility and parent node.
     *
     * @param value Branch node value
     * @param visible True if branch node is visible, false otherwise
     * @param parent Parent node
     */
    public BranchNode(final T value, final boolean visible, final TreeNode parent) {
        super(value, visible);
        this.parent = parent;
    }

    /**
     * Sets the branch node parent.
     *
     * @param parent Parent node
     */
    public void setParent(final TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Gets the branch node parent.
     *
     * @return Parent node
     */
    public TreeNode getParent() {
        return this.parent;
    }

    /**
     * Sets the branch updated status.
     *
     * @param updated True if the branch has been updated. False otherwise.
     */
    public void setUpdated(final boolean updated) {
        this.updated = updated;
    }

    /**
     * Gets the branch node value.
     *
     * @return Branch node value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Gets the branch accumulated values for a given accumulator function.
     *
     * @param accumulator {@link BranchAccumulator} function
     * @param branch The other branch node of the accumulated values
     * @param <V> Accumulated Value Type
     *
     * @return A single leaf node value
     */
    @SuppressWarnings("unchecked")
    public <V> V getAccumulatedValues(final BranchAccumulator accumulator, final TreeNode branch) {
        // If the branch node has been updated, we reset the accumulated values map
        if (this.updated) {
            this.accumulatedValuesMap.clear();
        }
        // if accumulated values map is empty, we will compute the accumulated values
        if (this.accumulatedValuesMap.isEmpty()) {
            final Map<TreeNode, List<TreeNode>> accumulatedLeafNodesMap = new HashMap<>();
            final BiConsumer<TreeNode, LeafNode<V>> addToAccumulatedLeafNodesMap = (parent, child) -> {
                final TreeNode parentNode = (parent.equals(child.getParent1()))
                    ? child.getParent2()
                    : child.getParent1();
                accumulatedLeafNodesMap.compute(parentNode, (branchNode, leafNodes) -> {
                    if (leafNodes == null) {
                        leafNodes = new ArrayList<>();
                    }
                    if (child.isVisible()) {
                        leafNodes.add(child);
                    }
                    return leafNodes;
                });
            };
            // Queue all the branch children
            final Deque<Pair<TreeNode, TreeNode>> children = this.children.stream().map(
                child -> Pair.of((TreeNode) this, child)).collect(Collectors.toCollection(ArrayDeque::new));
            // Keep iterating as long as branch children queue not empty
            while (!children.isEmpty()) {
                final Pair<TreeNode, TreeNode> child = children.poll();
                // We only process if child is visible
                if (child.getRight().isVisible()) {
                    // If child is a leaf node, we just add the children to the list
                    // Otherwise, we add the descendants to the children queue
                    if (child.getRight() instanceof LeafNode) {
                        addToAccumulatedLeafNodesMap.accept(child.getLeft(), (LeafNode<V>) child.getRight());
                    }
                    else {
                        children.addAll(child.getRight().getChildren().stream().map(
                            grandchild -> Pair.of(child.getRight(), grandchild)).toList());
                    }
                }
            }
            accumulatedLeafNodesMap.forEach((branchNode, leafNodes) -> this.accumulatedValuesMap.put(branchNode,
                accumulator.aggregateValues(leafNodes, branchNode)));
            this.updated = false;
        }

        return (V) this.accumulatedValuesMap.get(branch);
    }

    /**
     * Gets the list of branch node parents hierarchy.
     *
     * @return List of branch node parents
     */
    public List<TreeNode> getParents() {
        final LinkedList<TreeNode> parents = new LinkedList<>();
        BranchNode<?> parent = (BranchNode<?>) this.parent;
        // Backtracking up to the root node
        while (parent.getParent() != null) {
            parents.addFirst(parent);
            parent = (BranchNode<?>) parent.getParent();
        }

        return parents;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BranchNode<?> that = (BranchNode<?>) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(this.value).map(Objects::toString).orElse(null);
    }
}
