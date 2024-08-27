import java.util.Collection;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyArrayList<T extends Comparable> implements MyCollection<T>{
    private int size = 0;

    private T item;

    private MyArrayList<T> next;

    public MyArrayList(){}

    public MyArrayList(T item) {
        this.item = item;
    }
    public MyArrayList(Collection<T> collection){
        for(T item: collection){
            this.add(item);
        }
    }

    public T getItem() {
        return item;
    }

    public MyArrayList<T> getNext() {
        return next;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setNext(MyArrayList<T> next) {
        this.next = next;
    }

    public String toString(){
        StringBuilder res = new StringBuilder("[ ");
        MyArrayList<T> H = this;
        while (H.getNext() != null){
            res.append(H.getItem());
            res.append(", ");
            H = H.getNext();
        }
        res.append(H.getItem());
        res.append("]");
        return res.toString();
    }

    private MyArrayList<T> scroll(int index){
        MyArrayList<T> H = this;
        if(index == -1){
            while (H.getNext() != null) H = H.getNext();
        }
        else {
            for (int i = 0; i < index; ++i) {
                if (H.getNext() != null) H = H.getNext();
                else throw new IndexOutOfBoundsException("Index out of bounds!");
            }
        }
        return H;
    }


    @Override
    public void add(T item) {
        if(this.getItem() == null) this.setItem((T)item);
        else scroll(-1).setNext(new MyArrayList<>((T)item));
        ++size;
    }

    @Override
    public void add(int index, T item) {
        MyArrayList<T> H, temp;
        if(index == 0){
            H = new MyArrayList<>(this.getItem());
            H.setNext(this.getNext());
            this.setNext(H);
            this.setItem(item);
        }
        else {
            H = scroll(index-1);
            temp = new MyArrayList<>(item);
            temp.setNext(H.getNext());
            H.setNext(temp);
        }
        ++size;
    }

    @Override
    public T get(int index) {
        return scroll(index).getItem();
    }

    @Override
    public void set(int index, T item) {
        scroll(index).setItem(item);
    }

    @Override
    public T remove(int index) {
        T num;
        if(index == 0) {
            num = this.getItem();
            this.setItem(this.getNext().getItem());
            this.setNext(this.getNext().getNext());
        }
        else {
            MyArrayList<T> H = scroll(index-1);
            num = (T) H.getNext().getItem();
            H.setNext(H.getNext().getNext());
        }
        --size;
        return num;
    }

    @Override
    public boolean remove(T item) {
        return remove(indexOf(item)) != null;
    }

    @Override
    public boolean removeAll(Collection<? extends T> collection) {
        boolean res = false;
        for(T item: collection){
            res = this.remove(item);
            if(!res) break;
        }
        return res;
    }

    private boolean isSorted(){
        MyArrayList<T> H = this;
        boolean res = false;
        if(this.size == 1) return true;
        while (H.getNext() != null){
            if(H.getItem().compareTo(H.getNext().getItem()) > 0){
                res = false;
                break;
            }
            res = true;
            H = H.getNext();
        }
        return res;
    }

    @Override
    public void bubbleSort() {
        for (int out = size - 1; out >= 1; out--){
            MyArrayList<T> H = this;
            if(this.isSorted()) break;
            for (int in = 0; in < out; in++){
                if(H.getItem().compareTo(H.getNext().getItem()) > 0){
                    T temp = H.getItem();
                    H.setItem(H.getNext().getItem());
                    H.getNext().setItem(temp);
                }
                H = H.getNext();
            }
        }
    }

    private MyArrayList<T> combine(MyArrayList<T> left, MyArrayList<T> center, MyArrayList<T> right){
        if(right.getItem() != null) center.setNext(right);
        if(left.getItem() != null) left.scroll(left.size-1).setNext(center);
        else return center;
        return left;
    }

    @Override
    public void quickSort() {
        if(this.size == 1 || this.isSorted()) return;
        MyArrayList<T> small = new MyArrayList<>(), center, big = new MyArrayList<>();
        int i = this.size/2;
        center = this.scroll(i);
        for (int j = 0; j < this.size; ++j) {
            if (this.scroll(j).getItem().compareTo(center.getItem()) > 0) {
                big.add(this.scroll(j).getItem());
            }
            else if(j != i){
                small.add(this.scroll(j).getItem());
            }
        }
        if(big.getItem() != null) big.quickSort();
        if(small.getItem() != null) small.quickSort();
        MyArrayList<T> res = combine(small, center, big);
        this.setNext(res.getNext());
        this.setItem(res.getItem());
    }

    @Override
    public boolean contain(T item) {
        MyArrayList<T> H = this;
        while (H.getNext() != null && H.getItem() != item) H = H.getNext();
        return H.getItem() == item;
    }

    @Override
    public int indexOf(T item) {
        int i = 0;
        MyArrayList<T> H = this;
        while (H.getNext() != null){
            if(H.getItem() == item){
                return i;
            }
            ++i;
            H = H.getNext();
        }
        if(H.getItem() == item) return i;
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.setNext(null);
        this.setItem(null);
    }

    @Override
    public boolean isEmpty() {
        return this.getItem() == null && this.getNext() == null;
    }

    @Override
    public T[] toArray() {
        T[] temp = (T[]) new Object[this.size];
        MyArrayList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            temp[i] = H.getItem();
            H = H.getNext();
        }
        return temp;
    }

    @Override
    public T[] toArray(T[] temp) {
        MyArrayList<T> H = this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyArrayList<?> myArrayList = (MyArrayList<?>) o;
        return Objects.equals(item, myArrayList.item) && Objects.equals(next, myArrayList.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, next);
    }
}
