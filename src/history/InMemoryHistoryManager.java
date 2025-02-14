package history;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private Map<Integer, Node<T>> map = new HashMap<>();
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    @Override
    public void add(T task) {
        if (task == null) {
            return;
        }
        if (contain(task.getId())) {
            removeById(task.getId());
        }
        addLast(task, task.getId());
    }

    @Override
    public List<T> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeById(id);
    }

    public void addFirst(T e, int id) {
        final Node<T> oldHead = head;
        final Node<T> newNode = new Node<>(null, e, oldHead);
        head = newNode;
        if (oldHead == null) {
            tail = newNode;
        } else {
            oldHead.prev = newNode;
        }
        size++;
        map.put(id, newNode);
    }

    public void addLast(T e, int id) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, e, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        map.put(id, newNode);
    }

    public T getFirst() {
        final Node<T> head1 = head;
        if (head1 == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    public T getLast() {
        final Node<T> tail1 = tail;
        if (tail1 == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public T get(int id) {
        if (map.get(id) != null) {
            return map.get(id).data;
        } else {
            throw new NoSuchElementException();
        }
    }

    public ArrayList<T> getTasks() {
        ArrayList<T> tasks = new ArrayList<>();
        Node<T> node = head;
        for (int i = 0; i < size; i++) {
            if (node != null) {
                tasks.add(node.data);
                node = node.next;
            } else {
                return tasks;
            }

        }
        return tasks;
    }

    public void removeById(int id) {
        Node<T> n = map.get(id);
        removeNode(n);
        map.remove(id);
    }

    public boolean contain(int id) {
        return map.containsKey(id);
    }

    private void removeNode(Node<T> n) {
        if (n == null) {
            return;
        } else {
            if (n == head && n != tail) {
                head = n.next;
                head.prev = null;
            } else if (n == tail && n != head) {
                tail = n.prev;
                tail.next = null;
            } else if (n == head && n == tail) {
                head = null;
                tail = null;
            } else if (n != head && n != tail) {
                n.prev.next = n.next;
                n.next.prev = n.prev;
            }
            size--;
        }
    }

    class Node<E> {

        private E data;
        private Node<E> next;
        private Node<E> prev;

        private Node(Node<E> prev, E data, Node<E> next) {
            this.prev = prev;
            this.next = next;
            this.data = data;
        }

    }
}
