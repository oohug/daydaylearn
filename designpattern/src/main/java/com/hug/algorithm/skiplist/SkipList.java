package com.hug.algorithm.skiplist;

import java.util.Random;

public class SkipList<T> {
    private SkipListNode<T> head, tail;
    private int size; // ?
    private int listLevel; // 跳表层高
    private Random random;
    private static final double PROBABILITY = 0.5; // 概率?

    public SkipList() {
        head = new SkipListNode<T>(SkipListNode.HEAD_KEY, null);
        tail = new SkipListNode<>(SkipListNode.TAIL_KEY, null);
        head.next = tail;
        tail.pre = head;
        size = 0;
        listLevel = 0;
        random = new Random();
    }

    public SkipListNode<T> get(int key) {
        SkipListNode<T> p = findNode(key);
        if (p.key == key) {
            return p;
        }
        return null;
    }

    // 首先查找到包含key值的节点，将节点从链表中移除，接着如果有更高level的节点，则repeat这个操作即可。
    public T remove(int k) {
        SkipListNode<T> p = get(k);
        if (p == null) {
            return null;
        }
        T oldV = p.value;
        SkipListNode<T> q;
        while (p != null) {
            q = p.next;
            q.pre = p.pre;
            p.pre.next = q;
            p = p.up;
        }
        return oldV;
    }

    /**
     * 1.如果put的key值在跳跃表中存在，则进行修改操作；
     * 2.如果put的key值在跳跃表中不存在，则需要进行新增节点的操作，并且需要由random随机数决定新加入的节点的高度（最大level）；
     * 3.当新添加的节点高度达到跳跃表的最大level，需要添加一个空白层（除了-oo和+oo没有别的节点）
     *
     * @param k
     * @param v
     */
    public void put(int k, T v) {
        //这里不用get是因为下面可能用到这个节点
        SkipListNode<T> p = findNode(k); // p ? key值不存在时，返回跳跃表中key值小于key，并且key值相差最小的底层节点
        // 存在进行修改
        if (p.key == k) {
            p.value = v;
            return;
        }
        // 准备put
        SkipListNode<T> q = new SkipListNode<>(k, v);
        insertNode(p, q);

        int currentLevel = 0;
        // 随机数升层
        while (random.nextDouble() > PROBABILITY) {
            if (currentLevel >= listLevel) {
                addEmptyLevel();
                System.out.println("第一次升层");
            }
            while (p.up == null) {
                System.out.println(p);
                p = p.pre;
                System.out.println("找到第一个有上层结点的值" + p);
            }

            p = p.up;
            //创建 q的镜像变量（只存储k，不存储v，因为查找的时候会自动找最底层数据）
            SkipListNode<T> z = new SkipListNode<>(k, null);
            insertNode(p, z);
            z.down = q;
            q.up = z;
            //别忘了把指针移到上一层。
            q = z;
            currentLevel++;
            System.out.println("添加后" + this);
        }

        size++;
    }

    /**
     * 如果 当前为空呢?
     * 如果传入的key值在跳跃表中不存在，则findNode返回跳跃表中key值小于key，并且key值相差最小的底层节点;
     * 所以不能用此方法来代替get
     *
     * @param key
     * @return
     */
    public SkipListNode<T> findNode(int key) {
        SkipListNode<T> p = head;
        while (true) {
            System.out.println("p.next.key:" + p.next.key);
            if (p.next != null && p.next.key <= key) {
                p = p.next;
            }
            System.out.println("找到node:" + p);
            if (p.down != null) {
                p = p.down;
                System.out.println("node.down :" + p);
            } else if (p.next != null && p.next.key > key) {
                break;
            }
        }
        return p;
    }

    /**
     * 初始化level , set listLevel = 1
     */
    public void addEmptyLevel() {
        SkipListNode<T> headerNode = new SkipListNode<T>(SkipListNode.HEAD_KEY, null);
        SkipListNode<T> tailNode = new SkipListNode<T>(SkipListNode.TAIL_KEY, null);
        headerNode.next = tailNode;
        headerNode.down = head;
        tailNode.pre = headerNode;
        tailNode.down = tail;
        head.up = headerNode;
        tail.up = tailNode;
        head = headerNode;
        tail = tailNode;
        //
        listLevel++;
    }

    /**
     * @param p
     * @param q
     */
    private void insertNode(SkipListNode<T> p, SkipListNode<T> q) {
        q.next = p.next;
        q.pre = p;
        p.next.pre = q;
        p.next = q;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public SkipListNode<T> getHead() {
        return head;
    }

    public void setHead(SkipListNode<T> head) {
        this.head = head;
    }

    public SkipListNode<T> getTail() {
        return tail;
    }

    public void setTail(SkipListNode<T> tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getListLevel() {
        return listLevel;
    }

    public void setListLevel(int listLevel) {
        this.listLevel = listLevel;
    }
}
