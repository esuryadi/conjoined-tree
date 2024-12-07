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

import java.util.List;

/**
 * <strong>BranchAccumulator</strong> is a {@link BranchNode} accumulator interface to aggregate all the {@link LeafNode}
 * values for a given tree branch hierarchy at any level.
 *
 * @param <B> Branch node value
 * @param <L> Leaf node value
 *
 * @see BranchNode
 *
 * @author edward.suryadi
 * @since Mar-2024
 */
@FunctionalInterface
public interface BranchAccumulator<B, L> {

    /**
     * Aggregates all leaf node values for a given leaf nodes.
     *
     * @param leafNodes List of {@link LeafNode} that contain values for aggregation
     * @param parentBranch The parent {@link BranchNode} of the accumulated leafNodes
     *
     * @return A single aggregate value
     */
    L aggregateValues(List<TreeNode<B, L>> leafNodes, TreeNode<B, L> parentBranch);

}
