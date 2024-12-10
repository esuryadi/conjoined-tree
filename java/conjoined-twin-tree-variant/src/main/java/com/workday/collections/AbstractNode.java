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
 * @param <T> Tree node value type
 *
 * @see BranchNode
 * @see LeafNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
abstract class AbstractNode<T>
    implements TreeNode {

    /** Branch or Leaf Node value */
    protected final T value;

    /** The children of Root or Branch Node */
    protected final List<TreeNode> children = new LinkedList<>();

    /** Include or exclude this node from tree search or traversal */
    protected boolean visible;

    /**
     * Creates an instance of a Tree node.
     */
    AbstractNode(final T value) {
        this.value = value;
        this.visible = false;
    }

    /**
     * Creates an instance of a Tree node for a given value and visibility.
     *
     * @param visible True if Tree node is visible, false otherwise
     */
    AbstractNode(final T value, final boolean visible) {
        this.value = value;
        this.visible = visible;
    }

    /**
     * Sets the Tree node visibility.
     *
     * @param visible True if Tree node is visible, false otherwise
     */
    @SuppressWarnings("unchecked")
    public void setVisible(final boolean visible) {
        if (this instanceof LeafNode) {
            setUpdated((BranchNode<T>) ((LeafNode<T>) this).getParent1());
            setUpdated((BranchNode<T>) ((LeafNode<T>) this).getParent2());
        }
        this.visible = visible;
    }

    @Override
    public <V> TreeNode addBranch(final V value) {
        final BranchNode<V> branchNode = new BranchNode<>(value, this);
        this.children.add(branchNode);

        return branchNode;
    }

    @Override
    public <V> TreeNode addBranch(final V value, final int index) {
        final BranchNode<V> branchNode = new BranchNode<>(value, this);
        this.children.add(index, branchNode);

        return branchNode;
    }

    @Override
    public <V> TreeNode addLeaf(final V value, final TreeNode parentNode, final boolean updated) {
        final LeafNode<V> leafNode = new LeafNode<>(value, this, parentNode);
        this.children.add(leafNode);
        parentNode.getChildren().add(leafNode);
        if (updated) {
            setUpdated((BranchNode<?>) leafNode.getParent1());
            setUpdated((BranchNode<?>) leafNode.getParent2());
        }

        return leafNode;
    }

    @Override
    public <V> TreeNode addLeaf(final V value,
                                final TreeNode parentNode,
                                final int index,
                                final boolean updated) {
        final LeafNode<V> leafNode = new LeafNode<>(value, this, parentNode);
        this.children.add(index, leafNode);
        parentNode.getChildren().add(leafNode);
        if (updated) {
            setUpdated((BranchNode<?>) leafNode.getParent1());
            setUpdated((BranchNode<?>) leafNode.getParent2());
        }

        return leafNode;
    }

    @Override
    public TreeNode delete(final TreeNode deletedNode) {
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
    protected TreeNode delete(final TreeNode parentNode, final TreeNode deletedNode) {
        // Only delete when node is visible and deleted node exists in the children list
        if (deletedNode.isVisible() && parentNode.getChildren().contains(deletedNode)) {
            if (deletedNode instanceof BranchNode) {
                // Delete all branch node descendants
                if (!parentNode.getChildren().isEmpty()) {
                    final Deque<TreeNode> children = new ArrayDeque<>(deletedNode.getChildren());
                    while (!children.isEmpty()) {
                        delete(deletedNode, children.poll());
                    }
                }
                parentNode.getChildren().remove(deletedNode);
            }
            else if (deletedNode instanceof final LeafNode<?> leafNode) {
                // If leaf parent1 == parent, the parent2 node is the leaf parent2 and vice versa
                final TreeNode parent2Node = (leafNode.getParent1() == parentNode)
                    ? leafNode.getParent2()
                    : leafNode.getParent1();
                setUpdated((BranchNode<?>) parentNode);
                setUpdated((BranchNode<?>) parent2Node);
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
    public void sort(final Comparator<TreeNode> comparator) {
        this.children.sort(comparator);
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public List<TreeNode> getChildren() {
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
    protected void setUpdated(final BranchNode<?> branchNode) {
        final TreeNode parentNode = (branchNode.getParents().isEmpty())
            ? branchNode
            : branchNode.getParents().get(0);

        final Deque<TreeNode> branchNodeQueue = new ArrayDeque<>(Collections.singletonList(parentNode));
        while (!branchNodeQueue.isEmpty()) {
            final TreeNode branch = branchNodeQueue.poll();
            ((BranchNode<?>) branch).setUpdated(true);
            branch.getChildren().stream().findFirst().ifPresent(child -> {
                if (child instanceof BranchNode) {
                    branchNodeQueue.addAll(branch.getChildren());
                }
            });
        }
    }

}
