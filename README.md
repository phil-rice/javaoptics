# Optics for Java

* Lens
* Optional
* Iso
* Traversal
* Fold
* Prism

# Annotation Processor that automatically generates the optics for simple records

This is a POC to see what the limitations are.

It only works with Java records at the moment. A companion object is created which contains the optics. It contains
lens for each field, and a traversal for fields that are list/collection or stream.

There is an experimental feature to generate chained traversals, but unfortunately this is problematic with
incremental compilation. A new approach would be needed to make this work.

