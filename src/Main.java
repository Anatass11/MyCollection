
import collections.MyArrayList;
import collections.MyLinkedList;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int[] arg = new int[10000];
        for(int i = 0; i < 10000; ++i){
            arg[i] = random.nextInt(-100, 100);
        }
        ArrayList<Integer> rand = new ArrayList<>(IntStream.of(arg).boxed().toList());
        MyArrayList<Integer> array = new MyArrayList<>(rand);
        LinkedList<Integer> testList = new LinkedList<>();
        MyLinkedList<Integer> list = new MyLinkedList<>(rand);
        ArrayList<Integer> test = new ArrayList<>();
        /*array.add(5);
        array.add(2);

        testList.add(4);
        testList.add(3);
        testList.add(1);
        array.addAll(testList);
        System.out.println(array);
        //array.removeAll(testList);

        array.remove(2);
        array.remove((Integer) 1);
        array.set(0, 8);
        //array.bubbleSort();
        array.quickSort();
        System.out.println(array);
        System.out.println(array.contain(4));
        System.out.println(array.size());
        for(Integer ints : array.toArray(new Integer[array.size()])){
            System.out.println(ints);
        }*/
        /*list.add(5);
        list.add(2);
        test.add(4);
        test.add(3);
        test.add(1);
        System.out.println(rand);
        collections.MyCollection.sort(rand);
        System.out.println(rand);
        list.addAll(test);
        // = new collections.MyLinkedList<>(test);
        //list.quickSort();
        list.bubbleSort();
        System.out.println(list);
        list.remove(2);
        list.remove((Integer) 5);
        list.set(0, 7);
        System.out.println(list);
        System.out.println(list.size);
        System.out.println(list.contain(1));
        for(Integer ints : list.toArray(new Integer[list.size()])){
            System.out.println(ints);
        }
         */
        long startTime = System.currentTimeMillis();
        MyLinkedList.quickSort(list); //11440
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println(timeElapsed);
    }
}