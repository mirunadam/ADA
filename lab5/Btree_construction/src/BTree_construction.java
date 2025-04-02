/*
Construction exercise.
Without using the insertion operation, construct (by direct hardcoding)
a BTree of mindegree t=3, having the exact structure given in the figure.
You define the datastructures needed for representing BTrees and then instantiate nodes
and hardcode the links.
(You can see an example of constructing a tree by direct hardcoding for BSTs here)
~ this can be implemented on int
 */

import java.util.LinkedList;
import java.util.Queue;

interface BTree_HardCoded {
        public void buildConfig();
        public void displayLevels();
}

class IntegerBTree implements BTree_HardCoded {

    private int T; // the mindegree of the B-Tree
    private BTreeNode root;

    class BTreeNode {
        int n;    // current number of keys contained in node
        Integer key[] = new Integer[2 * T - 1];   //maximum 2T-1 keys
        BTreeNode child[] = new BTreeNode[2 * T]; // maximum 2T children
        boolean leaf = true;

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append(" [ ");
            for (int i=0; i<n; i++)
                sb.append(" "+key[i]);
            sb.append(" ] ");
            return sb.toString();
        }
    }

    public IntegerBTree(int t) {
        T = t;
        root = new BTreeNode();
        root.n = 0;
        root.leaf = true;
    }

    public void buildConfig(){
        root = new BTreeNode();
        root.n = 5;
        root.leaf = false;
        root.key[0] = 5;
        root.key[1] = 12;
        root.key[2] = 16;
        root.key[3] = 23;
        root.key[4] = 30;

        BTreeNode child0 = new BTreeNode();
        child0.n = 3;
        child0.leaf = true;
        child0.key[0] = 1;
        child0.key[1] = 2;
        child0.key[2] = 3;

        BTreeNode child1 = new BTreeNode();
        child1.n = 5;
        child1.leaf = true;
        child1.key[0] = 6;
        child1.key[1] = 7;
        child1.key[2] = 9;
        child1.key[3] = 10;
        child1.key[4] = 11;

        BTreeNode child2 = new BTreeNode();
        child2.n = 2;
        child2.leaf = true;
        child2.key[0] = 13;
        child2.key[1] = 14;

        BTreeNode child3 = new BTreeNode();
        child3.n = 3;
        child3.leaf = true;
        child3.key[0] = 18;
        child3.key[1] = 19;
        child3.key[2] = 20;

        BTreeNode child4 = new BTreeNode();
        child4.n = 2;
        child4.leaf = true;
        child4.key[0] = 24;
        child4.key[1] = 26;

        BTreeNode child5 = new BTreeNode();
        child5.n = 2;
        child5.leaf = true;
        child5.key[0] = 32;
        child5.key[1] = 36;

        root.child[0] = child0;
        root.child[1] = child1;
        root.child[2] = child2;
        root.child[3] = child3;
        root.child[4] = child4;
        root.child[5] = child5;
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
                    BTreeNode y = (x.child)[i];
                    q.add(new QueuePair(y, level + 1));
                }
            }
        }
        System.out.println();
    }


    public static void main(String[] args) {

        IntegerBTree b = new IntegerBTree(3);
        b.buildConfig();
        b.displayLevels();

    }
}