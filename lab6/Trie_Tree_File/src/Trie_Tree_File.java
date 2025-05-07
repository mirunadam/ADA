import java.io.*;
import java.util.*;
/* Implement following operations on SimpleTrieTree:
    void printAllWithPrefix(String prefix); prints all keys that start with the given prefix
    Iterable<String> getAllKeys(); returns an Iterable (list) of all keys, in ascending order
    void initFromFile(String filename); initializes the Trie Tree by adding all words contained in given file.
    The format of the file is that words are given each word on a line.
 */

interface TrieTree_methods{
    void printAllWithPrefix(String prefix);
    Iterable<String> getAllKeys();
}

class TrieTree implements TrieTree_methods {
    private final int R = 256; // R - size of alphabet.
    // in this example keys(words) are sequences of characters from extended ASCII

    private TrieNode root;      // root of trie tree

    // R-way trie node
    private class TrieNode {
        private Integer val;  // if current node contains end of a key(end of a word)
        // then it has a non-null val associated
        private final TrieNode[] next = new TrieNode[R]; // may have R children
    }

    /**
     * Initializes an empty trie tree
     */
    public TrieTree() {
        root = null;
    }


    /**
     * Inserts a word into the trie tree - the recursive implementation
     *
     * @param key the word to be inserted
     * @param val the value associated with the word
     */
    public void put(String key, Integer val) {
        if ((key == null) || (val == null)) throw new IllegalArgumentException("key or val argument is null");
        else root = put(root, key, val);
    }

    private TrieNode put(TrieNode x, String key, Integer val) {
        if (x == null) x = new TrieNode();
        if (key.equals("")) {
            x.val = val;
            return x;
        }
        char c = key.charAt(0);
        String restkey = "";
        if (key.length() > 1) restkey = key.substring(1);
        x.next[c] = put(x.next[c], restkey, val);
        return x;
    }

    /**
     * Inserts a word into the trie tree - the iterative version
     *
     * @param key the word to be inserted
     * @param val the value associated with the word
     */
    public void put_v2(String key, Integer val) {
        if (root == null) {
            root = new TrieNode();
        }
        TrieNode node = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (node.next[c] == null) {
                node.next[c] = new TrieNode();
            }
            node = node.next[c];
        }
        node.val = val;
    }

    public boolean contains(String key) {
        TrieNode node = getNode(root, key);
        return node != null && node.val != null;
    }

    private TrieNode getNode(TrieNode x, String prefix) {
        TrieNode node = x;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (node.next[c] == null) { //doesn't have a child for this character
                return null;
            }
            node = node.next[c]; //next child that corresponds to the current character
        }
        return node;
    }
    private void printKeysWithPrefix(TrieNode node, String prefix) {
        if (node.val != null) {
            System.out.println(prefix);
        }
        for (char c = 0; c < R; c++) {
            if (node.next[c] != null) {
                printKeysWithPrefix(node.next[c], prefix + (char)c);
            }
        }
    }
    public void printAllWithPrefix(String prefix) {
        TrieNode node = getNode(root, prefix);
        if (node == null) {
            System.out.println("No keys found with prefix " + prefix);
            return;
        }
        printKeysWithPrefix(node, prefix);
    }

    public Iterable<String> getAllKeys(){
        List<String> keys = new ArrayList<>();
        iterateKeys(root, "", keys);
        return keys;
    }

    private void iterateKeys(TrieNode node, String prefix, List<String> keys) {
        if (node == null) return;

        if (node.val != null) {
            keys.add(prefix);
        }

        for (char c = 0; c < R; c++) {
            if (node.next[c] != null) {
                iterateKeys(node.next[c], prefix + (char)c, keys);
            }
        }
    }

    public void printAllKeys(){
        Iterable<String> allKeys = getAllKeys();
        for (String key : allKeys) {
            System.out.println(key);
        }
    }

    public void initFromFile(String filename){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            int line_increment=0;
            while(reader.readLine() != null){
                String word=reader.readLine();
                this.put(word,line_increment);
                line_increment++;
            }
            reader.close();


        }catch(IOException e){
            e.getStackTrace();
        }
    }
    public void initFromFile_scan(String filename){
        try{
            Scanner scanner=new Scanner(new File(filename));

            int line_increment=0;
            while(scanner.hasNextLine()){
                String word=scanner.nextLine();
                this.put(word,line_increment);
                line_increment++;
            }
            scanner.close();

        }catch(FileNotFoundException e){
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        TrieTree st = new TrieTree();
        st.initFromFile("src/random_words.txt");
        System.out.println();

        List<String> allKeys = new ArrayList<>();
        for (String key : st.getAllKeys()) {
            allKeys.add(key);
        }

        System.out.println("first word in ascending (alphabetical) order\n"+ allKeys.get(0));
        System.out.println();
        System.out.println("last word in ascending alphabetical order\n" + allKeys.get(allKeys.size()-1));
        System.out.println();
        String longestWord = "";
        for (String key : allKeys) {
            if (key.length() > longestWord.length()) {
                longestWord = key;
            }
        }
        System.out.println("longest word\n"+longestWord);


        String testprefix = "ban";
        System.out.println("\nkeysWithPrefix " + testprefix);
        st.printAllWithPrefix(testprefix);

    }
}
