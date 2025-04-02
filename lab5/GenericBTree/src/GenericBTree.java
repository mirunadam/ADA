import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
/**
 *
 * Implemented operations: insert, displayLevels
 *
 * Implement for a GenericBTree:
 *   public boolean contains(int key);
 *   public int level(int key);
 *   public int successor(int key);
 *   public int predecessor(int key);
 */
interface BTreemethods<K,V>{

    public boolean contains(K key);
    public int level(K key);
    public K successor(K key);
    public K predecessor(K key);
}

class GenericBTree<K extends Comparable<K>, V> implements BTreemethods<K, V> {

    private int T; // the mindegree of the B-Tree

    class BTreeNode{
        int n;    // current number of keys contained in node
        List<K> key = new ArrayList<>(2 * T - 1);//maximum 2T-1 keys
        List<BTreeNode> child = new ArrayList<>(2 * T); // maximum 2T children
        //type erasure prevents direct generic array creation because it doesn't know at runtime what the actual type K and V are
        //is the load-time process
        boolean leaf = true;

        BTreeNode() {
            key = new ArrayList<>(2 * T - 1);
            for (int i = 0; i < 2 * T - 1; i++) {
                key.add(null);
            }

            child = new ArrayList<>(2 * T);
            for (int i = 0; i < 2 * T; i++) {
                child.add(null);
            }
        }

        public String toString(){
            StringBuilder sb =new StringBuilder();
            sb.append(" [ ");
            for (int i=0; i<n; i++)
                sb.append(" "+key.get(i));
            sb.append(" ] ");
            return sb.toString();
        }
    }

    /**
     * Constructor of an empty B-Tree of mindegree T
     * @param t - degree of B tree
     */
    public GenericBTree(int t) {
        T = t;
        root = new BTreeNode();
        root.n = 0;
        root.leaf = true;
    }

    private BTreeNode root; // root of tree

    public boolean contains(K key) {
        return contains(root, key);
    }

    private boolean contains(BTreeNode node, K key) {
        if (node == null) return false;

        int i = 0;
        while (i < node.n && key.compareTo(node.key.get(i)) > 0) {
            i++;
        }

        if (i < node.n && key.compareTo(node.key.get(i)) == 0) {
            return true;
        }

        if (node.leaf) {
            return false;
        }

        return contains(node.child.get(i), key);
    }

    public int level(K key){
        return level(root, key, 1);
    }

    private int level(BTreeNode node, K key, int currentLevel) {
        if (node == null){
            return -1;
        }

        int i = 0;
        while (i < node.n && key.compareTo(node.key.get(i)) > 0) {
            i++;
        }

        if (i < node.n && key.compareTo(node.key.get(i)) == 0) {
            return currentLevel;
        }

        if (node.leaf){
            return currentLevel;
        }
        return level(node.child.get(i), key, currentLevel + 1);
    }

    public K successor(K key){
        BTreeNode node = root;
        K successor = null;

        while (node != null) {
            int i = 0;
            while (i < node.n && key.compareTo(node.key.get(i)) >= 0) {
                i++;
            }

            if (i < node.n) {
                successor = node.key.get(i);
            }

            if (node.leaf) break;
            node = node.child.get(i);
        }
        return successor;
    }
    public K predecessor(K key){
        BTreeNode node = root;
        K predecessor = null;

        while (node != null) {
            int i = node.n - 1;
            while (i >= 0 && key.compareTo(node.key.get(i)) <= 0) {
                i--;
            }

            if (i >= 0) {
                predecessor = node.key.get(i);
            }

            if (node.leaf) break;
            node = node.child.get(i + 1);
        }
        return predecessor;
    }

    /**
     * Insert a key into a B-Tree in a single pass down the tree
     * see [CLRS] algorithm
     * @param key - new key to be inserted
     */
    public void insert(K key) {
        BTreeNode r = root;
        if (r.n == 2 * T - 1) { // if the root node is already full
            BTreeNode s = new BTreeNode(); // preventively splits the root
            root = s;
            s.leaf = false;
            s.n = 0;
            s.child.add(0, r);
            split(s, 0, r);
            insertNonfullStart(s, key);
        } else {
            insertNonfullStart(r, key);
        }
    }


