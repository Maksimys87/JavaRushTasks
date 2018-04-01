package com.javarush.task.task37.task3707;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class AmigoSet<E> extends AbstractSet<E> implements Set<E>, Serializable, Cloneable {
    private static final Object PRESENT = new Object();
    private transient HashMap<E, Object> map;


    public AmigoSet() {
        this.map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        this.map = new HashMap<>(Math.max(16, (int) (collection.size() / .75f) + 1));
        addAll(collection);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public Object clone() {
        try {
            AmigoSet<E> copy = (AmigoSet<E>) super.clone();
            copy.map = (HashMap<E, Object>) map.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(HashMapReflectionHelper.callHiddenMethod(map, "capacity"));
        out.writeFloat(HashMapReflectionHelper.callHiddenMethod(map, "loadFactor"));
        out.writeInt(map.size());
        for (E e : map.keySet())
            out.writeObject(e);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int capacity = in.readInt();
        float loadFactor = in.readFloat();
        int size = in.readInt();
        map = new HashMap<>(capacity, loadFactor);
        for (int i = 0; i < size; i++) {
            E e = (E) in.readObject();
            map.put(e, PRESENT);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AmigoSet<?> amigoSet = (AmigoSet<?>) o;
        return Objects.equals(map, amigoSet.map);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), map);
    }
}
