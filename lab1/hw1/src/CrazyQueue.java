/*
Crazy Queue implementation. Implement a Queue utility (interface MyQueue.java)
taking into account following restriction: you can only use two stacks for the internal
implementation of the queue! no other data structures are allowed!
Demonstrate the working of your queue implementation with a simple client that uses
3 queues with different types of elements.
 */

interface MyQueue<T> {
    void enqueue(T item);    // add a new object of type T to queue
    T dequeue();   // removes and returns oldest object in queue
    boolean isEmpty();  // test if queue is empty
}

interface MyStack<T> {
    void push(T item);    // insert a new object of type T onto stack
    T pop();   // removes and returns object on top of stack
    boolean isEmpty();  // test if stack is empty
}

class ArrayStack<T> implements MyStack<T> {
    private final T[] a;  // holds the items
    private int n;       // number of items in stack

    // create an empty stack with given capacity
    public ArrayStack(int capacity) {
        //a = new T[capacity]; - NOT possible with generics!!!
        a = (T[])new Object[capacity];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    private boolean isFull() {
        return n == a.length;
    }

    public void push(T item) {
        if (isFull()) throw new RuntimeException("Stack overflow");
        a[n++] = item;
    }

    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack underflow");
        T item = a[--n];
        a[n] = null;
        return item;
    }
}

public class CrazyQueue<T> implements MyQueue<T> {
    protected ArrayStack<T> stack1;
    protected ArrayStack<T> stack2;
    public CrazyQueue() {
        stack1 = new ArrayStack<>(5);
        stack2 = new ArrayStack<>(5);
    }
    public void enqueue(T item) {
        stack1.push(item);
    }
    public T dequeue() {
        if (stack2.isEmpty()){
            while (!stack1.isEmpty()) stack2.push(stack1.pop());
        }

        return stack2.pop();
    }

    public boolean isEmpty() {
        return stack1.isEmpty() && stack2.isEmpty();
    }

    public static void main(String[] args) {
        MyQueue<Integer> intQueue = new CrazyQueue<>();
        intQueue.enqueue(1);
        intQueue.enqueue(2);
        intQueue.enqueue(3);
        System.out.println("integerQueue:");
        while (!intQueue.isEmpty()) {
            System.out.println(intQueue.dequeue());
        }

        MyQueue<String> stringQueue = new CrazyQueue<>();
        stringQueue.enqueue("first");
        stringQueue.enqueue("second");
        stringQueue.enqueue("third");
        System.out.println("\nstringQueue:");
        while (!stringQueue.isEmpty()) {
            System.out.println(stringQueue.dequeue());
        }

        MyQueue<Double> doubleQueue = new CrazyQueue<>();
        doubleQueue.enqueue(1.1);
        doubleQueue.enqueue(2.2);
        doubleQueue.enqueue(3.3);
        System.out.println("\ndoubleQueue:");
        while (!doubleQueue.isEmpty()) {
            System.out.println(doubleQueue.dequeue());
        }
    }
}