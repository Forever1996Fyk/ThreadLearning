package com.yk.concurrency.collections;

import java.util.Random;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-03 20:43
 **/
public class SimpleSkipList {
    /**
     * 头节点
     */
    private final static byte HEAD_BIT = (byte)-1;
    /**
     * 初始数据节点
     */
    private final static byte DATA_BIT = (byte)0;
    /**
     * 尾节点
     */
    private final static byte TAIL_BIT = (byte)-1;

    private static class Node {
        private Integer value;
        // 定义上下左右四个节点
        private Node up, down, left, right;
        private byte bit;

        public Node(Integer value, byte bit) {
            this.value = value;
            this.bit = bit;
        }

        public Node(Integer value) {
            this(value, DATA_BIT);
        }
    }

    private Node head;
    private Node tail;
    // 链表长度
    private int size;
    // 链表高度
    private int height;
    // 随机算法
    private Random random;

    /**
     * 构造初始化跳表
     */
    public SimpleSkipList() {
        this.head = new Node(null, HEAD_BIT);
        this.tail = new Node(null, TAIL_BIT);

        /*
        空的链表关系: head的右边是tail, tail的左边是head
         */
        head.right = tail;
        tail.left = head;
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 找一个元素
     * @param element
     * @return
     */
    private Node find(Integer element) {
        Node current = head;

        // 双层遍历, 对于跳表来说, 就是不断的判断current的right和down节点是否满足条件
        for (;;) {
            // 当前的节点右边不是tail节点, 并且当前节点的值小于element时, 那么current就往右移,
            while (current.right.bit != TAIL_BIT && current.right.value <= element) {
                current = current.right;
            }

            // 如果当前节点的down节点不为null, 不是最底层的链表
            if (current.down != null) {
                current = current.down;
            } else {
                break;
            }
        }
        // 不一定找到了这个element元素, 但是一定 current <= element < current.right(if exist)
        return current;
    }

    /**
     * 跳表插入一个元素
     *
     * @param element
     */
    public void add(Integer element) {
        // 先找出element近似的节点
        Node nearNode = this.find(element);

        // 创建一个新节点
        Node newNode = new Node(element);

        // 建立关系
        newNode.left = nearNode;
        newNode.right = nearNode.right;
        nearNode.right.left = newNode;
        nearNode.right = newNode;

        // 将新节点放到跳表的某一层
        int currentLevel = 0;
        // 如果随机数比0.5小, 那么就把这个新节点提一层
        while (random.nextDouble() < 0.5d) {

            // 如果当前层高>=原来的总高度
            if (currentLevel >= height) {
                height++;

                Node dumyHead = new Node(null, HEAD_BIT);
                Node dumyTail = new Node(null, TAIL_BIT);

                dumyHead.right = dumyTail;
                dumyHead.down = head;
                head.up = dumyHead;

                dumyTail.left = dumyHead;
                dumyTail.down = tail;
                tail.up = dumyTail;

                head = dumyHead;
                tail = dumyHead;
            }

            while ((nearNode != null) && nearNode.up == null) {
                nearNode = nearNode.left;
            }
            nearNode = nearNode.up;
            Node upNode = new Node(element);
            upNode.left = nearNode;
            upNode.right = nearNode.right;
            upNode.down = newNode;

            nearNode.right.left = upNode;
            nearNode.right = upNode;

            newNode.up = upNode;
            newNode = upNode;
            currentLevel++;
        }

        size++;
    }

    /**
     * 获取整个跳表的信息
     */
    public void dumpSkipList() {
        Node temp = head;
        int i = height + 1;
        while (temp != null) {
            System.out.printf("Total [%d] height [%d]", height + 1, i--);
            Node node = temp.right;
            // 判断是否是数据节点
            while (node.bit == DATA_BIT) {
                System.out.printf("->%d ", node.value);
                node = node.right;
            }
            System.out.printf("\n");
            temp = temp.down;
        }
        System.out.println("======================");
    }

    /**
     * 判断元素是否包含在跳表中
     * @param element
     * @return
     */
    public boolean contains(Integer element) {
        Node node = this.find(element);
        return (node.value == element);
    }

    public Integer get(Integer element) {
        Node node = this.find(element);
        return (node.value == element) ? node.value : null;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        SimpleSkipList skipList = new SimpleSkipList();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            skipList.add(random.nextInt(1000));
        }
        skipList.dumpSkipList();
    }

}