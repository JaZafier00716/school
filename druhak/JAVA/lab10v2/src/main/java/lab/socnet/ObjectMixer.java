package lab.socnet;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class ObjectMixer<T> {
    private List<T> list;

    public ObjectMixer() {
        this.list = new ArrayList<>();
    }

    public void add(List<T> objs) {
        list.addAll(objs);
        shuffle(list);
    }

    public void add(T obj) {
        list.add(obj);
        shuffle(list);
    }

    public T getFirst() {
        T first = list.getFirst();
        list.removeFirst();
        return first;
    }
}
