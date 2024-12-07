# conjoined-tree

## Conjoined Tree Data Structure

The Conjoined Tree data structure is a graph data structure that has multiple roots and branches but share the same
leaves among the root and branches. The Conjoined Tree is designed to represent shareable/intersect matrix data
between multi different axis, e.g. a pivot table. The main benefit of a Conjoined Tree data structure is it allows
structure modification such as adding, inserting, deleting, sorting, filtering, swapping on either rows or columns in 
the pivot table. The other benefit of Conjoined Tree is it allows items order change (re-ordering) without the need 
to reorder items along its axis. For example when you sort one of column values within a pivot table, the other values 
along the axis will automatically follow without any extra effort to re-arrange them.

Documentation Concept: https://edsuryadi.atlassian.net/wiki/external/NzM1Mjk1NDUwOWU4NGIwZDg5MGQxYWMzMTdhN2M4YWM

The current implementation in this git repo is an implementation of Conjoined Twin Tree data structure which is a 
Conjoined Tree that contains 2 roots. This data structure is used to represent a pivot table. However, I welcome anyone
to contribute any N-number of Conjoined Tree data structure implementation in different programming language.

## Patent Information
Patent Pending: US 18/676076, US 18/676,081, US 18/676,086
Inventor: Edward Suryadi
Current Assignee: Workday, Inc.