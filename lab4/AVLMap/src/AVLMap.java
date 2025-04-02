import java.util.*;
import java.util.Map;
/*
Make your own Sorted Dictionary ADT by implementing interface MySortedMap.java  (similar to java.util.SortedMap)
using AVL  trees in the internal implementation. Add the missing operations to the implementation skeleton given in MyAVLMap.java.
Test with client MyAVLMapClient.java that your implementation in MyAVLMap can be used in a similar way to java.util.TreeMap and
that it produces the same results.

containsKey(key) - returns boolean
firstKey - returns the smallest key or null if map is empty
lastKey - returns the biggest key or null if map is empty

 */

/**
 * MySortedMap is a Map that provides a total ordering on its keys.
 * All keys inserted into a sorted map must implement the Comparable interface.
 *
 * The operations defined in MySortedMap emulate a subset of
 * the operations of java.util.SortedMap
 *
 * @param <Key> - the type of keys maintained by this SortedMap
 * @param <Value> - the type of values mapped to keys
 */
interface MySortedMap <Key extends Comparable<Key>, Value>{

    /**
     * Searches if a key is contained in the Map.
     * @param key - the key to be searched
     * @return true if this map contains a mapping for the specified key
     */
    boolean containsKey(Key key);


    /**
     * Searches the associated value of a key.
     * @param key - the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    Value get(Key key);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a value for the key, the old value
     * is replaced by the specified value.
     * @param key - the new key
     * @param val - the new value
     */
    void put(Key key, Value val);

    /**
     * Removes the mapping containing given key from this map if it is present.
     * @param key - key whose mapping is to be removed
     */
    void remove(Key key);

    /**
     * @return all keys as an Iterable in ascending order
     */
    Iterable<Key> getKeys();

    /**
     * @return all entries (Key, Value) as an Iterable in ascending order of keys
     */
    Iterable<Map.Entry<Key,  Value>> getEntries();

    Key firstKey();
    Key lastKey();

}

class MyAVLMap<Key extends Comparable<Key>, Value> implements MySortedMap<Key, Value> {
    private Node root;             // root of BST

    private class Node {
        private Key key;      // sorted by key
        private Value val;         // associated data

        private int height;     //height of node
        private Node left, right;  // left and right subtrees

        public Node(Key key, Value val, int height) {
            this.key = key;
            this.val = val;
            this.height = height;
        }
    }

    /**
     * Initializes an empty Map.
     */
    public MyAVLMap() {
        root = null;
    }

    /**
     * Searches if a key is contained in the Map.
     *
     * @param key - the key to be searched
     * @return true if this map contains a mapping for the specified key
     */
    @Override
    public boolean containsKey(Key key) {
        return containsKey(root, key);
    }
    private boolean containsKey(Node x, Key key) {
        if (x == null) {
            return false;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return containsKey(x.left, key);
        } else if (cmp > 0) {
            return containsKey(x.right, key);
        } else {
            return true;
        }
    }

    /**
     * Searches the associated value of a key.
     *
     * @param key - the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    @Override
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    /**
     * @return all keys as an Iterable in ascending order
     */
    @Override
    public Iterable<Key> getKeys() {
        Queue<Key> q= new LinkedList<Key>();
        keys(root, q);
        return q;
    }

    private void keys(Node x, Queue<Key> q) {
        if (x==null) return;
        keys(x.left, q);
        q.add(x.key);
        keys(x.right,q);
    }

    /**
     * @return all entries as an Iterable in ascending order of keys
     */
    @Override
    public Iterable<Map.Entry<Key, Value>> getEntries() {
        Queue<Map.Entry<Key, Value>> q= new LinkedList<Map.Entry<Key, Value>>();
        getEntries(root, q);
        return q;
    }

    private void getEntries(Node x, Queue<Map.Entry<Key, Value>> q) {
        if (x == null) return;
        getEntries(x.left, q);
        q.add(new AbstractMap.SimpleEntry<>(x.key, x.val));
        getEntries(x.right, q);
    }


    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a value for the key, the old value
     * is replaced by the specified value.
     *
     * @param key - the new key
     * @param val - the new value
     */
    @Override
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (val == null) {
            remove(key);
            return;
        }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 0);
        if (key.compareTo(x.key)<0) {
            x.left = put(x.left, key, val);
        } else if (key.compareTo(x.key)>0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
            return x;
        }
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return x.height;
    }


    /**
     * Restores the AVL balance property of the subtree.
     *
     * @param x the root of the subtree
     * @return the subtree with restored AVL balance property
     */
    private Node balance(Node x) {
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
    private int balanceFactor(Node x) {
        return height(x.left) - height(x.right);
    }

    /**
     * Rotates the given subtree to the right.
     *
     * @param y the root of the subtree
     * @return the right rotated subtree
     */
    private Node rotateRight(Node y) {
        //System.out.println("rotate right at " + y.key);
        Node x = y.left;
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
    private Node rotateLeft(Node x) {
        //System.out.println("rotate left at " + x.key);
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }
    /**
     * Removes the mapping containing given key from this map if it is present.
     *
     * @param key - key whose mapping is to be removed
     */
    @Override
    public void remove(Key key) {
        root = remove(root, key);
    }
    private Node remove(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key);
        } else if (cmp > 0) {
            x.right = remove(x.right, key);
        } else {

            //no right child -> return left child
            if (x.right == null) {
                return x.left;
            }

            //no left child -> return right child
            if (x.left == null) {
                return x.right;
            }

            //has two children
            //find the smallest node in the right subtree == succesor
            Node right_subtree = x.right;
            while (right_subtree.left != null) {
                right_subtree = right_subtree.left;
            }

            x.key = right_subtree.key;
            x.val = right_subtree.val;

            //remove inorder successor from the right subtree to avoid duplicates
            x.right = remove(x.right, right_subtree.key);
        }

        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }

    public Key firstKey() {
        return firstKey(root);
    }
    private Key firstKey(Node x) {
        if (x == null) {
            return null;
        }
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }

    public Key lastKey() {
        return lastKey(root);
    }

    private Key lastKey(Node x) {
        if (x == null) {
            return null;
        }
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }
}

