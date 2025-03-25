import java.util.NoSuchElementException;


/**
 * A simple BST implementation
 * Keys and values are ints
 */
 class IntBST {

    private class Node {   // BST Node = nested inner class
        int key;           // sorted by Key
        int val;         // associated data
        Node left, right;  // left and right subtrees

        Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;             // root of BST

    public IntBST() {
        root = null;        // initializes empty BST
    }

    /**
     * Print keys in ascending order by
     * doing an inorder tree walk
     */
    public void inorder() {
        inorder(root);
    }
    private void inorder(Node x) {
        if (x == null) return;
        inorder(x.left);
        System.out.print("("+x.key+","+x.val+")");
        inorder(x.right);
    }

    /**
     * Searches for a given key in the BST
     * returns true if the key is contained in the BST and false otherwise
     */
    public boolean contains(int key) {
        return contains(root, key);
    }
    private boolean contains(Node x, int key) {
        if (x == null) return false;
        if (key < x.key) return contains(x.left, key);
        else if (key > x.key) return contains(x.right, key);
        else return true;
    }



    /**
     * Inserts the specified key-value pair, overwriting the old
     * value in the node with the new value if the BST already contains the key.
     */
    public void put(int key, int val) {
        root=put(root, key, val);
    }

    private Node put(Node x, int key, int val) {
        if (x == null) return new Node(key, val);
        if (key < x.key) x.left = put(x.left, key, val);
        else if (key > x.key) x.right = put(x.right, key, val);
        else x.val = val;
        return x;
    }

    /**
     * Returns the smallest key in the BST
     */
    public int min() {
        if (root == null) throw new NoSuchElementException("calls min() with empty BST");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    /**
     * Returns the largest key in the BST
     */
    public int max() {
        if (root == null) throw new NoSuchElementException("calls max() with empty BST");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    /**
     * Deletes the node containing the specified key
     */
    public void delete(int key) {
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node z, int key) {
        if (z == null) return null;
        if (key < z.key) z.left = deleteRecursive(z.left, key);
        else if (key > z.key) z.right = deleteRecursive(z.right, key);
        else {
            // node z contains the key to be deleted
            if (z.right == null) return z.left;  // case 1: only 1 child left
            if (z.left == null) return z.right;  // case 1: only 1 child right
            //case 2: node z to be deleted has 2 children
            Node y = min(z.right); // find minimum in its right subtree (successor of z)
            z.right = deleteRecursive(z.right, y.key); //delete minimum node - we KNOW it has max 1 child
            z.key = y.key; //replace current key with minimum  key
        }
        return z;
    }

    /**
     * Test IntBST
     */
    public static void main(String[] args) {

        int[] a = {20, 30, 15, 1, 7, 9, 29, 11, 12, 4};

        IntBST bst = new IntBST();

        for (int i = 0; i < a.length; i++) {
            bst.put(a[i], 3*a[i]); // put triple as key values
        }

        bst.inorder();

        System.out.println();

        bst.delete(15);

        bst.inorder();

        System.out.println();

    }
}