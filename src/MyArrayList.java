import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MyArrayList<T extends Comparable> implements MyCollection<T>{
    private int size = 0, buffer = 10;

    private T[] items;

    public MyArrayList(){}

    public MyArrayList(T[] items) {
        this.items = items;
        this.buffer = items.length;
        this.size = items.length;
    }
    public MyArrayList(Collection<T> collection){
        for(T item: collection){
            this.add(item);
        }
    }

    public T[] getItem() {
        return items;
    }

    public void setItem(T[] items) {
        this.items = items;
        this.buffer = items.length;
        this.size = items.length;
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

    @SuppressWarnings("unchecked")
    private void expand(){
        buffer *= 1.5;
        T[] temp = this.items;
        this.items = (T[]) Array.newInstance((temp[0]).getClass(), buffer);
        if (size >= 0) System.arraycopy(temp, 0, this.items, 0, size);
    }

    @SuppressWarnings("unchecked")
    public void trimToSize(){
        T[] temp = (T[]) Array.newInstance((items[0]).getClass(), size);
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        buffer = size;
        this.items = temp;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void add(T item) {
        if(this.items == null){
            this.items = (T[]) Array.newInstance(item.getClass(), buffer);
        }
        else if(size == buffer){
            expand();
        }
        this.items[size++] = item;
    }

    @Override
    public void add(int index, T item) {
        if(size == buffer){
            expand();
        }
        for(int i = size; i > index; --i){
            this.items[i] = this.items[i-1];
        }
        this.items[index] = item;
        size++;
    }

    @Override
    public T get(int index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        return this.items[index];
    }

    @Override
    public void set(int index, T item) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        this.items[index] = item;
    }

    @Override
    public T remove(int index) {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
        T temp = items[index];
        for (int i = index; i < size-1; ++i){
            items[i] = items[i+1];
        }
        items[--size] = null;
        return temp;
    }

    @Override
    public boolean remove(T item) {
        return remove(indexOf(item)) != null;
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection) {
        boolean res = false;
        for(T item: collection){
            res = remove(item);
            if(!res) break;
        }
        return res;
    }

    @Override
    public void addAll(Collection<? extends T> collection) {
        for(T item: collection){
            add(item);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isSorted(){
        boolean res = false;
        if(this.size == 1) return true;
        for (int i = 0; i < this.size-1; ++i){
            if(items[i].compareTo(items[i+1]) > 0){
                res = false;
                break;
            }
            res = true;
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void bubbleSort() {
        for (int out = size - 1; out >= 1; out--){
            if(this.isSorted()) break;
            for (int in = 0; in < out; in++){
                if(items[in].compareTo(items[in+1]) > 0){
                    T temp = items[in];
                    items[in] = items[in+1];
                    items[in+1] = temp;
                }
            }
        }
    }

    private MyArrayList<T> combine(MyArrayList<T> left, T center, MyArrayList<T> right){
        MyArrayList<T> temp = new MyArrayList<>();
        if(left.getItem() != null){
            temp.addAll(Arrays.asList(left.toArray()));
        }
        temp.add(center);
        if(right.getItem() != null) {
            temp.addAll(Arrays.asList(right.toArray()));
        }
        return temp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void quickSort() {
        if(this.isSorted()) return;
        MyArrayList<T> small = new MyArrayList<>(), big = new MyArrayList<>();
        int i = this.size/2;
        T center = this.items[i];
        if(!this.isSorted()) {
            for (int j = 0; j < this.size; ++j) {
                if (items[j].compareTo(center) > 0) {
                    big.add(items[j]);
                }
                else if(j != i){
                    small.add(items[j]);
                }
            }
            if(big.getItem() != null) big.quickSort();
            if(small.getItem() != null) small.quickSort();
        }
        this.setItem(combine(small, center, big).toArray());
    }

    @Override
    public boolean contain(T item) {
        return indexOf(item) != -1;
    }

    @Override
    public int indexOf(T item) {
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
        T[] temp = (T[]) Array.newInstance((items[0]).getClass(), size);
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        return temp;
    }

    @Override
    public T[] toArray(T[] temp) {
        if (size >= 0) System.arraycopy(items, 0, temp, 0, size);
        return temp;
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
