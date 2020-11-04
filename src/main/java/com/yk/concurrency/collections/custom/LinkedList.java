package com.yk.concurrency.collections.custom;

/**
 * @program: ThreadLearning
 * @description: 自定义手写单向LinkedList
 * @author: YuKai Fan
 * @create: 2020-11-02 21:26
 **/
public class LinkedList<E> {

    private final static String PLAIN_NULL = "null";

    /**
     * 首节点
     */
    private Node<E> first;

    /**
     * 也可以不用写, 默认就是null
     */
    private final Node<E> NULL = (Node<E>) null;

    /**
     * 链表大小
     */
    private int size;

    public LinkedList() {
    }

    public LinkedList(Node<E> first) {
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
    public static <E> LinkedList<E> of(E... elements) {
        final LinkedList list = new LinkedList();
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
     * 让newNode的next 指向 FirstNode, 并把newNode 作为 FirstNode
     * @param e
     */
    public LinkedList<E> addFirst(E e) {
        final Node<E> newNode = new Node<>(e);
        newNode.next = first;
        this.size++;
        this.first = newNode;
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
            throw new NoElementException("The LinkedList is Empty");
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
        LinkedList<String> list = LinkedList.of("Hello", "World", "Scala", "Java", "Thread");
        list.addFirst("Concurrency").addFirst("Test");

        System.out.println(list.size());
        System.out.println(list.contains("Scala1"));
        System.out.println("=======================");
        System.out.println(list);

        while (!list.isEmpty()) {
            System.out.println(list.removeFirst());
        }

        System.out.println(list.size());
        System.out.println(list.isEmpty());
    }


}