    /**
     * Splits a node and introduces the new split as a child of the same parent
     * see [CLRS] algorithm
     * @param x - parent of node to split
     * @param pos - position in parent where to link new node
     * @param y - node to be split
     */
    private void split(BTreeNode x, int pos, BTreeNode y) {
        System.out.println("Split node "+y.toString());

        BTreeNode z = new BTreeNode();
        z.leaf = y.leaf; // new node z is leaf only if node y to be splitted was leaf
        z.n = T - 1;
        for (int j = 0; j < T - 1; j++) { //copy right half of y into new node
            z.key.add(j, y.key.get(j+T));
        }
        if (!y.leaf) {
            for (int j = 0; j < T; j++) {
                z.child.add(j, y.child.get(j+T));
            }
        }

        y.n = T - 1; //update the size of the original node y
        x.child.add(null);
        for (int j = x.n; j > pos; j--) { //right shift children in parent node
            x.child.set(j + 1, x.child.get(j));
        }
        x.child.set(pos + 1, z); //insert new node z as child of parent

        for (int j = x.n - 1; j >= pos; j--) {
            x.key.set(j + 1, x.key.get(j));
        }
        x.key.set(pos, y.key.get(T - 1));
        x.n = x.n + 1;
    }


    /**
     * Inserts key k into node x which is assumed to be non-full when function is called.
     * This function recurses as necessary down the tree, at all times guaranteeing that
     * the node to which it recurses is not full by calling split as necessary
     * see [CLRS] algorithm
     * @param x - root (non-full node) of subtree where insertion is done
     * @param k - new key to be inserted
     */
    final private void insertNonfullStart(BTreeNode x, K k) {

        if (x.leaf) {
            // x is a non-full leaf node, insert key into it
            int i = 0;
            // shift existing keys right to make place for new k
            x.key.add(null);
            for (i = x.n - 1; i >= 0 && k.compareTo(x.key.get(i))< 0; i--) {
                x.key.set(i + 1, x.key.get(i));
            }
            x.key.set(i + 1, k);
            x.n = x.n + 1;
        } else { // x is not a leaf
            int i = 0;
            while (i < x.n && k.compareTo(x.key.get(i)) > 0) {
                i++;
            }
            BTreeNode tmp = x.child.get(i);
            if (tmp.n == 2 * T - 1) {
                // if child is full, split it
                split(x, i, tmp);
                if (k.compareTo(x.key.get(i))>0) { // determines in which split half we insert
                    i++;
                }
            }
            insertNonfullStart(x.child.get(i), k); // recursive insert
        }

    }

    private class QueuePair {
        BTreeNode node;
        int level;
        QueuePair(BTreeNode node, int level){
            this.node=node;
            this.level=level;
        }
    }
    public void displayLevels() {
        // Use Queue to hold nodes while printing on levels
        Queue<QueuePair> q = new LinkedList<QueuePair>();

        System.out.println("B Tree displayed on levels: ");
        BTreeNode x = root;
        int oldLevel = 0;
        int level;

        q.add(new QueuePair(x, oldLevel));

        while (!q.isEmpty()) {

            QueuePair p = q.remove();
            x = p.node;
            level = p.level;

            if (level > oldLevel) {
                System.out.println(); // level changed
                oldLevel = level;
            }
            System.out.print(x.toString());
            if (!x.leaf) {
                for (int i = 0; i <= x.n; i++) {
                    BTreeNode y = (x.child.get(i));
                    q.add(new QueuePair(y, level + 1));
                }
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        GenericBTree b = new GenericBTree(3);

        int[] IntKeys={8,9,10,11,15,20, 17, 22, 25,16, 12, 13, 14, 26, 27, 29, 33, 40, 50, 51, 52, 53};

        for (int i=0; i<IntKeys.length; i++) {
            System.out.println("Will insert "+IntKeys[i]);
            b.insert(IntKeys[i]);
            b.displayLevels();
        }

        System.out.println();
        System.out.println("Contains 13? " + b.contains(13));
        System.out.println("Contains 23? " + b.contains(23));

        System.out.println("\nLevel of 13: " + b.level(13));
        System.out.println("\nSuccessor of 20: " + b.successor(20));
        System.out.println("Predecessor of 15: " + b.predecessor(15));


        GenericBTree<String, String> s = new GenericBTree<>(3);
        String[] stringKeys = {"apple", "banana", "cherry", "blueberry", "fig", "lemon", "mango", "orange", "pear"};

        for (String fruit : stringKeys) {
            System.out.println("\nWill insert: " + fruit);
            s.insert(fruit);
            s.displayLevels();
        }

        System.out.println("Contains apple? " + s.contains("apple"));
        System.out.println("Contains mar? " + s.contains("mar"));

        System.out.println();
        System.out.println("Level of cherry: " + s.level("cherry"));

        System.out.println("\nSuccessor of 'blueberry': " + s.successor("blueberry"));
        System.out.println("Predecessor of 'cherry': " + s.predecessor("cherry"));

        System.out.println();

        s.displayLevels();
    }
}
