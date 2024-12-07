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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <strong>LeafNode</strong> class is {@link TreeNode} implementation class that has 2 parents.
 *
 * @param <B> Branch node value type
 * @param <L> Leaf node value type
 *
 * @see AbstractNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
public class LeafNode<B, L>
    extends AbstractNode<B, L> {

    private TreeNode<B, L> parent1;
    private TreeNode<B, L> parent2;
    private final L value;

    /**
     * Creates an instance of LeafNode given a value and 2 parent nodes.
     *
     * @param value Leaf node value
     * @param parent1 First parent node
     * @param parent2 Second parent node
     */
    public LeafNode(final L value, final TreeNode<B, L> parent1, final TreeNode<B, L> parent2) {
        super(true);
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.value = value;
    }

    /**
     * Creates an instance of LeafNode given a value, visibility and 2 parent nodes.
     *
     * @param value Tree node value
     * @param visible True if leaf node is visible, false otherwise
     * @param parent1 First parent node
     * @param parent2 Second parent node
     */
    public LeafNode(final L value, final boolean visible, final TreeNode<B, L> parent1, final TreeNode<B, L> parent2) {
        super(visible);
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.value = value;
    }

    /**
     * Sets the first parent node.
     *
     * @param parent1 First parent node
     */
    public void setParent1(final TreeNode<B, L> parent1) {
        this.parent1 = parent1;
    }

    /**
     * Gets first parent node.
     *
     * @return First parent node
     */
    public TreeNode<B, L> getParent1() {
        return this.parent1;
    }

    /**
     * Sets the second parent node.
     *
     * @param parent2 Second parent node
     */
    public void setParent2(final TreeNode<B, L> parent2) {
        this.parent2 = parent2;
    }

    /**
     * Gets the second parent node.
     *
     * @return Second parent node
     */
    public TreeNode<B, L> getParent2() {
        return this.parent2;
    }

    /**
     * Gets the leaf node value
     *
     * @return Leaf node value
     */
    public L getValue() {
        return this.value;
    }

    /**
     * Gets the list of first parent nodes hierarchy.
     *
     * @return List of first parent nodes hierarchy
     */
    public List<TreeNode<B, L>> getParents1() {
        final LinkedList<TreeNode<B, L>> parents = new LinkedList<>();
        BranchNode<B, L> parent = (BranchNode<B, L>) this.parent1;
        // Backtracking up to the root node
        while (parent.getParent() != null) {
            parents.addFirst(parent);
            parent = (BranchNode<B, L>) parent.getParent();
        }

        return parents;
    }

    /**
     * Gets the list of second parent node hierarchy.
     *
     * @return List of second parent nodes hierarchy
     */
    public List<TreeNode<B, L>> getParents2() {
        final LinkedList<TreeNode<B, L>> parents = new LinkedList<>();
        BranchNode<B, L> parent = (BranchNode<B, L>) this.parent2;
        // Backtracking up to the root node
        while (parent.getParent() != null) {
            parents.addFirst(parent);
            parent = (BranchNode<B, L>) parent.getParent();
        }

        return parents;
    }

    @Override
    public TreeNode<B, L> addBranch(final B value) {
        throw new UnsupportedOperationException("addBranch is not supported operation in the LeafNode!");
    }

    @Override
    public TreeNode<B, L> addBranch(final B value, final int index) {
        throw new UnsupportedOperationException("addBranch is not supported operation in the LeafNode!");
    }

    @Override
    public TreeNode<B, L> addLeaf(final L value, final TreeNode<B, L> parentNode, final boolean updated) {
        throw new UnsupportedOperationException("addLeaf is not supported operation in the LeafNode!");
    }

    @Override
    public TreeNode<B, L> addLeaf(final L value,
                                  final TreeNode<B, L> parentNode,
                                  final int index,
                                  final boolean updated) {
        throw new UnsupportedOperationException("addLeaf is not supported operation in the LeafNode!");
    }

    @Override
    public TreeNode<B, L> delete(final TreeNode<B, L> deletedNode) {
        throw new UnsupportedOperationException("delete is not supported operation in the LeafNode!");
    }

    @Override
    public void sort(final Comparator<TreeNode<B, L>> comparator) {
        throw new UnsupportedOperationException("sort is not supported operation in the LeafNode!");
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
        final LeafNode<B, L> that = (LeafNode<B, L>) o;
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
