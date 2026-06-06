package com.njht.webyun.publish;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/18 13:35
 * @Description: 12
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class NoticeTest {

    public static void main(String[] args) {
        int i = 26;
        int [] arr = {9,8,7,6,5,4,3,5,2,1,10,11};
        shellSort(arr,arr.length);
//        bubbleSort(arr);
//        simpleSelectSort(arr);
//        directInsertionSort(arr,i);
//        binarySearch(arr,i,0,arr.length);
    }

    /**
     * 希尔排序
     * @param arr
     */
    private static void shellSort(int[] arr,int gap) {
        gap = gap/2;
        for (int i = 0; i < gap; i++) {


        }

    }

    /**
     * 冒泡排序 （元素两两交换）
     * @param arr
     */
    private static void bubbleSort(int[] arr) {
        for (int i=0;i<arr.length;i++) {
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {



                }
            }
        }
        for (int i : arr) {
            System.out.print(i+" ");
        }
    }

    /**
     * 简单选择排序 （从小到大）记录最小元素的位置,并交换到首位
     * @param arr
     */
    private static void simpleSelectSort(int[] arr) {
        for (int j = 0; j < arr.length; j++) {
            // temp 记录最小数下标
            int temp = j;
            for (int i = j+1; i < arr.length; i++) {
                if (arr[i] < arr[temp]) {
                    temp = i;
                }
            }
            // for循环之后 temp 指向元素最小的位置 放到i的位置
            swap(arr,j,temp);
        }

        for (int i : arr) {
            System.out.print(i+" ");
        }
    }

    /**
     * 直接插入排序 (有序数组)
     * @param arr
     * @param i
     */
    private static void directInsertionSort(int[] arr, int i) {
        int [] returnArr = new int[arr.length+1];
        // 设置中间变量记录 i被替换的位置
        int temp = 0;
        for (int j = 0; j < arr.length; j++) {
            if (temp == 1){
                returnArr[j+1] = arr[j];
                continue;
            }
            if (i > arr[j]) {
                returnArr[j] = arr[j];
            } else {
                returnArr[j] = i;
                returnArr[j+1] = arr[j];
                temp = 1;
            }
        }
        for (int i1 : returnArr) {
            System.out.print(i1+" ");
        }

    }

    /**
     * 二分查找
     * @param arr
     * @param i
     */
    private static void binarySearch(int[] arr, int i,int left,int right) {
        int temp = left+(right-left)/2;
        if (arr[temp] == i) {
            System.out.println("i:"+arr[temp]);
            return;
        }
        if ((right-left)/2 == 0) {
            System.out.println("集合中不包含该元素");
            return;
        }
        if (arr[temp] > i) {
            System.out.println("当前值大于要获取的值："+arr[temp]);
            // 右边界被修改
            right = temp;
            binarySearch(arr,i,left,right);
        }
        if (arr[temp] < i) {
            // 坐边界被修改;
            left = temp;
            System.out.println("当前值小于要获取的值："+arr[temp]);
            binarySearch(arr,i,left,right);
        }
    }

    /**
     * 选择排序-堆排序
     * @param array 待排序数组
     * @return 已排序数组
     */
    public static int[] heapSort(int[] array) {
        //这里元素的索引是从0开始的,所以最后一个非叶子结点array.length/2 - 1
        for (int i = array.length / 2 - 1; i >= 0; i--) {
            adjustHeap(array, i, array.length);  //调整堆
        }

        // 上述逻辑,建堆结束
        // 下面,开始排序逻辑
        for (int j = array.length - 1; j > 0; j--) {
            // 元素交换,作用是去掉大顶堆
            // 把大顶堆的根元素,放到数组的最后；换句话说,就是每一次的堆调整之后,都会有一个元素到达自己的最终位置
            swap(array, 0, j);
            // 元素交换之后,毫无疑问,最后一个元素无需再考虑排序问题了。
            // 接下来我们需要排序的,就是已经去掉了部分元素的堆了,这也是为什么此方法放在循环里的原因
            // 而这里,实质上是自上而下,自左向右进行调整的
            adjustHeap(array, 0, j);
        }
        return array;
    }

    /**
     * 整个堆排序最关键的地方
     * @param array 待组堆
     * @param i 起始结点
     * @param length 堆的长度
     */
    public static void adjustHeap(int[] array, int i, int length) {
        // 先把当前元素取出来,因为当前元素可能要一直移动
        int temp = array[i];
        for (int k = 2 * i + 1; k < length; k = 2 * k + 1) {  //2*i+1为左子树i的左子树(因为i是从0开始的),2*k+1为k的左子树
            // 让k先指向子节点中最大的节点
            if (k + 1 < length && array[k] < array[k + 1]) {  //如果有右子树,并且右子树大于左子树
                k++;
            }
            //如果发现结点(左右子结点)大于根结点,则进行值的交换
            if (array[k] > temp) {
                swap(array, i, k);
                // 如果子节点更换了,那么,以子节点为根的子树会受到影响,所以,循环对子节点所在的树继续进行判断
                i  =  k;
            } else {  //不用交换,直接终止循环
                break;
            }
        }
    }

    /**
     * 交换元素
     * @param arr
     * @param a 元素的下标
     * @param b 元素的下标
     */
    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
