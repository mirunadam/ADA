import java.util.NoSuchElementException;
/*
Add following operations to the implementation of IntBST (implementation for any BST, not only for AVL)
rotateLeft(int key) - transforms the shape of the BST by performing a left rotation around the node containing key
rotateRight(int key)
 */
interface BSTmethods<K,V>{
    public void put(K key, V val);
    public void inorder();
    public void preorder();
    public boolean contains(K key);

    public void rotateRight(K key);
    public void rotateLeft(K key);

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
    public void preorder() {
        preorder(root);
    }

    public void preorder(Node x) {
        if (x == null) return;
        System.out.print("(" + x.key + "," + x.val + ")");
        preorder(x.left);
        preorder(x.right);
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

    public Node min() {
        if (root == null) throw new NoSuchElementException("calls min() with empty BST");
        return min(root);
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    public Node max() {
        if (root == null) throw new NoSuchElementException("calls max() with empty BST");
        return max(root);
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
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

    private boolean compare(Node currentNode, Node closest, K key) {
        if (currentNode.val.equals(closest.val)){
            return false;
        }
        return (key.compareTo(currentNode.key) - key.compareTo(closest.key)) == 0;
    }

    public void rotateRight(K key) {
        root = rotateRight(root, key);
    }

    private Node rotateRight(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = rotateRight(x.left, key);
        } else if (cmp > 0) {
            x.right = rotateRight(x.right, key);
        } else {
            if (x.left == null) {  //no child on left  == no rotation
                return x;
            }

            Node aux_Root = x.left;
            x.left = aux_Root.right;
            aux_Root.right = x;

            return aux_Root;
        }
        return x;
    }

    public void rotateLeft(K key) {
        root = rotateLeft(root, key);
    }

    private Node rotateLeft(Node x, K key) {
        if (x == null){
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = rotateLeft(x.left, key);
        } else if (cmp > 0) {
            x.right = rotateLeft(x.right, key);
        } else {
            if (x.right == null) {  //no child on right  == no rotation
                return x;
            }

            Node aux_Root = x.right;
            x.right = aux_Root.left;
            aux_Root.left = x;

            return aux_Root;
        }
        return x;
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
public class GenericBST_rotate {
    public static void main(String[] args) {
        GenericBST<Integer, Integer> bst = new GenericBST<>();
        GenericBST<Integer, Integer> bst1 = new GenericBST<>();
        GenericBST<Integer, String> bst2 = new GenericBST<>();

        bst.put(5, 10);
        bst.put(2, 4);
        bst.inorder();
        System.out.println();
        bst2.put(3, "pisici");
        bst2.put(1, "pisic");
        bst2.put(7, "pisicute");
        bst2.inorder();
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
       // bst1.preorder();

        GenericBST<Integer, Integer> bst_left = new GenericBST<>();

        bst_left.put(7, 7);
        bst_left.put(10, 10);
        bst_left.put(5, 5);
        bst_left.put(12, 12);
        bst_left.put(22, 22);

        System.out.println("Before Left Rotation:");
        bst_left.preorder();
        System.out.println();

        bst_left.rotateLeft(10);

        System.out.println("After Left Rotation:");
        bst_left.preorder();
        System.out.println();

        GenericBST<Integer, Integer> bst_right = new GenericBST<>();

        bst_right.put(5, 5);
        bst_right.put(4, 4);
        bst_right.put(3, 3);
        bst_right.put(1, 1);
        bst_right.put(8, 8);

        System.out.println("Before Right Rotation:");
        bst_right.preorder();
        System.out.println();

        bst_right.rotateRight(4);

        System.out.println("After Right Rotation:");
        bst_right.preorder();
        System.out.println();

        GenericBST<Integer, String> bst_cats = new GenericBST<>();
        bst_cats.put(3, "pisi3");
        bst_cats.put(1, "miti");
        bst_cats.put(5, "pisi mare");
        bst_cats.put(4, "pisica");
        bst_cats.put(8, "pisica grasa");
        bst_cats.put(10, "pisica cosmica");

        System.out.println("Before Left Rotation:");
        bst_cats.preorder();
        System.out.println();

        bst_cats.rotateLeft(3);

        System.out.println("After Left Rotation:");
        bst_cats.preorder();
        System.out.println();

        GenericBST<String, City> bst3 = new GenericBST<>();
        bst3.put("Arad", new City("Arad", 180, 210));
        bst3.put("Brasov", new City("Brasov", 200, 280));
        bst3.put("Timisoara", new City("Timisoara", 230, 300));
        System.out.println("Before Left Rotation:");
        bst3.preorder();
        System.out.println();
        bst3.rotateLeft("Arad");
        System.out.println("After Left Rotation:");
        bst3.preorder();

    }
}