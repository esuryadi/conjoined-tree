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
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * <strong>AbstractNode</strong> class is an abstract class for all the tree nodes that has common implementation of
 * the {@link TreeNode} interface.
 *
 * @param <B> Branch node value type
 * @param <L> Leaf node value type
 *
 * @see BranchNode
 * @see LeafNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
abstract class AbstractNode<B, L>
    implements TreeNode<B, L> {

    /** The children of Root or Branch Node */
    protected final List<TreeNode<B, L>> children = new LinkedList<>();

    /** Include or exclude this node from tree search or traversal */
    protected boolean visible;

    /**
     * Creates an instance of a Tree node.
     */
    AbstractNode() {
        this.visible = false;
    }

    /**
     * Creates an instance of a Tree node for a given value and visibility.
     *
     * @param visible True if Tree node is visible, false otherwise
     */
    AbstractNode(final boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the Tree node visibility.
     *
     * @param visible True if Tree node is visible, false otherwise
     */
    public void setVisible(final boolean visible) {
        if (this instanceof LeafNode) {
            setUpdated((BranchNode<B, L>) ((LeafNode<B, L>) this).getParent1());
            setUpdated((BranchNode<B, L>) ((LeafNode<B, L>) this).getParent2());
        }
        this.visible = visible;
    }

    @Override
    public TreeNode<B, L> addBranch(final B value) {
        final BranchNode<B, L> branchNode = new BranchNode<>(value, this);
        this.children.add(branchNode);

        return branchNode;
    }

    @Override
    public TreeNode<B, L> addBranch(final B value, final int index) {
        final BranchNode<B, L> branchNode = new BranchNode<>(value, this);
        this.children.add(index, branchNode);

        return branchNode;
    }

    @Override
    public TreeNode<B, L> addLeaf(final L value, final TreeNode<B, L> parentNode, final boolean updated) {
        final LeafNode<B, L> leafNode = new LeafNode<>(value, this, parentNode);
        this.children.add(leafNode);
        parentNode.getChildren().add(leafNode);
        if (updated) {
            setUpdated((BranchNode<B, L>) leafNode.getParent1());
            setUpdated((BranchNode<B, L>) leafNode.getParent2());
        }

        return leafNode;
    }

    @Override
    public TreeNode<B, L> addLeaf(final L value,
                                  final TreeNode<B, L> parentNode,
                                  final int index,
                                  final boolean updated) {
        final LeafNode<B, L> leafNode = new LeafNode<>(value, this, parentNode);
        this.children.add(index, leafNode);
        parentNode.getChildren().add(leafNode);
        if (updated) {
            setUpdated((BranchNode<B, L>) leafNode.getParent1());
            setUpdated((BranchNode<B, L>) leafNode.getParent2());
        }

        return leafNode;
    }

    @Override
    public TreeNode<B, L> delete(final TreeNode<B, L> deletedNode) {
        return delete(this, deletedNode);
    }

    /**
     * Delete a child node for a given parent node.
     *
     * @param parentNode Parent node
     * @param deletedNode Deleted child node
     *
     * @return Deleted node
     */
    protected TreeNode<B, L> delete(final TreeNode<B, L> parentNode, final TreeNode<B, L> deletedNode) {
        // Only delete when node is visible and deleted node exists in the children list
        if (deletedNode.isVisible() && parentNode.getChildren().contains(deletedNode)) {
            if (deletedNode instanceof BranchNode) {
                // Delete all branch node descendants
                if (!parentNode.getChildren().isEmpty()) {
                    final Deque<TreeNode<B, L>> children = new ArrayDeque<>(deletedNode.getChildren());
                    while (!children.isEmpty()) {
                        delete(deletedNode, children.poll());
                    }
                }
                parentNode.getChildren().remove(deletedNode);
            }
            else if (deletedNode instanceof LeafNode) {
                final LeafNode<B, L> leafNode = (LeafNode<B, L>) deletedNode;
                // If leaf parent1 == parent, the parent2 node is the leaf parent2 and vice versa
                final TreeNode<B, L> parent2Node = (leafNode.getParent1() == parentNode)
                    ? leafNode.getParent2()
                    : leafNode.getParent1();
                setUpdated((BranchNode<B, L>) parentNode);
                setUpdated((BranchNode<B, L>) parent2Node);
                parentNode.getChildren().remove(leafNode);
                // Delete leaf node from the other parent as well
                parent2Node.getChildren().remove(leafNode);
            }
            else {
                throw new UnsupportedOperationException("RootNode cannot be deleted");
            }
        }

        return deletedNode;
    }

    @Override
    public void sort(final Comparator<TreeNode<B, L>> comparator) {
        this.children.sort(comparator);
    }

    @Override
    public B getBranchValue() {
        return (this instanceof BranchNode) ? ((BranchNode<B, L>) this).getValue() : null;
    }

    @Override
    public L getLeafValue() {
        return (this instanceof LeafNode) ? ((LeafNode<B, L>) this).getValue() : null;
    }

    @Override
    public List<TreeNode<B, L>> getChildren() {
        return this.children;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Marks all parents, grandparents and siblings as updated to reset the accumulated value map in all related branches.
     *
     * @param branchNode Parent of the leaf node
     */
    protected void setUpdated(final BranchNode<B, L> branchNode) {
        final TreeNode<B, L> parentNode = (branchNode.getParents().isEmpty())
            ? branchNode
            : branchNode.getParents().get(0);

        final Deque<TreeNode<B, L>> branchNodeQueue = new ArrayDeque<>(Collections.singletonList(parentNode));
        while (!branchNodeQueue.isEmpty()) {
            final TreeNode<B, L> branch = branchNodeQueue.poll();
            ((BranchNode<B, L>) branch).setUpdated(true);
            branch.getChildren().stream().findFirst().ifPresent(child -> {
                if (child instanceof BranchNode) {
                    branchNodeQueue.addAll(branch.getChildren());
                }
            });
        }
    }

}
