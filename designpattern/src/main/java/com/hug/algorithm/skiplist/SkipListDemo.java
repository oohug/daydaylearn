package com.hug.algorithm.skiplist;

public class SkipListDemo {
    public static void main(String[] args) {
        SkipList<String> list = new SkipList<String>();
        list.put(10, "sho");
        list.put(1, "sha");
        list.put(9, "na");
        list.put(2, "bing");
        list.put(8, "ling");

        print(list);

        list.put(7, "xiao");
        list.put(100, "你好，skiplist");
        list.put(5, "冰");
        list.put(6, "灵");
        for (int i = 0; i < 20; i++) {
//            list.put(i, "i");
        }

        print(list);


        System.out.println("列表元素：\n" + list);
        System.out.println("删除100：" + list.remove(100));
        System.out.println("列表元素：\n" + list);
        System.out.println("5对于的value：\n" + list.get(5).value);
        System.out.println("链表大小：" + list.size() + ",深度：" + list.getListLevel());
    }

    public static void print(SkipList<String> list){
        System.out.println("next==========");

        SkipListNode head = list.getHead();

        SkipListNode head1 = head;
        System.out.print(" " + head.key);
        while (head1.next != null) {
            System.out.print(" " + head1.next.key);
            head1 = head1.next;
        }

        System.out.println("down==========");
        SkipListNode head2 = head;
        while (head2.down != null) {
            SkipListNode d1 = head2.down;
            System.out.print(" " + d1.key);
            while (d1.next != null) {
                System.out.print(" " + d1.next.key);
                d1 = d1.next;
            }
            head2 = head2.down;
            System.out.println("down-down==========");
        }
    }
}