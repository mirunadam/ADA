/*
PrintPathFromTo(node1, node2) - if node1 and node2 belong to the BST, prints the path from node1 to node2.
(Hint: find the lowest common ancestor of the nodes )
Example: For the BST in the example figure, PrintPathFromTo(10, 18) prints 10, 15, 20, 18.

SearchClosest(k) - returns the node containing the key with the value closest to k
(or the node containing k, if k is present in the tree). The function must do the search in O(h),
where h is the height of the tree.
Example: For the BST in the example figure above, SearchClosest(16) returns 15.
 */

interface BSTmethods<K,V>{
    public void put(K key, V val);
    public void inorder();
    public boolean contains(K key);

    public void PrintPathFromTo(K key1, K key2);
    public GenericBST.Node SearchClosest(K key);

}

class GenericBST<K extends Comparable<K>, V> implements BSTmethods<K,V>{


    public class Node {
        K key;           // sorted by Key
        V val;         // associated data
        Node left, right;  // left and right subtrees

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;             // root of BST

    public GenericBST() {
        root = null;        // initializes empty BST
    }


    /**
     * Print keys in ascending order by
     * doing an inorder tree walk
     */
    public void inorder() {
        inorder(root);
    }

    public void inorder(Node x) {
        if (x == null) return;
        inorder(x.left);
        System.out.print("(" + x.key + "," + x.val + ")");
        inorder(x.right);
    }


    /**
     * Searches for a given key in the BST
     * returns true if the key is contained in the BST and false otherwise
     */
    public boolean contains(K key) {
        return contains(root, key);
    }

    private boolean contains(Node x, K key) {
        if (x == null) return false;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return contains(x.left, key);
        else if (cmp > 0) return contains(x.right, key);
        else return true;
    }

    /**
     * Inserts the specified key-value pair, overwriting the old
     * value in the node with the new value if the BST already contains the key.
     */
    public void put(K key, V val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(root, key, val);
    }

    private Node put(Node x, K key, V val) {
        if (x == null) return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        return x;
    }
    /**
     * Deletes the node containing the specified key
     */
    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = deleteRecursive(root, key);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node deleteRecursive(Node z, K key) {
        if (z == null) return null;

        int cmp = key.compareTo(z.key);

        if (cmp < 0) z.left = deleteRecursive(z.left, key);
        else if (cmp > 0) z.right = deleteRecursive(z.right, key);
        else {
            // node z contains the key to be deleted
            if (z.right == null) return z.left;  // case 1: only 1 child left
            if (z.left == null) return z.right;  // case 1: only 1 child right

            // case 2: node z to be deleted has 2 children
            Node y = min(z.right);  // find minimum in its right subtree (successor of z)
            z.right = deleteRecursive(z.right, y.key);  // delete minimum node
            z.key = y.key;  // replace current key with minimum key
            z.val = y.val;  // replace current value with minimum value
        }
        return z;
    }

    public Node findLowComAncestor(Node root, K key1, K key2) {
        if (root == null) {
            return null;
        }

        if (key1.compareTo(root.key) < 0 && key2.compareTo(root.key) < 0) {
            return findLowComAncestor(root.left, key1, key2);
        }
        if (key1.compareTo(root.key) > 0 && key2.compareTo(root.key) > 0) {
            return findLowComAncestor(root.right, key1, key2);
        }
        return root;
    }
    public void PrintPathFromTo(K key1, K key2) {
        Node lowComAncestor = findLowComAncestor(root, key1, key2);

        printPath(lowComAncestor.left, lowComAncestor.key);
        printPath(lowComAncestor, key2);
    }

    private void printPath(Node lca, K key) {
        if (lca == null) return;

        if (key.compareTo(lca.key) < 0) {
            System.out.print(lca.key + " ");
            printPath(lca.left, key);
        } else if (key.compareTo(lca.key) > 0) {
            System.out.print(lca.key + " ");
            printPath(lca.right, key);
        } else {
            System.out.print(lca.key + " ");
        }
    }

    public Node SearchClosest(K key) {
        return SearchClosest(root, key, null);
    }

    private Node SearchClosest(Node x, K key, Node closest) {
        if (x == null) return closest;

        if (closest == null || compare(x, closest, key)) {
            closest = x;
        }

        int cmp = key.compareTo(x.key);

        if (cmp < 0) {
            return SearchClosest(x.left, key, closest);
        } else if (cmp > 0) {
            return SearchClosest(x.right, key, closest);
        } else {
            return x;
        }
    }

    private boolean compare(Node currentNode, Node closest, K key) {
        if (currentNode.val.equals(closest.val)){
            return false;
        }
        return (key.compareTo(currentNode.key) - key.compareTo(closest.key)) == 0;
    }

}

class City {
    private final String name;
    private final int size;
    private final int population;

    public City(String name, int size, int population) {
        this.name = name;
        this.size = size;
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" + "name='" + name + '\'' + ", size=" + size + ", population=" + population + '}';
    }
}
public class GenericBSTClient {
    public static void main(String[] args) {
        GenericBST<Integer, Integer> bst = new GenericBST<>();
        GenericBST<Integer, Integer> bst1 = new GenericBST<>();
        GenericBST<Integer, String> bst2 = new GenericBST<>();
        GenericBST<String, City> bst3 = new GenericBST<>();

        bst.put(5, 10);
        bst.put(2, 4);
        bst.inorder();
        System.out.println();
        bst2.put(3, "pisici");
        bst2.put(1, "pisic");
        bst2.put(7, "pisicute");
        bst2.inorder();
        System.out.println();
        bst3.put("Timisoara", new City("Timisoara", 230, 300));
        bst3.put("Arad", new City("Arad", 180, 210));
        bst3.put("Brasov", new City("Brasov", 200, 280));
        bst3.inorder();
        System.out.println();
        bst1.put(8, 8);
        bst1.put(2, 2);
        bst1.put(15, 15);
        bst1.put(1, 1);
        bst1.put(5, 5);
        bst1.put(10, 10);
        bst1.put(20, 20);
        bst1.put(4, 4);
        bst1.put(3, 3);
        bst1.put(7, 7);
        bst1.put(18, 18);
        bst1.put(22, 22);
        //bst1.inorder();

        System.out.print("Path from 10 to 18: ");
        bst1.PrintPathFromTo(10, 18); // 10, 15, 20, 18.

        System.out.println();
        System.out.println("Closest key to 16: " + (bst1.SearchClosest(16)).key);
        System.out.println("Closest key to Timi: " + (bst3.SearchClosest("Timi")).key);


    }
}