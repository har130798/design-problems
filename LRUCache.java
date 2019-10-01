package com.interviewcoder.coreservice;

/**
 * created on: 2019-09-30
 */
import java.util.HashMap;
import java.lang.*;

class dll {
    public int key;
    public int val;
    public dll next;
    public dll prev;

    public dll (int key, int val) {
        this.key = key;
        this.next = null;
        this.prev = null;
        this.val = val;
    }
}

class LRU {
    public int capacity;
    public HashMap<Integer, dll> cache;
    public int size;
    public dll head;
    public dll tail;

    public LRU(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public int get(int key) {
        if (!this.cache.containsKey(key)) {
            return -1;
        }
        dll node = this.cache.get(key);

        if (this.size == 1 || this.head.key == key) {
            return node.val;
        }

        if (this.size > 1 && this.tail.key == key) {
            dll prev = node.prev;
            prev.next = null;
            this.tail = prev;
        } else {
            dll prev = node.prev;
            dll next = node.next;
            prev.next = next;
            next.prev = prev;
        }

        this.head.prev = node;
        node.next = this.head;
        node.prev = null;
        this.head = node;
        return node.val;
    }

    void put(int key, int val) {
        if (this.size == 0) {
            dll newnode = new dll(key, val);
            this.cache.put(key, newnode);
            this.size += 1;
            this.head = newnode;
            this.tail = newnode;
            return;
        }

        if (this.cache.containsKey(key)) {
            dll node = this.cache.get(key);
            node.val = val;
            this.get(key);
            return;
        }

        if (this.size == this.capacity) {
            this.invalidate();
        }

        dll newnode = new dll(key, val);
        this.cache.put(key, newnode);
        this.size += 1;

        if (this.capacity == 1) {
            this.head = this.tail = newnode;
        } else {
            newnode.next = this.head;
            this.head.prev = newnode;
            this.head = newnode;
        }
    }

    private void invalidate() {
        this.cache.remove(this.tail.key);
        this.size -= 1;

        if (this.capacity == 1) {
            this.head = this.tail = null;
        } else {
            dll prev = this.tail.prev;
            prev.next = null;
            this.tail = prev;
        }
    }

    public void printCache() {
        System.out.print("Cache content : ");
        dll node = this.head;
        while(node != null) {
            System.out.print(node.val);
            System.out.print(' ');
            node = node.next;
        }
        System.out.println();
    }
}
public class LRUCache {
    public static void main(String [] args) {
        LRU cache = new LRU(2);
        cache.put(1, 1);
        cache.printCache();
        cache.put(2, 2);
        cache.printCache();
        System.out.println(cache.get(1));       // returns 1
        cache.printCache();
        cache.put(3, 3);    // evicts key 2
        cache.printCache();
        System.out.println(cache.get(2));       // returns -1 (not found)
        cache.printCache();
        cache.put(4, 4);    // evicts key 1
        cache.printCache();
        System.out.println(cache.get(1));       // returns -1 (not found)
        cache.printCache();
        System.out.println(cache.get(3));       // returns 3
        cache.printCache();
        System.out.println(cache.get(4));       // returns 4
        cache.printCache();
    }
}