public class AVLMap {
    private static String getRandomAlphaString(int n)
    {
        String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random index in Alphabet
            int index = (int)(Alphabet.length() * Math.random());
            // add corresponding character to string
            sb.append(Alphabet.charAt(index));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        // two sorted maps implemented by MyAVLMap
        MySortedMap<Integer, Integer> map1= new MyAVLMap<Integer, Integer>();
        MySortedMap<Integer, String> map2 = new MyAVLMap<Integer, String>();

        // two sorted maps implemented by java.util.TreeMap
        SortedMap<Integer, Integer> map1j= new TreeMap<Integer, Integer>();
        SortedMap<Integer, String> map2j = new TreeMap<Integer, String>();

        System.out.println("Testing on an empty map:");
        System.out.println("firstKey: " + map1.firstKey());
        System.out.println("lastKey: " + map1.lastKey());

        Integer intVals[] = {23, 1, 5, 6, 10, 41, 34, 99 };

        //populate all maps with keys from intVals
        //put the same values in MyAVLMap and in java.util.TreeMap

        for (int i=0; i<intVals.length; i++) {
            int rand_int1 = intVals[i];
            map1.put(rand_int1, 2*rand_int1); // put in MySortedMap
            map1j.put(rand_int1, 2*rand_int1);  // put in Java's TreeMap
            String aValue = getRandomAlphaString(4);
            map2.put(rand_int1, aValue ); // put in MySortedMap
            map2j.put(rand_int1, aValue ); // put in Java's TreeMap
        }

        System.out.println("Keys of map1j Java's TreeMap in ascending order: ");
        for(Integer i: map1j.keySet()){
            System.out.print(" "+i);
        }
        System.out.println("\n");


        System.out.println("Keys of map1 MySortedMap in ascending order: ");
        for(Integer i:map1.getKeys()){
            System.out.print(" "+i);
        }
        System.out.println("\n");

        System.out.println("Entries of map2j Java's TreeMap in ascending order: ");
        for(Map.Entry<Integer, String> s:map2j.entrySet()){
            System.out.print(" "+s);
        }
        System.out.println("\n");

        System.out.println("Entries of map2 MySortedMap in ascending order: ");
        for(Map.Entry<Integer, String> s:map2.getEntries()){
            System.out.print(" "+s);
        }
        System.out.println("\n");
        System.out.println("Contains 5?: " + map2.containsKey(5));
        System.out.println("\n");

        int toDelete = intVals[2]; // one of the keys that was inserted will be deleted

        System.out.println("Delete key "+toDelete+ " from map2j Java's TreeMap ");
        map2j.remove(toDelete);
        System.out.println("Entries of map2j after deleting: ");
        for(Map.Entry<Integer, String> s:map2j.entrySet()){
            System.out.print(" "+s);
        }
        System.out.println("\n");

        System.out.println("Delete key "+toDelete+ " from map2 MyTreeMap ");
        map2.remove(toDelete);
        System.out.println("Entries of map2 after deleting: ");
        for(Map.Entry<Integer, String> s:map2.getEntries()){
            System.out.print(" "+s);
        }
        System.out.println("\n");
        System.out.println("Contains 5?: " + map2.containsKey(5));
        System.out.println("\n");

        System.out.println("Testing after inserting values for map1:");
        System.out.println("firstKey of map1: " + map1.firstKey());
        System.out.println("lastKey of map1: " + map1.lastKey());
        System.out.println();

        MySortedMap<String, String> map_fruit = new MyAVLMap<String, String>();
        map_fruit.put("apple", "fruit");
        map_fruit.put("carrot", "vegetable");
        map_fruit.put("elderberry", "fruit");
        map_fruit.put("date", "fruit");
        map_fruit.put("banana", "fruit");
        map_fruit.put("cherry", "fruit");

        System.out.println("Testing for map_fruit:");

        System.out.println("firstKey: " + map_fruit.firstKey());
        System.out.println("lastKey: " + map_fruit.lastKey());
        System.out.println();

        System.out.println("Contains 'cherry': " + map_fruit.containsKey("cherry"));
        System.out.println();
        System.out.println("Keys of map_fruit MySortedMap in ascending order: ");
        for(String i:map_fruit.getKeys()){
            System.out.print(" "+i);
        }
        System.out.println("\n");

    }
}
//time complexity e inaltimea