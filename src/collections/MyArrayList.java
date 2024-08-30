package collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MyArrayList<T> implements MyCollection<T> {
    private int size = 0, buffer = 10;
    private static final double INCREASE = 1.5;

    private T[] items;

    public MyArrayList(){}

    public MyArrayList(T[] items) {
        this.items = items;
        this.buffer = items.length;
        this.size = items.length;
    }
    public MyArrayList(Collection<T> collection){
        this.addAll(collection);
    }

    private T[] getItem() {
        return items;
    }

    private void setItem(T[] items) {
        this.items = items;
        this.buffer = items.length;
        this.size = items.length;
    }

    @SuppressWarnings("unchecked")
    private T[] create(Class<T> clas, int size){
        return (T[]) Array.newInstance(clas, size);
    }

    private static <R> MyArrayList<R> combine(MyArrayList<R> left, R center, MyArrayList<R> right){
        MyArrayList<R> temp = new MyArrayList<>();
        if(!left.isEmpty()){
            temp.addAll(Arrays.asList(left.toArray()));
        }
        temp.add(center);
        if(!right.isEmpty()) {
            temp.addAll(Arrays.asList(right.toArray()));
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    private <R extends Comparable<R>> boolean isSorted(){
        R[] temp = (R[]) this.items;
        if(this.size == 1) return true;
        for (int i = 0; i < this.size-1; ++i){
            if(temp[i].compareTo(temp[i+1]) > 0){
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void trimToSize(){
        T[] temp = create((Class<T>) this.get(0).getClass(), size);
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        buffer = size;
        this.items = temp;
    }

    @SuppressWarnings("unchecked")
    private void expand(){
        buffer *= INCREASE;
        T[] temp = this.items;
        this.items = create((Class<T>) this.get(0).getClass(), buffer);
        if (size >= 0) System.arraycopy(temp, 0, this.items, 0, size);
    }

    private void checkItem(T item){
        if(item == null) throw new NullPointerException("Item is null!");
    }

    private void checkIndex(int index){
        if(index >= size || index < 0) throw new IndexOutOfBoundsException("Out of bounds!");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(T item) {
        checkItem(item);
        if(this.items == null) this.items = create((Class<T>) item.getClass(), buffer);
        else if(size == buffer) expand();
        this.items[size++] = item;
    }

    @Override
    public void add(int index, T item) {
        checkIndex(index);
        checkItem(item);
        if(size == buffer) expand();
        for(int i = size; i > index; --i) this.items[i] = this.items[i-1];
        this.items[index] = item;
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return this.items[index];
    }

    @Override
    public void set(int index, T item) {
        checkItem(item);
        checkIndex(index);
        this.items[index] = item;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T temp = items[index];
        for (int i = index; i < size-1; ++i){
            items[i] = items[i+1];
        }
        items[--size] = null;
        return temp;
    }

    @Override
    public boolean remove(T item) {
        checkItem(item);
        return remove(indexOf(item)) != null;
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection) {
        for(T item: collection){
            if(!remove(item)) return false;
        }
        return true;
    }

    @Override
    public void addAll(Collection<? extends T> collection) {
        for(T item: collection){
            add(item);
        }
    }

    public static <R extends Comparable<R>> void bubbleSort(MyArrayList<R> sort) {
        for (int out = sort.size() - 1; out >= 1; out--){
            boolean sorted = true;
            for (int in = 0; in < out; in++){
                if(sort.get(in).compareTo(sort.get(in+1)) > 0){
                    R temp = sort.get(in);
                    sort.set(in, sort.get(in+1));
                    sort.set(in+1, temp);
                    sorted = false;
                }
            }
            if(sorted) break;
        }
    }

    public static <R extends Comparable<R>> void quickSort(MyArrayList<R> sort) {
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
        if(!big.isEmpty()) quickSort(big);
        if(!small.isEmpty()) quickSort(small);
        sort.setItem(combine(small, center, big).toArray());
    }

    @Override
    public boolean contain(T item) {
        checkItem(item);
        return indexOf(item) != -1;
    }

    @Override
    public int indexOf(T item) {
        checkItem(item);
        int i;
        for(i = 0; i < size; ++i){
            if(items[i] == item){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.items = null;
        this.size = 0;
        this.buffer = 10;
    }

    @Override
    public boolean isEmpty() {
        return (this.items == null || size == 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] temp = create((Class<T>) this.get(0).getClass(), size);
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        return temp;
    }

    @Override
    public T[] toArray(T[] temp) {
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        return temp;
    }

    public String toString(){
        StringBuilder res = new StringBuilder("[ ");
        for(int i = 0; i < size; ++i){
            res.append(items[i]);
            res.append(", ");
        }
        res.delete(res.length()-2, res.length()-1);
        res.append("]");
        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyArrayList<?> that = (MyArrayList<?>) o;
        return size == that.size && buffer == that.buffer && Arrays.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, buffer);
        result = 31 * result + Arrays.hashCode(items);
        return result;
    }
}
