
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MyLinkedList<T extends Comparable> implements MyCollection<T>{

    private T item;
    int size;

    private MyLinkedList<T> next, prev;

    public MyLinkedList(){};

    public MyLinkedList(T item) {
        this.item = item;
    }

    public MyLinkedList(Collection<T> collection){
        for(T item: collection){
            this.add(item);
        }
    }

    public void setNext(MyLinkedList<T> next) {
        this.next = next;
    }

    public void setPrev(MyLinkedList<T> prev) {
        this.prev = prev;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public MyLinkedList<T> getNext() {
        return next;
    }

    public MyLinkedList<T> getPrev() {
        return prev;
    }

    public T getItem() {
        return item;
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

    private MyLinkedList<T> scroll(int index){
        MyLinkedList<T> H = this;
        index = index % this.size;
        if(index < 0){
            if( (index * -1) <= this.size/2) {
                for (int i = 0; i > index; --i) {
                    H = H.getPrev();
                }
            }
            else {
                for (int i = this.size; i > (-1 * index); --i) {
                    H = H.getNext();
                }
            }
        }
        else {
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
        }
        return H;
    }


    @Override
    public void add(T item) {
        if(this.getItem() == null) this.setItem((T)item);
        else if(this.getPrev() == null){
            this.setNext(new MyLinkedList<>(item));
            this.setPrev(this.getNext());
            this.getNext().setPrev(this);
            this.getNext().setNext(this);
        }
        else {
            MyLinkedList<T> H = scroll(-1);
            H.setNext(new MyLinkedList<>(item));
            H.getNext().setPrev(H);
            H.getNext().setNext(this);
            this.setPrev(H.getNext());
        }
        ++size;
    }

    @Override
    public void add(int index, T item) {
        MyLinkedList<T> H, temp;
        index = index % this.size;
        if(index == 0){
            H = new MyLinkedList<>(this.getItem());
            H.setNext(this.getNext());
            H.setPrev(this);
            this.setNext(H);
            this.setItem(item);
        }
        else {
            H = scroll(index-1);
            temp = new MyLinkedList<>(item);
            temp.setNext(H.getNext());
            temp.setPrev(H);
            H.getNext().setPrev(temp);
            H.setNext(temp);
        }
        ++size;
    }

    @Override
    public T get(int index) {
        return scroll(index % this.size).getItem();
    }

    @Override
    public void set(int index, T item) {
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

    @SuppressWarnings("unchecked")
    private boolean isSorted(){
        MyLinkedList<T> H = this;
        boolean res = false;
        if(this.size == 1) return true;
        for (int i = 0; i < this.size; ++i){
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
    @SuppressWarnings("unchecked")
    public void bubbleSort() {
        for (int out = size - 1; out >= 1; out--){
            MyLinkedList<T> H = this;
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

    private MyLinkedList<T> combine(MyArrayList<T> left, T center, MyArrayList<T> right){
        MyArrayList<T> temp = new MyArrayList<>();
        if(left.getItem() != null){
            temp.addAll(Arrays.asList(left.toArray()));
        }
        temp.add(center);
        if(right.getItem() != null) {
            temp.addAll(Arrays.asList(right.toArray()));
        }
        return new MyLinkedList<>(Arrays.stream(temp.toArray()).toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void quickSort() {
        if(this.isSorted()) return;
        MyArrayList<T> small = new MyArrayList<>(), big = new MyArrayList<>();
        int i = this.size/2;
        T center = this.get(i);
        if(!this.isSorted()) {
            for (int j = 0; j < this.size; ++j) {
                if (this.get(i).compareTo(center) > 0) {
                    big.add(this.get(j));
                }
                else if(j != i){
                    small.add(this.get(j));
                }
            }
            if(big.getItem() != null) big.quickSort();
            if(small.getItem() != null) small.quickSort();
        }
        MyLinkedList<T> res = combine(small, center, big);
        this.setNext(res.getNext());
        this.setItem(res.getItem());
    }

    @Override
    public boolean contain(T item) {
        MyLinkedList<T> H = this;
        for (int i = 0; i < this.size; ++i){
            if(H.getItem() == item){
                break;
            }
            H = H.getNext();
        }
        return H.getItem() == item;
    }

    @Override
    public int indexOf(T item) {
        MyLinkedList<T> H = this;
        int i;
        for (i = 0; i < this.size; ++i){
            if(H.getItem() == item){
                return i;
            }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLinkedList<?> that = (MyLinkedList<?>) o;
        return Objects.equals(item, that.item) && Objects.equals(next, that.next) && Objects.equals(prev, that.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, next, prev);
    }
}
