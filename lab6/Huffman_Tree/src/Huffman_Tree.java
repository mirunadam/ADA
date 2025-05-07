import java.util.PriorityQueue;

/**
 * Starting point for a Hufman compression-decompression tool
 * in demo-mode (codes are Strings, not bits as they should be in real life)
 *
 * char getCharWithLongestCode(HufmanNode root); returns the character that corresponds
 * to the longest code in the given hufman tree
 *
 * CompressionResult encode(String input); encodes the given input string,
 * returns in CompressionResult the used Huffman tree and the encoded string
 * -  partially implemented in HufmanEncoderPrototype
 *
 implements HuffmanTreeMethods
interface HuffmanTreeMethods {
    char getCharWithLongestCode(HufmanEncoderPrototype.HufmanNode root); // Method to get the character with the longest Huffman code
    HufmanEncoderPrototype.CompressionResult encode(String input); // Method to encode a string using Huffman codes
} */


class HufmanEncoderPrototype{
    // alphabet size of extended ASCII
    private static int R = 256;

    /**
     * Nodes of Hufman tree
     */
    public static class HufmanNode implements Comparable<HufmanNode> {
        char ch;
        int freq;
        HufmanNode left, right;

        HufmanNode(char ch, int freq, HufmanNode left, HufmanNode right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        /**
         *
         * @return true if the node is a leaf node
         */
        boolean isLeaf() {
            return (left == null) && (right == null);
        }

        /**
         * Compares this HufmanNode with that HufmanNode given as argument
         * Comparison of nodes is done by comparing their freq attribute.
         * We need this operation defined in the Comparable interface because
         * we will put HufmanNodes in a PriorityQueue when building the Hufman tree.
         * @param that the object to be compared.
         * @return a negative integer, zero, or a positive integer
         * as this object is less than, equal to, or greater than the specified object.
         */
        public int compareTo(HufmanNode that) {
            return this.freq - that.freq;
        }
    }

    /**
     * A CompressionResult object simulates a compressed file.
     * It contains the bits resulting after encoding and the used Huffman codes.
     * In this demo version the encoded "Bits" are a string where chars are  '1' or '0'
     * The encode operation returns as result a CompressionResult.
     * The decode operation takes as input argument a CompressionResult.
     */
    public static class CompressionResult {
        HufmanNode huffmanCodes;
        String Bits;

        public CompressionResult(String bits, HufmanNode tree) {
            this.huffmanCodes = tree;
            this.Bits = bits;
        }
    }

    /**
     * Encodes a string using Hufman codes.
     * The CompressionResult contains the encoded message and
     * the Hufman tree used to generate the codes
     *
     * @param s - the string to be encoded
     * @return a CompressionResult
     */
    public static CompressionResult encode(String s) {

        char[] input = s.toCharArray();

        // compute frequencies of characters
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // for demo purpose only displays frequency counts
        for (int i = 0; i < R; i++)
            if (freq[i] > 0)
                System.out.println("freq " + (char) i + " " + freq[i]);

        // build Huffman tree
        HufmanNode root = buildHufmanTree(freq);

        // build code table
        String[] st = new String[R];
        printCodeTable(st, root, "");

        /* use Huffman code to encode input
        CompressionResult result = new CompressionResult("", root);
        // TO DO: Implement compression of input and compute the bits of the CompressionResult!
        System.out.println("Compression of input not yet implemented - TO DO!");
        return result; */
        StringBuilder encodedBits = new StringBuilder();
        for (char c : input) {
            encodedBits.append(st[c]);  //Huff code for each char
        }
        return new CompressionResult(encodedBits.toString(), root);
    }

    /**
     * Build the Hufman tree from given frequencies
     * @param freq - the array of character frequencies
     * @return the root node of the Hufman tree
     */
    private static HufmanNode buildHufmanTree(int[] freq) {
        // initialize priority queue with singleton trees
        PriorityQueue<HufmanNode> pq = new PriorityQueue<>();
        for (char c = 0; c < R; c++)
            if (freq[c] > 0)
                pq.add(new HufmanNode(c, freq[c], null, null));
        // repeat merge two smallest trees
        while (pq.size() > 1) {
            HufmanNode left = pq.remove();
            HufmanNode right = pq.remove();
            HufmanNode parent = new HufmanNode('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.remove();
    }

    /**
     * Build (and print) lookup table for symbols and their Hufman codes
     * @param st - the lookup  table. st[c] contains the code for character c as a string
     * @param x - the root node of the Hufman tree
     * @param s - a buffer collecting the  path encoding  from the root to the current node
     */
    private static void printCodeTable(String[] st, HufmanNode x, String s) {
        if (!x.isLeaf()) {
            printCodeTable(st, x.left, s + '0');
            printCodeTable(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
            // for demo purpose prints codes
            System.out.println("Code of " + x.ch + " is " + s);
        }
    }
    
    public static char getCharWithLongestCode(HufmanNode root) {
        if (root == null) {
            return '\0';    //empty tree
        }

        int[] maxLength = new int[R];   //array will hold the max code length found for each char
        traverseTree(root, 0, maxLength);

        char result = '\0';
        int longest = -1;

        for (char c = 0; c < R; c++) {
            if (maxLength[c] > longest) {   //find character with the maximum code length
                longest = maxLength[c];
                result = c;
            }
        }
        return result;
    }

    /**
     * Helper method to traverse the Huffman tree and record code lengths
     * @param node - current node in the traversal
     * @param depth - current depth in the tree (code length so far)
     * @param maxLength - array storing maximum code lengths for each character
     */
    private static void traverseTree(HufmanNode node, int depth, int[] maxLength) {
        if (node.isLeaf()) {
            if (depth > maxLength[node.ch]) { //update max code length
                maxLength[node.ch] = depth;
            }
        } else {
            if (node.left != null) {
                traverseTree(node.left, depth + 1, maxLength);
            }
            if (node.right != null) {
                traverseTree(node.right, depth + 1, maxLength);
            }
        }
    }
    /**
     * Decodes an encoded message
     *
     * @param encoded - contains an encoded string and the Huffman tree that was used for encoding
     * @return - the decoded string
     */
    public static String decode(CompressionResult encoded) {
        String output = "";
        HufmanNode root = encoded.huffmanCodes;
        HufmanNode current = root;

        for (int i = 0; i < encoded.Bits.length(); i++) {
            char bit = encoded.Bits.charAt(i);

            if (bit == '0') {
                current = current.left;
            } else if (bit == '1') {
                current = current.right;
            }

            if (current.isLeaf()) { //leaf -> append the character and reset to root
                output = output + (current.ch);
                current = root;
            }
        }

        return output.toString();
    }

    public static void main(String[] args) {
        String original = "ABRACABABRA";
        System.out.println("Original message to be encoded: " + original);

        CompressionResult result = encode(original);

        System.out.println("Encoded message has " + result.Bits.length() + " bits");
        System.out.println("Encoded message is: " + result.Bits);

        System.out.println();

        String recovered = decode(result);
        System.out.println("Decoded message is: " + recovered);

        char charWithLongestCode = getCharWithLongestCode(result.huffmanCodes);
        System.out.println("Character with the longest Huffman code: " + charWithLongestCode);

    }

}