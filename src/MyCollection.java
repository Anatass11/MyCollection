import java.util.Collection;

public interface MyCollection<T> {
    void add(T item);
    void add(int index, T item);
    T get(int index);
    void set(int index, T item);
    T remove(int index);
    boolean remove(T item);
    boolean removeAll(Collection<? extends T> collection);
    void addAll(Collection<? extends T> collection);
    void bubbleSort();
    void quickSort();
    boolean contain(T item);
    int indexOf(T item);
    int size();
    void clear();
    boolean isEmpty();
    T[] toArray();
    T[] toArray(T[] temp);
    static void sort(Collection collection){
        MyArrayList temp = new MyArrayList<>(collection);
        temp.quickSort();
        collection.clear();
        for (int i = 0; i < temp.size(); ++i){
            collection.add(temp.get(i));
        }
    }
}
