package edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5;

import static edu.northeastern.info6205.tspsolver.jgrapht.alg.matching.blossom.v5.BlossomVNode.Label.*;

class BlossomVPrimalUpdater<V, E>
{
    
    private BlossomVState<V, E> state;

    
    public BlossomVPrimalUpdater(BlossomVState<V, E> state)
    {
        this.state = state;
    }

    
    public void grow(BlossomVEdge growEdge, boolean recursiveGrow, boolean immediateAugment)
    {
        long start = System.nanoTime();
        int initialTreeNum = state.treeNum;
        int dirToMinusNode = growEdge.head[0].isInfinityNode() ? 0 : 1;

        BlossomVNode nodeInTheTree = growEdge.head[1 - dirToMinusNode];
        BlossomVNode minusNode = growEdge.head[dirToMinusNode];
        BlossomVNode plusNode = minusNode.getOppositeMatched();

        nodeInTheTree.addChild(minusNode, growEdge, true);
        minusNode.addChild(plusNode, minusNode.matched, true);

        BlossomVNode stop = plusNode;

        while (true) {
            minusNode.label = MINUS;
            plusNode.label = PLUS;
            minusNode.isMarked = plusNode.isMarked = false;
            processMinusNodeGrow(minusNode);
            processPlusNodeGrow(plusNode, recursiveGrow, immediateAugment);
            if (initialTreeNum != state.treeNum) {
                break;
            }

            if (plusNode.firstTreeChild != null) {
                minusNode = plusNode.firstTreeChild;
                plusNode = minusNode.getOppositeMatched();
            } else {
                while (plusNode != stop && plusNode.treeSiblingNext == null) {
                    plusNode = plusNode.getTreeParent();
                }
                if (plusNode.isMinusNode()) {
                    minusNode = plusNode.treeSiblingNext;
                    plusNode = minusNode.getOppositeMatched();
                } else {
                    break;
                }
            }
        }
        state.statistics.growTime += System.nanoTime() - start;
    }

    
    public void augment(BlossomVEdge augmentEdge)
    {
        long start = System.nanoTime();

        // augment trees on both sides
        for (int dir = 0; dir < 2; dir++) {
            BlossomVNode node = augmentEdge.head[dir];
            augmentBranch(node, augmentEdge);
            node.matched = augmentEdge;
        }

        state.statistics.augmentTime += System.nanoTime() - start;
    }

    
    public BlossomVNode shrink(BlossomVEdge blossomFormingEdge, boolean immediateAugment)
    {
        long start = System.nanoTime();
        BlossomVNode blossomRoot = findBlossomRoot(blossomFormingEdge);
        BlossomVTree tree = blossomRoot.tree;
        
        BlossomVNode blossom = new BlossomVNode(state.nodeNum + state.blossomNum);
        // initialize blossom node
        blossom.tree = tree;
        blossom.isBlossom = true;
        blossom.isOuter = true;
        blossom.isTreeRoot = blossomRoot.isTreeRoot;
        blossom.dual = -tree.eps;
        if (blossom.isTreeRoot) {
            tree.root = blossom;
        } else {
            blossom.matched = blossomRoot.matched;
        }

        // mark all blossom nodes
        for (BlossomVEdge.BlossomNodesIterator iterator =
             blossomFormingEdge.blossomNodesIterator(blossomRoot); iterator.hasNext();)
        {
            iterator.next().isMarked = true;
        }

        // move edges and children, change slacks if necessary
        BlossomVEdge augmentEdge = updateTreeStructure(blossomRoot, blossomFormingEdge, blossom);

        // create circular linked list of circuit nodes
        setBlossomSiblings(blossomRoot, blossomFormingEdge);

        // reset marks of blossom nodes
        blossomRoot.isMarked = false;
        blossomRoot.isProcessed = false;
        for (BlossomVNode current = blossomRoot.blossomSibling.getOpposite(blossomRoot);
            current != blossomRoot; current = current.blossomSibling.getOpposite(current))
        {
            current.isMarked = false;
            current.isProcessed = false;
        }
        blossomRoot.matched = null; // now new blossom is matched (used when finishing the matching

        state.statistics.shrinkNum++;
        state.blossomNum++;

        state.statistics.shrinkTime += System.nanoTime() - start;
        if (augmentEdge != null && immediateAugment) {
            augment(augmentEdge);
        }
        return blossom;
    }

    
    public void expand(BlossomVNode blossom, boolean immediateAugment)
    {
        long start = System.nanoTime();

        BlossomVTree tree = blossom.tree;
        double eps = tree.eps;
        blossom.dual -= eps;
        blossom.tree.removeMinusBlossom(blossom); // it doesn't belong to the tree no more

        BlossomVNode branchesEndpoint =
            blossom.parentEdge.getCurrentOriginal(blossom).getPenultimateBlossom();

        // the node which is matched to the node from outside
        BlossomVNode blossomRoot =
            blossom.matched.getCurrentOriginal(blossom).getPenultimateBlossom();

        // mark blossom nodes
        BlossomVNode current = blossomRoot;
        do {
            current.isMarked = true;
            current = current.blossomSibling.getOpposite(current);
        } while (current != blossomRoot);

        // move all edge from blossom to penultimate children
        blossom.removeFromChildList();
        for (BlossomVNode.IncidentEdgeIterator iterator = blossom.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode penultimateChild = edge.headOriginal[1 - iterator.getDir()]
                .getPenultimateBlossomAndFixBlossomGrandparent();
            edge.moveEdgeTail(blossom, penultimateChild);
        }

        // reverse the circular blossomSibling references so that the first branch in even branch
        if (!forwardDirection(blossomRoot, branchesEndpoint)) {
            reverseBlossomSiblings(blossomRoot);
        }

        // change the matching, the labeling and the dual information on the odd branch
        expandOddBranch(blossomRoot, branchesEndpoint, tree);

        // change the matching, the labeling and dual information on the even branch
        BlossomVEdge augmentEdge = expandEvenBranch(blossomRoot, branchesEndpoint, blossom);

        // reset marks of blossom nodes
        current = blossomRoot;
        do {
            current.isMarked = false;
            current.isProcessed = false;
            current = current.blossomSibling.getOpposite(current);
        } while (current != blossomRoot);
        state.statistics.expandNum++;
        state.removedNum++;
        state.statistics.expandTime += System.nanoTime() - start;

        if (immediateAugment && augmentEdge != null) {
            augment(augmentEdge);
        }

    }

    
    private void processMinusNodeGrow(BlossomVNode minusNode)
    {
        double eps = minusNode.tree.eps;
        minusNode.dual += eps;

        // maintain heap of "-" blossoms
        if (minusNode.isBlossom) {
            minusNode.tree.addMinusBlossom(minusNode);
        }
        // maintain minus-plus edges in the minus-plus heaps in the tree edges
        for (BlossomVNode.IncidentEdgeIterator iterator = minusNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            edge.slack -= eps;
            if (opposite.isPlusNode()) {
                if (opposite.tree != minusNode.tree) {
                    // encountered (-,+) cross-tree edge
                    if (opposite.tree.currentEdge == null) {
                        BlossomVTree.addTreeEdge(minusNode.tree, opposite.tree);
                    }
                    opposite.tree.removePlusInfinityEdge(edge);
                    opposite.tree.currentEdge
                        .addToCurrentMinusPlusHeap(edge, opposite.tree.currentDirection);
                } else if (opposite != minusNode.getOppositeMatched()) {
                    // encountered a former (+, inf) edge
                    minusNode.tree.removePlusInfinityEdge(edge);
                }
            }
        }
    }

    
    private void processPlusNodeGrow(
        BlossomVNode node, boolean recursiveGrow, boolean immediateAugment)
    {
        double eps = node.tree.eps;
        node.dual -= eps;
        BlossomVEdge augmentEdge = null;
        for (BlossomVNode.IncidentEdgeIterator iterator = node.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            // maintain heap of plus-infinity edges
            edge.slack += eps;
            if (opposite.isPlusNode()) {
                // this is a (+,+) edge
                if (opposite.tree == node.tree) {
                    // this is blossom-forming edge
                    node.tree.removePlusInfinityEdge(edge);
                    node.tree.addPlusPlusEdge(edge);
                } else {
                    // this is plus-plus edge to another trees
                    if (opposite.tree.currentEdge == null) {
                        BlossomVTree.addTreeEdge(node.tree, opposite.tree);
                    }
                    opposite.tree.removePlusInfinityEdge(edge);
                    opposite.tree.currentEdge.addPlusPlusEdge(edge);
                    if (edge.slack <= node.tree.eps + opposite.tree.eps) {
                        augmentEdge = edge;
                    }
                }
            } else if (opposite.isMinusNode()) {
                // this is a (+,-) edge
                if (opposite.tree != node.tree) {
                    // this is (+,-) edge to another trees
                    if (opposite.tree.currentEdge == null) {
                        BlossomVTree.addTreeEdge(node.tree, opposite.tree);
                    }
                    opposite.tree.currentEdge
                        .addToCurrentPlusMinusHeap(edge, opposite.tree.currentDirection);
                }
            } else if (opposite.isInfinityNode()) {
                node.tree.addPlusInfinityEdge(edge);
                // this edge can be grown as well
                // it can be the case when this edge can't be grown because opposite vertex is
                // already added
                // to this tree via some other grow operation
                if (recursiveGrow && edge.slack <= eps && !edge.getOpposite(node).isMarked) {
                    BlossomVNode minusNode = edge.getOpposite(node);
                    BlossomVNode plusNode = minusNode.getOppositeMatched();
                    minusNode.isMarked = plusNode.isMarked = true;
                    node.addChild(minusNode, edge, true);
                    minusNode.addChild(plusNode, minusNode.matched, true);
                }
            }
        }
        if (immediateAugment && augmentEdge != null) {
            augment(augmentEdge);
        }
        state.statistics.growNum++;
    }

    
    private BlossomVEdge expandEvenBranch(
        BlossomVNode blossomRoot, BlossomVNode branchesEndpoint, BlossomVNode blossom)
    {
        BlossomVEdge augmentEdge = null;
        BlossomVTree tree = blossom.tree;
        blossomRoot.matched = blossom.matched;
        blossomRoot.tree = tree;
        blossomRoot.addChild(blossom.matched.getOpposite(blossomRoot), blossomRoot.matched, false);

        BlossomVNode current = blossomRoot;
        BlossomVNode prevNode = current;
        current.label = MINUS;
        current.isOuter = true;
        current.parentEdge = blossom.parentEdge;
        // first traversal. It is done from blossomRoot to branchesEndpoint, i.e. from higher
        // layers of the tree to the lower
        while (current != branchesEndpoint) {
            // process "+" node
            current = current.blossomSibling.getOpposite(current);
            current.label = PLUS;
            current.isOuter = true;
            current.tree = tree;
            current.matched = current.blossomSibling;
            BlossomVEdge prevMatched = current.blossomSibling;
            current.addChild(prevNode, prevNode.blossomSibling, false);
            prevNode = current;

            // process "-" node
            current = current.blossomSibling.getOpposite(current);
            current.label = MINUS;
            current.isOuter = true;
            current.tree = tree;
            current.matched = prevMatched;
            current.addChild(prevNode, prevNode.blossomSibling, false);
            prevNode = current;
        }
        blossom.parentEdge
            .getOpposite(branchesEndpoint).addChild(branchesEndpoint, blossom.parentEdge, false);

        // second traversal, update edge slacks and their presence in heaps
        current = blossomRoot;
        expandMinusNode(current);
        while (current != branchesEndpoint) {
            current = current.blossomSibling.getOpposite(current);
            BlossomVEdge edge = expandPlusNode(current);
            if (edge != null) {
                augmentEdge = edge;
            }
            current.isProcessed = true; // this is needed for correct processing of (+, +) edges
                                        // connecting two node on the branch

            current = current.blossomSibling.getOpposite(current);
            expandMinusNode(current);
        }
        return augmentEdge;
    }

    
    private void expandOddBranch(
        BlossomVNode blossomRoot, BlossomVNode branchesEndpoint, BlossomVTree tree)
    {
        BlossomVNode current = branchesEndpoint.blossomSibling.getOpposite(branchesEndpoint);
        // the traversal is done from branchesEndpoint to blossomRoot, i.e. from
        // lower layers to higher
        while (current != blossomRoot) {
            current.label = INFINITY;
            current.isOuter = true;
            current.tree = null;
            current.matched = current.blossomSibling;
            BlossomVEdge prevMatched = current.blossomSibling;
            expandInfinityNode(current, tree);
            current = current.blossomSibling.getOpposite(current);

            current.label = INFINITY;
            current.isOuter = true;
            current.tree = null;
            current.matched = prevMatched;
            expandInfinityNode(current, tree);
            current = current.blossomSibling.getOpposite(current);
        }
    }

    
    private BlossomVEdge expandPlusNode(BlossomVNode plusNode)
    {
        BlossomVEdge augmentEdge = null;
        double eps = plusNode.tree.eps; // the plusNode.tree is assumed to be correct
        plusNode.dual -= eps; // apply lazy delta spreading
        for (BlossomVNode.IncidentEdgeIterator iterator = plusNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            // update slack of the edge
            if (opposite.isMarked && opposite.isPlusNode()) {
                // this is an inner (+, +) edge
                if (!opposite.isProcessed) {
                    // we encounter this edge for the first time
                    edge.slack += 2 * eps;
                }
            } else if (!opposite.isMarked) {
                // this is boundary edge
                edge.slack += 2 * eps; // the endpoint changes its label to "+"
            } else if (!opposite.isMinusNode()) {
                // this edge is inner edge between even and odd branches or it is an inner (+, +)
                // edge
                edge.slack += eps;
            }
            // update its presence in the heap of edges
            if (opposite.isPlusNode()) {
                if (opposite.tree == plusNode.tree) {
                    // this edge becomes a (+, +) in-tree edge
                    if (!opposite.isProcessed) {
                        // if opposite.isProcessed = true => this is an inner (+, +) edge => its
                        // slack has been
                        // updated already and it has been added to the plus-plus edges heap already
                        plusNode.tree.addPlusPlusEdge(edge);
                    }
                } else {
                    // opposite is from another tree since it's label is "+"
                    opposite.tree.currentEdge.removeFromCurrentMinusPlusHeap(edge);
                    opposite.tree.currentEdge.addPlusPlusEdge(edge);
                    if (edge.slack <= eps + opposite.tree.eps) {
                        augmentEdge = edge;
                    }
                }
            } else if (opposite.isMinusNode()) {
                if (opposite.tree != plusNode.tree) {
                    // this edge becomes a (+, -) cross-tree edge
                    if (opposite.tree.currentEdge == null) {
                        BlossomVTree.addTreeEdge(plusNode.tree, opposite.tree);
                    }
                    opposite.tree.currentEdge
                        .addToCurrentPlusMinusHeap(edge, opposite.tree.currentDirection);
                }
            } else {
                // this is either an inner edge, that becomes a (+, inf) edge, or it is a former (-,
                // +) edge,
                // that also becomes a (+, inf) edge
                plusNode.tree.addPlusInfinityEdge(edge); // updating edge's key
            }
        }
        return augmentEdge;
    }

    
    private void expandMinusNode(BlossomVNode minusNode)
    {
        double eps = minusNode.tree.eps; // the minusNode.tree is assumed to be correct
        minusNode.dual += eps;
        if (minusNode.isBlossom) {
            minusNode.tree.addMinusBlossom(minusNode);
        }
        for (BlossomVNode.IncidentEdgeIterator iterator = minusNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            if (opposite.isMarked && !opposite.isPlusNode()) {
                // this is a (-, inf) or (-, -) inner edge
                edge.slack -= eps;
            }
        }
    }

    
    private void expandInfinityNode(BlossomVNode infinityNode, BlossomVTree tree)
    {
        double eps = tree.eps;
        for (BlossomVNode.IncidentEdgeIterator iterator = infinityNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            if (!opposite.isMarked) {
                edge.slack += eps; // since edge's label changes to inf and this is a boundary edge
                if (opposite.isPlusNode()) {
                    // if this node is marked => it's a blossom node => this edge has been processed
                    // already
                    if (opposite.tree != tree) {
                        opposite.tree.currentEdge.removeFromCurrentMinusPlusHeap(edge);
                    }
                    opposite.tree.addPlusInfinityEdge(edge);
                }
            }
        }
    }

    
    private void augmentBranch(BlossomVNode firstNode, BlossomVEdge augmentEdge)
    {
        BlossomVTree tree = firstNode.tree;
        double eps = tree.eps;
        BlossomVNode root = tree.root;

        // set currentEdge and currentDirection of all opposite trees connected via treeEdge
        tree.setCurrentEdges();

        // apply tree.eps to all tree nodes and updating slacks of all incident edges
        for (BlossomVTree.TreeNodeIterator treeNodeIterator = tree.treeNodeIterator();
             treeNodeIterator.hasNext();)
        {
            BlossomVNode node = treeNodeIterator.next();
            if (!node.isMarked) {
                // apply lazy delta spreading
                if (node.isPlusNode()) {
                    node.dual += eps;
                } else {
                    node.dual -= eps;
                }
                for (BlossomVNode.IncidentEdgeIterator incidentEdgeIterator =
                    node.incidentEdgesIterator(); incidentEdgeIterator.hasNext();)
                {
                    BlossomVEdge edge = incidentEdgeIterator.next();
                    int dir = incidentEdgeIterator.getDir();
                    BlossomVNode opposite = edge.head[dir];
                    BlossomVTree oppositeTree = opposite.tree;
                    if (node.isPlusNode()) {
                        edge.slack -= eps;
                        if (oppositeTree != null && oppositeTree != tree) {
                            // if this edge is a cross-tree edge
                            BlossomVTreeEdge treeEdge = oppositeTree.currentEdge;
                            if (opposite.isPlusNode()) {
                                // this is a (+,+) cross-tree edge
                                treeEdge.removeFromPlusPlusHeap(edge);
                                oppositeTree.addPlusInfinityEdge(edge);
                            } else if (opposite.isMinusNode()) {
                                // this is a (+,-) cross-tree edge
                                treeEdge.removeFromCurrentPlusMinusHeap(edge);
                            }
                        }
                    } else {
                        // current node is a "-" node
                        edge.slack += eps;
                        if (oppositeTree != null && oppositeTree != tree && opposite.isPlusNode()) {
                            // this is a (-,+) cross-tree edge
                            BlossomVTreeEdge treeEdge = oppositeTree.currentEdge;
                            treeEdge.removeFromCurrentMinusPlusHeap(edge);
                            oppositeTree.addPlusInfinityEdge(edge);
                        }

                    }
                }
                node.label = INFINITY;
            } else {
                // this node was added to the tree by the grow operation,
                // but it hasn't been processed, so we don't need to process it here
                node.isMarked = false;
            }
        }

        // add all elements from the (-,+) and (+,+) heaps to (+, inf) heaps of the opposite trees
        // and
        // delete tree edges
        for (BlossomVTree.TreeEdgeIterator treeEdgeIterator = tree.treeEdgeIterator();
             treeEdgeIterator.hasNext();)
        {
            BlossomVTreeEdge treeEdge = treeEdgeIterator.next();
            int dir = treeEdgeIterator.getCurrentDirection();
            BlossomVTree opposite = treeEdge.head[dir];
            opposite.currentEdge = null;

            opposite.plusPlusEdges.meld(treeEdge.plusPlusEdges);
            opposite.plusPlusEdges.meld(treeEdge.getCurrentMinusPlusHeap(dir));
            treeEdge.removeFromTreeEdgeList();
        }

        // update the matching
        BlossomVEdge matchedEdge = augmentEdge;
        BlossomVNode plusNode = firstNode;
        BlossomVNode minusNode = plusNode.getTreeParent();
        while (minusNode != null) {
            plusNode.matched = matchedEdge;
            matchedEdge = minusNode.parentEdge;
            minusNode.matched = matchedEdge;
            plusNode = minusNode.getTreeParent();
            minusNode = plusNode.getTreeParent();
        }
        root.matched = matchedEdge;

        // remove root from the linked list of roots;
        root.removeFromChildList();
        root.isTreeRoot = false;

        state.treeNum--;
    }

    
    private BlossomVEdge updateTreeStructure(
            BlossomVNode blossomRoot, BlossomVEdge blossomFormingEdge, BlossomVNode blossom)
    {
        BlossomVEdge augmentEdge = null;
        BlossomVTree tree = blossomRoot.tree;
        
        for (BlossomVEdge.BlossomNodesIterator iterator =
             blossomFormingEdge.blossomNodesIterator(blossomRoot); iterator.hasNext();)
        {
            BlossomVNode blossomNode = iterator.next();
            if (blossomNode != blossomRoot) {
                if (blossomNode.isPlusNode()) {
                    // substitute varNode with the blossom in the tree structure
                    blossomNode.removeFromChildList();
                    blossomNode.moveChildrenTo(blossom);
                    BlossomVEdge edge = shrinkPlusNode(blossomNode, blossom);
                    if (edge != null) {
                        augmentEdge = edge;
                    }
                    blossomNode.isProcessed = true;
                } else {
                    if (blossomNode.isBlossom) {
                        tree.removeMinusBlossom(blossomNode);
                    }
                    blossomNode.removeFromChildList(); // minus node have only one child and this
                                                       // child belongs to the circuit
                    shrinkMinusNode(blossomNode, blossom);
                }
            }
            blossomNode.blossomGrandparent = blossomNode.blossomParent = blossom;
        }
        // substitute varNode with the blossom in the tree structure
        blossomRoot.removeFromChildList();
        if (!blossomRoot.isTreeRoot) {
            blossomRoot.getTreeParent().addChild(blossom, blossomRoot.parentEdge, false);
        } else {
            // substitute blossomRoot with blossom in the linked list of tree roots
            blossom.treeSiblingNext = blossomRoot.treeSiblingNext;
            blossom.treeSiblingPrev = blossomRoot.treeSiblingPrev;
            blossomRoot.treeSiblingPrev.treeSiblingNext = blossom;
            if (blossomRoot.treeSiblingNext != null) {
                blossomRoot.treeSiblingNext.treeSiblingPrev = blossom;
            }
        }
        // finally process blossomRoot
        blossomRoot.moveChildrenTo(blossom);
        BlossomVEdge edge = shrinkPlusNode(blossomRoot, blossom);
        if (edge != null) {
            augmentEdge = edge;
        }
        blossomRoot.isTreeRoot = false;

        return augmentEdge;
    }

    
    private BlossomVEdge shrinkPlusNode(BlossomVNode plusNode, BlossomVNode blossom)
    {
        BlossomVEdge augmentEdge = null;
        BlossomVTree tree = plusNode.tree;
        double eps = tree.eps;
        plusNode.dual += eps;

        for (BlossomVNode.IncidentEdgeIterator iterator = plusNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];

            if (!opposite.isMarked) {
                // opposite isn't a node inside the blossom
                edge.moveEdgeTail(plusNode, blossom);
                if (opposite.tree != tree && opposite.isPlusNode()
                    && edge.slack <= eps + opposite.tree.eps)
                {
                    augmentEdge = edge;
                }
            } else if (opposite.isPlusNode()) {
                // inner edge, subtract eps only in the case the opposite node is a "+" node
                if (!opposite.isProcessed) { // here we rely on the proper setting of the
                                             // isProcessed flag
                    // remove this edge when it is encountered for the first time
                    tree.removePlusPlusEdge(edge);
                }
                edge.slack -= eps;
            }
        }
        return augmentEdge;
    }

    
    private void shrinkMinusNode(BlossomVNode minusNode, BlossomVNode blossom)
    {
        BlossomVTree tree = minusNode.tree;
        double eps = tree.eps;
        minusNode.dual -= eps;

        for (BlossomVNode.IncidentEdgeIterator iterator = minusNode.incidentEdgesIterator();
            iterator.hasNext();)
        {
            BlossomVEdge edge = iterator.next();
            BlossomVNode opposite = edge.head[iterator.getDir()];
            BlossomVTree oppositeTree = opposite.tree;

            if (!opposite.isMarked) {
                // opposite isn't a node inside the blossom
                edge.moveEdgeTail(minusNode, blossom);
                edge.slack += 2 * eps;
                if (opposite.tree == tree) {
                    // edge to the node from the same tree, need only to add it to "++" heap if
                    // opposite is "+" node
                    if (opposite.isPlusNode()) {
                        tree.addPlusPlusEdge(edge);
                    }
                } else {
                    // cross-tree edge or infinity edge
                    if (opposite.isPlusNode()) {
                        oppositeTree.currentEdge.removeFromCurrentMinusPlusHeap(edge);
                        oppositeTree.currentEdge.addPlusPlusEdge(edge);
                    } else if (opposite.isMinusNode()) {
                        if (oppositeTree.currentEdge == null) {
                            BlossomVTree.addTreeEdge(tree, oppositeTree);
                        }
                        oppositeTree.currentEdge
                            .addToCurrentPlusMinusHeap(edge, oppositeTree.currentDirection);
                    } else {
                        tree.addPlusInfinityEdge(edge);
                    }

                }
            } else if (opposite.isMinusNode()) {
                // this is an inner edge
                edge.slack += eps;
            }
        }
    }

    
    private void setBlossomSiblings(BlossomVNode blossomRoot, BlossomVEdge blossomFormingEdge)
    {
        // set blossom sibling nodes
        BlossomVEdge prevEdge = blossomFormingEdge;
        for (BlossomVEdge.BlossomNodesIterator iterator =
             blossomFormingEdge.blossomNodesIterator(blossomRoot); iterator.hasNext();)
        {
            BlossomVNode current = iterator.next();
            if (iterator.getCurrentDirection() == 0) {
                current.blossomSibling = prevEdge;
                prevEdge = current.parentEdge;
            } else {
                current.blossomSibling = current.parentEdge;
            }
        }
    }

    
    BlossomVNode findBlossomRoot(BlossomVEdge blossomFormingEdge)
    {
        BlossomVNode root, upperBound; // need to be scoped outside of the loop
        BlossomVNode[] endPoints = new BlossomVNode[2];
        endPoints[0] = blossomFormingEdge.head[0];
        endPoints[1] = blossomFormingEdge.head[1];
        int branch = 0;
        while (true) {
            if (endPoints[branch].isMarked) {
                root = endPoints[branch];
                upperBound = endPoints[1 - branch];
                break;
            }
            endPoints[branch].isMarked = true;
            if (endPoints[branch].isTreeRoot) {
                upperBound = endPoints[branch];
                BlossomVNode jumpNode = endPoints[1 - branch];
                while (!jumpNode.isMarked) {
                    jumpNode = jumpNode.getTreeGrandparent();
                }
                root = jumpNode;
                break;
            }
            endPoints[branch] = endPoints[branch].getTreeGrandparent();
            branch = 1 - branch;
        }
        BlossomVNode jumpNode = root;
        while (jumpNode != upperBound) {
            jumpNode = jumpNode.getTreeGrandparent();
            jumpNode.isMarked = false;
        }
        clearIsMarkedAndSetIsOuter(root, blossomFormingEdge.head[0]);
        clearIsMarkedAndSetIsOuter(root, blossomFormingEdge.head[1]);

        return root;
    }

    
    private void clearIsMarkedAndSetIsOuter(BlossomVNode root, BlossomVNode start)
    {
        while (start != root) {
            start.isMarked = false;
            start.isOuter = false;
            start = start.getTreeParent();
            start.isOuter = false;
            start = start.getTreeParent();
        }
        root.isOuter = false;
        root.isMarked = false;
    }

    
    private void reverseBlossomSiblings(BlossomVNode blossomNode)
    {
        BlossomVEdge prevEdge = blossomNode.blossomSibling;
        BlossomVNode current = blossomNode;
        do {
            current = prevEdge.getOpposite(current);
            BlossomVEdge tmpEdge = prevEdge;
            prevEdge = current.blossomSibling;
            current.blossomSibling = tmpEdge;
        } while (current != blossomNode);
    }

    
    private boolean forwardDirection(BlossomVNode blossomRoot, BlossomVNode branchesEndpoint)
    {
        int hops = 0;
        BlossomVNode current = blossomRoot;
        while (current != branchesEndpoint) {
            ++hops;
            current = current.blossomSibling.getOpposite(current);
        }
        return (hops & 1) == 0;
    }

    
    public void printBlossomNodes(BlossomVNode blossomNode)
    {
        System.out.println("Printing blossom nodes");
        BlossomVNode current = blossomNode;
        do {
            System.out.println(current);
            current = current.blossomSibling.getOpposite(current);
        } while (current != blossomNode);
    }
}
