/*
 Define in an appropriate way the ArithmeticTree class. Implement operations:
 postorder() that will produce the postfix of the expression and
 value() that computes the value of the expression. Build the tree of the expression
 ((3+1)*3)/((9-5)+2)-((3*(7-4))+6) in a hardcoded way and test postorder() and value();
 CreateFromPostfix:  given a String that represents any valid Postfix arithmetic expression,
 create its equivalent binary tree representation.
 */
import java.util.*;
import java.util.Stack;

interface ArithmeticTreeMethods<K, V> {

}
class Node<K extends Comparable<K>, V>{
    V value;
    Node left, right;

    Node(V value) {
        this.value = value;
        this.left = this.right = null;
    }
}

class ArithmeticTree<K extends Comparable<K>, V> implements ArithmeticTreeMethods<K, V> {
    Node root;

    public V value() {
        return value(root);
    }
    public V value(Node node) {
        if (node == null) {
            return null;
        }
        if (node.left == null && node.right == null) {
            return (V)node.value;
        }

        V leftValue = value(node.left);
        V rightValue = value(node.right);
        if(instanceof(node.value)==String)
        switch (node.value) {
            case "+":
                return (double)(leftValue + rightValue);
            case "-":
                return (double)leftValue - rightValue;
            case "*":
                return (double)leftValue * rightValue;
            case "/":
                return (double)leftValue / rightValue;
            default:
                throw new IllegalArgumentException("mistery value: " + node.value);
        }
    }
    public String postorder() {
        return postorder(root);
    }
    private String postorder(Node node) {
        if (node == null) {
            return "";
        }

        String leftPostfix = postorder(node.left);
        String rightPostfix = postorder(node.right);
        return leftPostfix + rightPostfix + node.value + " ";
    }

    private boolean isOperator(String element) {
        return element.equals("+") || element.equals("-") || element.equals("*") || element.equals("/");
    }

   public ArithmeticTree CreateFromPostfix(String postfix) {
       Stack<Node> stack = new Stack<>();
       String[] elements = postfix.split(" ");

       for (String el : elements) {
           if (isOperator(el)) {
               Node right = stack.pop();
               Node left = stack.pop();

               Node operatorNode = new Node(el);
               operatorNode.left = left;
               operatorNode.right = right;

               stack.push(operatorNode);
           } else {
               stack.push(new Node(el));
           }
       }
       root = stack.pop(); //last element is root of the bin tree
       return this;
   }

}

public class BinTreeArithmetic {
    public static void main(String[] args) {
        ArithmeticTree tree = new ArithmeticTree();
        tree.root = new Node("-");
        // ((3+1)*3)
        tree.root.left = new Node("/");
        tree.root.left.left = new Node("*");
        tree.root.left.left.left = new Node("+");
        tree.root.left.left.left.left = new Node("3");
        tree.root.left.left.left.right = new Node("1");
        tree.root.left.left.right = new Node("3");
        // ((9-5)+2)
        tree.root.left.right = new Node("+");
        tree.root.left.right.left = new Node("-");
        tree.root.left.right.left.left = new Node("9");
        tree.root.left.right.left.right = new Node("5");
        tree.root.left.right.right = new Node("2");
        // ((3*(7-4))+6)
        tree.root.right = new Node("+");
        tree.root.right.left = new Node("*");
        tree.root.right.left.left = new Node("3");
        tree.root.right.left.right = new Node("-");
        tree.root.right.left.right.left = new Node("7");
        tree.root.right.left.right.right = new Node("4");
        tree.root.right.right = new Node("6");

        System.out.println("value of the expression: " + tree.value());
        System.out.println("postorder: " + tree.postorder());

        System.out.println("for the CreateFromPostfix: ");
        String expression = "3 1 + 3 * 9 5 - 2 + / 3 7 4 - * 6 + -";
        ArithmeticTree tree_exp = new ArithmeticTree();
        tree_exp.CreateFromPostfix(expression);
        System.out.println("value of the expression: " + tree_exp.value());
        System.out.println("postorder: " + tree_exp.postorder());

    }
}