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
 * @param <B> Branch node value type
 * @param <L> Leaf node value type
 *
 * @see AbstractNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
public class BranchNode<B, L>
    extends AbstractNode<B, L> {

    private TreeNode<B, L> parent;
    private boolean updated;
    private final B value;
    private final Map<TreeNode<B, L>, L> accumulatedValuesMap = new HashMap<>();

    /**
     * Creates an instance of BranchNode for a given branch value.
     */
    public BranchNode() {
        super();
        this.value = null;
    }

    /**
     * Creates an instance of BranchNode for a given value and parent node.
     *
     * @param value Branch node value
     * @param parent Parent node
     */
    public BranchNode(final B value, final TreeNode<B, L> parent) {
        super(true);
        this.parent = parent;
        this.value = value;
    }

    /**
     * Creates an instance of BranchNode for a given value, visibility and parent node.
     *
     * @param value Branch node value
     * @param visible True if branch node is visible, false otherwise
     * @param parent Parent node
     */
    public BranchNode(final B value, final boolean visible, final TreeNode<B, L> parent) {
        super(visible);
        this.parent = parent;
        this.value = value;
    }

    /**
     * Sets the branch node parent.
     *
     * @param parent Parent node
     */
    public void setParent(final TreeNode<B, L> parent) {
        this.parent = parent;
    }

    /**
     * Gets the branch node parent.
     *
     * @return Parent node
     */
    public TreeNode<B, L> getParent() {
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
    public B getValue() {
        return this.value;
    }

    /**
     * Gets the branch accumulated values for a given accumulator function.
     *
     * @param accumulator {@link BranchAccumulator} function
     * @param branch The other branch node of the accumulated values
     *
     * @return A single leaf node value
     */
    public L getAccumulatedValues(final BranchAccumulator<B, L> accumulator, final TreeNode<B, L> branch) {
        // If the branch node has been updated, we reset the accumulated values map
        if (this.updated) {
            this.accumulatedValuesMap.clear();
        }
        // if accumulated values map is empty, we will compute the accumulated values
        if (this.accumulatedValuesMap.isEmpty()) {
            final Map<TreeNode<B, L>, List<TreeNode<B, L>>> accumulatedLeafNodesMap = new HashMap<>();
            final BiConsumer<BranchNode<B, L>, LeafNode<B, L>> addToAccumulatedLeafNodesMap = (parent, child) -> {
                final TreeNode<B, L> parentNode = (parent.equals(child.getParent1()))
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
            final Deque<Pair<BranchNode<B, L>, TreeNode<B, L>>> children = this.children.stream().map(
                child -> Pair.of(this, child)).collect(Collectors.toCollection(ArrayDeque::new));
            // Keep iterating as long as branch children queue not empty
            while (!children.isEmpty()) {
                final Pair<BranchNode<B, L>, TreeNode<B, L>> child = children.poll();
                // We only process if child is visible
                if (child.getRight().isVisible()) {
                    // If child is a leaf node, we just add the children to the list
                    // Otherwise, we add the descendants to the children queue
                    if (child.getRight() instanceof LeafNode) {
                        addToAccumulatedLeafNodesMap.accept(child.getLeft(), (LeafNode<B, L>) child.getRight());
                    }
                    else {
                        children.addAll(child.getRight().getChildren().stream().map(
                            grandchild -> Pair.of((BranchNode<B, L>) child.getRight(), grandchild)).collect(
                            Collectors.toList()));
                    }
                }
            }
            accumulatedLeafNodesMap.forEach((branchNode, leafNodes) -> {
                this.accumulatedValuesMap.put(branchNode, accumulator.aggregateValues(leafNodes, branchNode));
            });
            this.updated = false;
        }

        return this.accumulatedValuesMap.get(branch);
    }

    /**
     * Gets the list of branch node parents hierarchy.
     *
     * @return List of branch node parents
     */
    public List<TreeNode<B, L>> getParents() {
        final LinkedList<TreeNode<B, L>> parents = new LinkedList<>();
        BranchNode<B, L> parent = (BranchNode<B, L>) this.parent;
        // Backtracking up to the root node
        while (parent.getParent() != null) {
            parents.addFirst(parent);
            parent = (BranchNode<B, L>) parent.getParent();
        }

        return parents;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BranchNode<B, L> that = (BranchNode<B, L>) o;
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
