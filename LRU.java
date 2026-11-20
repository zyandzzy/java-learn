import java.util.HashMap;
import java.util.Map;

public class LRU<K, V> {
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        Node<K, V> prev;

        Node() {}

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final Map<K, Node<K, V>> cache;

    public LRU(int capacity) {
        this.capacity = capacity;
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
        cache = new HashMap<>();
    }

    private void removeNode(Node<K, V> node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToHead(Node<K, V> node) {
        head.next.prev = node;
        node.next = head.next;
        node.prev = head;
        head.next = node;
    }

    private void moveNode(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    private Node<K, V> removeTail() {
        Node<K, V> node = tail.prev;
        removeNode(node);
        return node;
    }

    public V get(K key) {
        Node<K, V> node = cache.get(key);
        if (node == null) return null;
        moveNode(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node<K, V> node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveNode(node);
        } else {
            Node<K, V> newNode = new Node<>(key, value);
            cache.put(key, newNode);
            addToHead(newNode);

            if (cache.size() > capacity) {
               Node<K, V> no = removeTail();
               cache.remove(no.key);
            }
        }
    }

    private int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
        head.next = tail;
        tail.prev = head;
    }



}
