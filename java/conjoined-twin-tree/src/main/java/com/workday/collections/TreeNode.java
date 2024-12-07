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
import java.util.List;

/**
 * <strong>TreeNode</strong> interface is a common ConjoinTwinTree node operation interface that perform node level
 * operation such as addBranch, addLeaf, sort, etc.
 *
 * @param <B> Branch node value type
 * @param <L> Leaf node value type
 *
 * @see AbstractNode
 *
 * @author edward.suryadi
 * @since Jan-2024
 */
public interface TreeNode<B, L> {

    /**
     * Create a branch node for a given node value and add it as children of the parent branch node.
     *
     * @param value Branch node value
     *
     * @return {@link BranchNode}
     */
    TreeNode<B, L> addBranch(B value);

    /**
     * Create a branch node for a given node value and insert it as children at a given index position.
     *
     * @param value Branch node value
     * @param index Index location
     *
     * @return {@link BranchNode}
     */
    TreeNode<B, L> addBranch(B value, int index);

    /**
     * Create a leaf node for a given node value and add it as children of the two parent branch node.
     *
     * @param value Leaf node value
     * @param parentNode The other parent node
     * @param updated True if the tree has been modified from the original construction
     *
     * @return {@link LeafNode}
     */
    TreeNode<B, L> addLeaf(L value, TreeNode<B, L> parentNode, boolean updated);

    /**
     * Create a leaf node for a given node value and insert it as children at a given index position and add it as
     * a child of the other parent node.
     *
     * @param value Leaf node value
     * @param parentNode The other parent node
     * @param index Index location
     * @param updated True if the tree has been modified from the original construction
     *
     * @return {@link LeafNode}
     */
    TreeNode<B, L> addLeaf(L value, TreeNode<B, L> parentNode, int index, boolean updated);

    /**
     * Delete a tree node ({@link BranchNode} or {@link LeafNode} from its node parent. Note that root node cannot
     * be deleted from the tree.
     *
     * @param deletedNode Deleted tree node
     *
     * @return Deleted tree node
     */
    TreeNode<B, L> delete(TreeNode<B, L> deletedNode);

    /**
     * Sort the tree node (root node or {@link BranchNode}) children for a given Comparator function.
     *
     * @param comparator Sort {@link Comparator} Function
     */
    void sort(Comparator<TreeNode<B, L>> comparator);

    /**
     * Get the branch node value.
     *
     * @return Branch node value
     */
    B getBranchValue();

    /**
     * Get the leaf node value.
     *
     * @return Leaf node value
     */
    L getLeafValue();

    /**
     * Get the tree node children.
     *
     * @return Immutable List of Tree node children
     */
    List<TreeNode<B, L>> getChildren();

    /**
     * Check if tree node is visible or not.
     *
     * @return True if tree node is visible. False otherwise.
     */
    boolean isVisible();

}
