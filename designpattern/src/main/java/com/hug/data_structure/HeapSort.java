package com.hug.data_structure;

/**
 * 大顶堆:每个结点的值都大于或等于其左右孩子结点的值；
 * 大顶堆：arr[i] >= arr[2i+1] && arr[i] >= arr[2i+2]
 * 小顶堆：每个结点的值都小于或等于其左右孩子结点的值；
 * 小顶堆：arr[i] <= arr[2i+1] && arr[i] <= arr[2i+2]
 *
 * 排序思想：（假设有一个n个元素的数组 arr）
 *  1、将数组数据构造成一个大顶堆，此时序列的最大值就是堆顶的根节点
 *  2、将1中的根节点和数组的尾元素交换，此时数组的最大值已确定
 *  3、将剩余 n-1 个元素重新构造成一个 大顶堆，将会得到剩余元素的最大值 （重复 1 2 步骤）
 *
 *     a.将无需序列构建成一个堆，根据升序降序需求选择大顶堆或小顶堆;
 * 　　b.将堆顶元素与末尾元素交换，将最大元素"沉"到数组末端;
 * 　　c.重新调整结构，使其满足堆定义，然后继续交换堆顶元素与当前末尾元素，反复执行调整+交换步骤，直到整个序列有序。
 *
 * 　堆排序是一种选择排序，整体主要由构建初始堆+交换堆顶元素和末尾元素并重建堆两部分组成。
 *      其中构建初始堆经推导复杂度为O(n)，在交换并重建堆的过程中，需交换n-1次，
 *      而重建堆的过程中，根据完全二叉树的性质，[log2(n-1),log2(n-2)...1]逐步递减，近似为nlogn。
 *      所以堆排序时间复杂度一般认为就是O(nlogn)级。
 *
 * 堆排序，采用顺序存储
 * 大根堆
 */
public class HeapSort {
    int[] arr;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HeapSort heapSor = new HeapSort();
        int[] arr = {7, 23, 45, 9, 40, 73, 12, 49};  // 0下标放的是数组长度，
        heapSor.arr = arr;
        heapSor.heapSort(arr);

        for (int i = 0; i < arr.length; i++)
            System.out.print(".." + arr[i]);
    }

    void heapAdjust(int[] arr, int s, int m) {
        //已知arr[s...m]中记录的关键字除arr[s]之外均满足堆的定义，本函数调整arr[s]的关键字，使arr[s...m]成为一个最大堆
        int rc = arr[s]; //s是最后一个非叶子节点

        for (int j = 2 * s; j <= m; j = s * 2) {
            if (j < m && arr[j] < arr[j + 1])
                j++;  //j为key较大的下标
            if (rc >= arr[j]) break;
            arr[s] = arr[j];  //上移到父节点
            s = j;
        }
        arr[s] = rc;  //要放入的位置

    }

    void heapSort(int[] arr) {
        //对数组进行建堆操作，就是从最后一个非叶结点进行筛选的过程
        for (int i = (arr.length - 1) / 2; i > 0; i--) {    // 因为0没有使用，所以length-1
            heapAdjust(arr, i, arr.length - 1);
        }
        System.out.println("........建堆完成.............");

        outputArr(1);
        for (int i = arr.length - 1; i > 1; i--) {
            int temp = arr[1];
            arr[1] = arr[i];
            arr[i] = temp;
            heapAdjust(arr, 1, i - 1);
        }
    }

    void outputArr(int i) {

        if (i <= arr[0]) {
            System.out.println(arr[i]);
            outputArr(i * 2);  //左
            outputArr(i * 2 + 1);  //右
        }
    }
}