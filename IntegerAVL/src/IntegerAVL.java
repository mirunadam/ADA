import java.util.LinkedList;
import java.util.Queue;





public class IntegerAVL {

    private class AVLNode {
        Integer key;      // sorted by key
        Integer val;      // associated data
        AVLNode left;
        AVLNode right;    // left and right subtrees
        int height;       // height of subtree rooted in this node

        public AVLNode(Integer key, Integer val, int height) {
            this.key = key;
            this.val = val;
            this.height = height;
            this.left = null;
            this.right = null;
        }
    }
    private AVLNode root;



    public IntegerAVL(){
        root = null;
    }

    public int height() {
        return height(root); // AVL nodes have to record their height, so no need to compute
    }

    private int height(AVLNode x) {
        if (x == null) return -1;
        return x.height;
    }


    public void put(Integer key, Integer val) {
        if (key == null) throw new IllegalArgumentException("key is null");
        root = put(root, key, val);
    }

    private AVLNode put(AVLNode x, Integer key, Integer val) {
        if (x == null) return new AVLNode(key, val, 0);
        if (key < x.key) {
            x.left = put(x.left, key, val);
        } else if (key > x.key) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
            return x;
        }
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }


    /**
     * Restores the AVL balance property of the subtree.
     *
     * @param x the root of the subtree
     * @return the subtree with restored AVL balance property
     */
    private AVLNode balance(AVLNode x) {
        if (balanceFactor(x) < -1) {
            if (balanceFactor(x.right) > 0) {
                x.right = rotateRight(x.right);
            }
            x = rotateLeft(x);
        } else if (balanceFactor(x) > 1) {
            if (balanceFactor(x.left) < 0) {
                x.left = rotateLeft(x.left);
            }
            x = rotateRight(x);
        }
        return x;
    }

    /**
     * The balance factor is defined  as the difference between
     * height of the left subtree and height of the right subtree
     * A subtree with a balance factor of -1, 0 or 1 is AVL
     *
     * @param x the root of a subtree
     * @return the balance factor of the subtree
     */
    private int balanceFactor(AVLNode x) {
        return height(x.left) - height(x.right);
    }

    /**
     * Rotates the given subtree to the right.
     *
     * @param y the root of the subtree
     * @return the right rotated subtree
     */
    private AVLNode rotateRight(AVLNode y) {
        System.out.println("rotate right at " + y.key);
        AVLNode x = y.left;
        y.left = x.right;
        x.right = y;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    /**
     * Rotates the given subtree to the left.
     *
     * @param x the root of the subtree
     * @return the left rotated subtree
     */
    private AVLNode rotateLeft(AVLNode x) {
        System.out.println("rotate left at " + x.key);
        AVLNode y = x.right;
        x.right = y.left;
        y.left = x;
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }


    private class QueuePair {
        AVLNode node;
        int level;
        QueuePair(AVLNode node, int level){
            this.node=node;
            this.level=level;
        }
    }

    public void displayLevels() {
        // Use Queue to hold nodes while printing on levels
        Queue<QueuePair> q = new LinkedList<QueuePair>();

        System.out.print("AVL Tree displayed on levels: ");
        AVLNode x = root;
        int oldLevel = -1;
        int level = 0;

        if (root != null) q.add(new QueuePair(x, level));

        while (!q.isEmpty()) {

            QueuePair p = q.remove();
            x= p.node;
            level = p.level;

            if (level > oldLevel) {
                System.out.println();
                System.out.print("Level " + level + ":  "); // level changed
                oldLevel = level;
            }

            System.out.print(x.key + " ");
            if (x.left != null) q.add(new QueuePair(x.left, level+1));
            if (x.right != null) q.add(new QueuePair(x.right, level+1));
        }
        System.out.println();
        System.out.println();
    }


    public static void main(String[] args) {
        IntegerAVL st = new IntegerAVL();

        int[] a={8,9,10,11,15, 3, 17, 22, 25,16};

        for (int i=0; i<a.length; i++) {
            System.out.println("Insert " + a[i]);
            st.put(a[i], i);
            st.displayLevels();
        }
    }
}

