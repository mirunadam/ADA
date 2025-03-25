/*
StackClient5:  Write a stack transformation algorithm that starts with a stack containing n integers and finishes
with the same integers in the same stack, but with the value that has the minimum value moved to the top,
and all other values in the same order.  For example: if the initial stack is [7, 3, 1, 9, 6, 8]  then after
transformation the stack is [7, 3,  9, 6, 8, 1]. Hint: you may use another helper stack in your transformation algorithm.
 */

import java.util.Stack;

public class StackClient5 {
    public static void moveMin(Stack<Integer> stack) {
        Stack<Integer> aux = new Stack<>();
        int min = stack.peek(); //retrieves but not takes out
        for (int i = 0; i < stack.size(); i++) { //iterate through the stack to find minimum
            if (min > stack.get(i)) {
                min = stack.get(i);
            }
        }

        while (!stack.isEmpty()) {
            boolean foundMin = false;
            int element = stack.pop();
            if (element == min) {
                foundMin=true;
            }
            else{
                aux.push(element);
            }
        }

        while (!aux.isEmpty()) {
            stack.push(aux.pop()); //place the rest of the elements in their order
        }

        stack.push(min); //place minimum on top

    }
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(7);
        stack.push(3);
        stack.push(1);
        stack.push(9);
        stack.push(6);
        stack.push(8);
        System.out.println("initial stack: " + stack);
        moveMin(stack);
        System.out.println("transformed stack: " + stack);

    }
}