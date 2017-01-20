/*----------------------------------------------------------------
 *  Author:        Junkai Cai
 *  Written:       12/17/2016
 *  Last updated:  12/17/2016
 *
 *  Compilation:   javac Deque.java
 *  Execution:     java Deque
 *  
 *----------------------------------------------------------------*/
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int sizeOfDeque;
    private Node first, last;
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    /**
    *  Constructor  
    *  construct an empty deque
    *   
    */
    public Deque() {
        first = null;
        last = null;
        sizeOfDeque = 0;
    }
    
    /**
    *  isEmpty()  
    *  is the deque empty?
    *   
    */
    public boolean isEmpty() {
        if ((first == null) && (sizeOfDeque == 0)) // double check
            return true;
        else
            return false;
    }
    
    /**
    *  size()  
    *  return the number of items on the deque
    *   
    */
    public int size() {
        return sizeOfDeque;
    }
    
    /**
    *  addFirst(Item item) 
    *  add the item to the front
    *   
    */      
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("Cannot add null items");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.previous = null; 
        first.next = oldFirst;
        sizeOfDeque++;
        if (sizeOfDeque == 1)
            last = first;
        else
            oldFirst.previous = first;
    }
    
    /**
    *  addLast(Item item) 
    *  add the item to the end
    *   
    */
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException("Cannot add null items");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldLast; // including null if it was empty
        sizeOfDeque++;
        if (sizeOfDeque == 1)
            first = last;
        else
            oldLast.next = last;
    }
    
    /**
    *  removeFirst() 
    *  remove and return the item from the front
    *   
    */    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item item = first.item;
        first = first.next;
        sizeOfDeque--;
        if (sizeOfDeque == 0) { // if first is null
            first = null; // make sure first is null, not necessary
            last = null;
        }
        else
             first.previous = null; // error if only 1 item left (first = null)
        return item;
    }
    
    /**
    *  removeLast() 
    *  remove and return the item from the end
    *   
    */
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
        Item item = last.item;
        last = last.previous;        
        sizeOfDeque--;
        if (sizeOfDeque == 0) { // set first to null
            first = null;
            last = null; // make sure last is null, not necessary
        }
        else
            last.next = null; // error if only 1 item left (last = null)
        return item;
    }
    
    /**
    *  iterator() 
    *  return an iterator over items in order from front to end
    *   
    */
    public Iterator<Item> iterator() {
          return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        /** Returns true if there is an item next in the deque */
        @Override
        public boolean hasNext() {
            return (current != null);
        }
        
        /** returns the current item and increments the pointer */
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("No more items to iterate");
            }            
            Item item = current.item;
            current = current.next;            
            return item;
        }
        
        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException("remove function not supported");
        }
    }
/*
    public void printDeque() {
        Iterator<Item> i = this.iterator();
        while (i.hasNext()) {            
            Item s = i.next();
            System.out.println(s);
        }
    }
*/    
    public static void main(String[] args) {
        /*
        Deque<Double> testDeque = new Deque<Double>();
        
        testDeque.addFirst (256.3);
        testDeque.addLast (15.0);
        testDeque.addLast (-44.2);
        testDeque.addFirst (-235.3);
        testDeque.addFirst (346.4);
        testDeque.removeFirst();
        testDeque.removeFirst();
        testDeque.removeLast();
        //testDeque.removeLast();
       
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(0);
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.removeLast();
        if (deque.isEmpty())
            System.out.println("Empty");
        else
            System.out.println("Not Empty, " + deque.size() + " items in the Deque");
        System.out.println("--------------------------");
        deque.printDeque();
        */
    }
}