package com.yk.concurrency.collections.custom;

/**
 * @program: ThreadLearning
 * @description: 有序LinkedList, 每一个节点都继承Comparable
 * @author: YuKai Fan
 * @create: 2020-11-02 22:13
 **/
public class PriorityLinkedList<E extends Comparable<E>>{
    private final static String PLAIN_NULL = "null";

    /**
     * 首节点
     */
    private Node<E> first;

    /**
     * 也可以不用写, 默认就是null
     */
    private final PriorityLinkedList.Node<E> NULL = (PriorityLinkedList.Node<E>) null;

    /**
     * 链表大小
     */
    private int size;

    public PriorityLinkedList() {
    }

    public PriorityLinkedList(PriorityLinkedList.Node<E> first) {
        this.first = NULL;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
     * 静态工厂方法, 批量添加Node
     * @param elements
     * @param <E>
     * @return
     */
    public static <E extends Comparable<E>> PriorityLinkedList<E> of(E... elements) {
        final PriorityLinkedList list = new PriorityLinkedList();
        if (elements.length != 0) {
            for (E element : elements) {
                list.addFirst(element);
            }
        }

        return list;
    }

    /**
     * 添加元素
     *
     * @param e
     */
    public PriorityLinkedList<E> addFirst(E e) {
        final Node<E> newNode = new Node<>(e);
        Node<E> previous = NULL;
        Node<E> current = first;
        // 如果插入的元素e, 比当前Node的value要大, 那么就把当前node指向node.next(也就是向后移一位)
        while (current != null && e.compareTo(current.value) > 0) {
            previous = current;
            current = current.next;
        }

        // 如果previous=null, 表示list的firstNode.value就是比元素e大, 那就直接把newNode作为first, newNode.next指向原来的first
        if (previous == NULL) {
            first = newNode;
        } else {
            previous.next = newNode;
        }
        newNode.next = current;
        size++;
        return this;
    }

    /**
     * 是否包含某一个Node
     * @param e
     * @return
     */
    public boolean contains(E e) {
        Node<E> current = first;
        while (current != null) {
            if (current.value == e) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * 移除第一个节点
     *
     * 将first节点保存到临时变量node中
     *
     * 将first.next节点作为first节点, 并返回node.value
     * @return
     */
    public E removeFirst() {
        if (this.isEmpty()) {
            throw new LinkedList.NoElementException("The LinkedList is Empty");
        }

        Node<E> node = first;
        first = node.next;
        size--;
        return node.value;
    }

    /**
     * 定义一个节点类
     * @param <E>
     */
    private static class Node<E> {
        E value;

        // 下一个节点
        Node<E> next;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (null != value) {
                return value.toString();
            }
            return PLAIN_NULL;
        }
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        } else {
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null) {
                builder.append(current.toString()).append(",");
                current = current.next;
            }

            builder.replace(builder.length() - 1, builder.length(), "]");
            return builder.toString();
        }
    }

    /**
     * 定义异常
     */
    static class NoElementException extends RuntimeException {
        public NoElementException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        PriorityLinkedList<Integer> list = PriorityLinkedList.of(10, 1, -10, 0, 100, 88, 90, 2);
        System.out.println(list);
    }
}