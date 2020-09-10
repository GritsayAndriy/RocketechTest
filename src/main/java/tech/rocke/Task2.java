package tech.rocke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Task2 {
    public static void main(String[] args) {
        String [] name = {"Andriy","Sacha"};
        Integer [] index = {1,2};

        Collection<Object> collection = new ArrayList<>();

        Stack<Object> stack = new Stack<>();
        stack.put(name, collection);
        stack.put(index, collection);
        stack.print();

    }
}

class Stack<T>{

    private Collection<T> collection;

    public void put(T[] array, Collection<T> collection) {
        this.collection = collection;
        collection.addAll(Arrays.asList(array));
    }

    public Collection<T> get(){
        return this.collection;
    }

    public void print(){
        this.collection.forEach(System.out::println);
    }
}