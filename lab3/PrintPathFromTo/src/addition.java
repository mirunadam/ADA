 /* private Node findLCA(Node root, K key1, K key2) {
        if (root == null) return null;

        int cmp1 = key1.compareTo(root.key);
        int cmp2 = key2.compareTo(root.key);

        // If both keys are smaller, LCA is in the left subtree
        if (cmp1 < 0 && cmp2 < 0) return findLCA(root.left, key1, key2);
        // If both keys are greater, LCA is in the right subtree
        if (cmp1 > 0 && cmp2 > 0) return findLCA(root.right, key1, key2);
        // Otherwise, root is the LCA
        return root;
    }

    // Print path from LCA to a node
    private void printPathToNode(Node root, K key) {
        if (root == null) return;

        System.out.print(root.val + " ");
        if (key.compareTo(root.key) < 0) {
            printPathToNode(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            printPathToNode(root.right, key);
        }
    }

    // Print path from node1 to node2
    public void PrintPathFromTo(K node1, K node2) {
        Node lca = findLCA(root, node1, node2); // Find the LCA of node1 and node2
        printPathToNode(lca, node1); // Print path from LCA to node1
        System.out.print(" ");
        printPathToNode(lca, node2); // Print path from LCA to node2
        System.out.println();
    }

    // Search for the closest node to key k
    public K SearchClosest(K k) {
        return SearchClosest(root, k, root.key);
    }

    private K SearchClosest(Node root, K k, K closest) {
        if (root == null) return closest;

        int cmp = k.compareTo(root.key);

        // Update closest if the current node is closer
        if (Math.abs(k.compareTo(closest)) > Math.abs(k.compareTo(root.key))) {
            closest = root.key;
        }

        // If k is smaller, move to the left subtree
        if (cmp < 0) {
            return SearchClosest(root.left, k, closest);
        }
        // If k is greater, move to the right subtree
        else if (cmp > 0) {
            return SearchClosest(root.right, k, closest);
        }

        return root.key;  // k is found, return it
    }*/