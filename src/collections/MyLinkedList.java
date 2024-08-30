package collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MyLinkedList<T> implements MyCollection<T> {

    private T item;
    private int size;

    private MyLinkedList<T> next, prev;

    public MyLinkedList(){};

    public MyLinkedList(T item) {
        this.item = item;
    }

    public MyLinkedList(Collection<T> collection){
        this.addAll(collection);
    }

    private void setNext(MyLinkedList<T> next) {
        this.next = next;
    }

    private void setPrev(MyLinkedList<T> prev) {
        this.prev = prev;
    }

    private void setItem(T item) {
        this.item = item;
    }

    private MyLinkedList<T> getNext() {
        return next;
    }

    private MyLinkedList<T> getPrev() {
        return prev;
    }

    private T getItem() {
        return item;
    }

    private void link(MyLinkedList<T> left, MyLinkedList<T> center, MyLinkedList<T> right){
        center.setNext(right);
        center.setPrev(left);
        left.setNext(center);
        right.setPrev(center);
    }

    private MyLinkedList<T> scroll(int index){
        MyLinkedList<T> H = this;
        index = index % this.size;
        if(index < 0) index = size + index;
        if(index <= this.size/2) {
            for (int i = 0; i < index; ++i) {
                H = H.getNext();
            }
        }
        else {
            for (int i = this.size; i > index; --i) {
                H = H.getPrev();
            }
        }
        return H;
    }

    private static <T> MyLinkedList<T> combine(MyArrayList<T> left, T center, MyArrayList<T> right){
        MyArrayList<T> temp = new MyArrayList<>();
        if(!left.isEmpty()){
            temp.addAll(Arrays.asList(left.toArray()));
        }
        temp.add(center);
        if(!right.isEmpty()) {
            temp.addAll(Arrays.asList(right.toArray()));
        }
        return new MyLinkedList<>(Arrays.stream(temp.toArray()).toList());
    }

    @SuppressWarnings("unchecked")
    private <R extends Comparable<R>> boolean isSorted(){
        MyLinkedList<R> H = (MyLinkedList<R>) this;
        if(this.size() == 1) return true;
        for (int i = 0; i < this.size() - 1; ++i){
            if(H.getItem().compareTo(H.getNext().getItem()) > 0){
                return false;
            }
            H = H.getNext();
        }
        return true;
    }

    private void checkItem(T item){
        if(item == null) throw new NullPointerException("Item is null!");
    }

    @Override
    public void add(T item) {
        checkItem(item);
        if(this.getItem() == null) this.setItem((T)item);
        else if(this.getPrev() == null){
            this.link(this, new MyLinkedList<>(item), this);
        }
        else {
            MyLinkedList<T> H = this.getPrev();
            H.link(H, new MyLinkedList<>(item), this);
        }
        ++size;
    }

    @Override
    public void add(int index, T item) {
        checkItem(item);
        MyLinkedList<T> H, temp;
        index = index % this.size;
        if(index == 0){
            this.link(this, new MyLinkedList<>(this.getItem()), this.getNext());
            this.setItem(item);
        }
        else {
            H = this.getPrev();
            H.link(H, new MyLinkedList<>(item), H.getNext());
        }
        ++size;
    }

    @Override
    public T get(int index) {
        return scroll(index % this.size).getItem();
    }

    @Override
    public void set(int index, T item) {
        checkItem(item);
        scroll(index % this.size).setItem(item);
    }

    @Override
    public T remove(int index) {
        T num;
        index = index % this.size;
        if(index == 0) {
            num = this.getItem();
            this.setItem(this.getNext().getItem());
            this.setNext(this.getNext().getNext());
            this.getNext().setPrev(this);
        }
        else {
            MyLinkedList<T> H = scroll(index-1);
            num = (T) H.getNext().getItem();
            H.setNext(H.getNext().getNext());
            H.getNext().setPrev(H);
        }
        --size;
        return num;
    }

    @Override
    public boolean remove(T item) {
        checkItem(item);
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            if(H.getItem() == item){
                H.getNext().setPrev(H.getPrev());
                H.getPrev().setNext(H.getNext());
                return true;
            }
            H = H.getNext();
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection) {
        for(T item: collection){
            if(!remove(item)) return false;
        }
        return true;
    }

    public static <R extends Comparable<R>> void bubbleSort(MyLinkedList<R> sort) {
        boolean sorted;
        for (int out = sort.size() - 1; out >= 1; out--){
            MyLinkedList<R> H = sort;
            sorted = true;
            for (int in = 0; in < out; in++){
                if(H.getItem().compareTo(H.getNext().getItem()) > 0){
                    R temp = H.getItem();
                    H.setItem(H.getNext().getItem());
                    H.getNext().setItem(temp);
                    sorted = false;
                }
                H = H.getNext();
            }
            if(sorted) break;
        }
    }

    public static <R extends Comparable<R>> void quickSort(MyLinkedList<R> sort) {
        if(sort.isSorted()) return;
        MyArrayList<R> small = new MyArrayList<>(), big = new MyArrayList<>();
        int i = sort.size()/2;
        R center = sort.get(i);
        for (int j = 0; j < sort.size(); ++j) {
            if (sort.get(j).compareTo(center) > 0) {
                big.add(sort.get(j));
            }
            else if(j != i){
                small.add(sort.get(j));
            }
        }
        if(!big.isEmpty()) MyArrayList.quickSort(big);
        if(!small.isEmpty()) MyArrayList.quickSort(small);
        MyLinkedList<R> res = combine(small, center, big);
        sort.setNext(res.getNext());
        sort.setItem(res.getItem());
    }

    @Override
    public boolean contain(T item) {
        return this.indexOf(item) != -1;
    }

    @Override
    public int indexOf(T item) {
        checkItem(item);
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            if(H.getItem() == item){
                return i;
            }
            H = H.getNext();
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.setNext(null);
        this.setPrev(null);
        this.setItem(null);
    }

    @Override
    public boolean isEmpty() {
        return this.getItem() == null && this.getNext() == null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] temp = (T[]) Array.newInstance((this.getItem()).getClass(), size);
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            temp[i] = H.getItem();
            H = H.getNext();
        }
        return temp;
    }

    @Override
    public T[] toArray(T[] temp) {
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            temp[i] = H.getItem();
            H = H.getNext();
        }
        return temp;
    }

    @Override
    public void addAll(Collection<? extends T> collection) {
        for(T item: collection){
            this.add(item);
        }
    }

    public String toString(){
        StringBuilder res = new StringBuilder("[ ");
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            res.append(H.getItem());
            res.append(", ");
            H = H.getNext();
        }
        res.append(H.getItem());
        res.append("]");
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLinkedList<?> that = (MyLinkedList<?>) o;
        MyLinkedList<T> H = this;
        if(size == that.size){
            for(int i = 0; i < size; ++i){
                if(!Objects.equals(H.item, that.item)){
                    return false;
                }
                H = H.getNext();
                that = that.getNext();
            }
        }
        else return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, size, next, prev);
    }
}
