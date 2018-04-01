package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/*
!!!!!!!!!!!!!
Валидацию прошел немного с другим решением. Этот вариант решения был сделан позже.
Мне он кажется более правильным и логичным (смотреть картинку по ссылке в условии и выводы в My Tests)
Вкратце логика реализации:
При удалении какого-либо элемента дерева, на его место больше нельзя добавить другой элемент.
При следующем добавлении, элемент добавляется на следующее свободное место в дереве.
В случае, если возникнет ситуация, когда в дерево больше нельзя добавить ни одного элемента
(например, при удалении левого и правого поддерева у корневого элемента), запрет на добавление сбрасывается у всех элементов дерева
и можно снова добавлять элементы в дерево по порядку.
Не знаю или такое решение пройдет валидацию!
!!!!!!!!!!!!
*/

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root = new Entry<>("0");

    public static void main(String[] args) {
        List<String> list = new CustomTree();
        for (int i = 1; i < 16; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println(list.size());
        System.out.println("Expected 3, actual is " + ((CustomTree) list).getParent("8"));
        list.remove("5");
        System.out.println("Expected null, actual is " + ((CustomTree) list).getParent("11"));
        System.out.println(list.size());

        // My Tests
        /*
        System.out.println(list.size());
        list.remove("1");
        System.out.println("Expected 7, actual is " + list.size());
        list.add("16");
        System.out.println("Expected 8, actual is " + list.size());
        System.out.println("Expected 11, actual is " + ((CustomTree) list).getParent("16"));
        list.remove("2");
        System.out.println("Expected 0, actual is " + list.size());
        list.add("17");
        System.out.println("Expected 1, actual is " + list.size());
        System.out.println("Expected 0, actual is " + ((CustomTree) list).getParent("17"));
        list.add("18");
        System.out.println("Expected 2, actual is " + list.size());
        System.out.println("Expected 0, actual is " + ((CustomTree) list).getParent("18"));
        list.add("19");
        System.out.println("Expected 3, actual is " + list.size());
        System.out.println("Expected 17, actual is " + ((CustomTree) list).getParent("19"));
        list.add("20");
        System.out.println("Expected 4, actual is " + list.size());
        System.out.println("Expected 17, actual is " + ((CustomTree) list).getParent("20"));
        list.add("21");
        System.out.println("Expected 5, actual is " + list.size());
        System.out.println("Expected 18, actual is " + ((CustomTree) list).getParent("21"));
        */
    }

    static class Entry<T> implements Serializable {
        String elementName;
        int lineNumber;       // not used
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            this.availableToAddLeftChildren = true;
            this.availableToAddRightChildren = true;
        }

        void checkChildren() {
            if (leftChild != null) availableToAddLeftChildren = false;
            if (rightChild != null) availableToAddRightChildren = false;
        }

        boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }

    @Override
    public boolean add(String s) {
        Queue<Entry<String>> queue = new LinkedList<>();
        Entry<String> iter = root;

        if (addChildren(queue, iter, s)) return true;

        while (true) {
            iter.availableToAddLeftChildren = true;
            iter.availableToAddRightChildren = true;

            if (iter.leftChild != null) queue.add(iter.leftChild);
            if (iter.rightChild != null) queue.add(iter.rightChild);
            if (!queue.isEmpty()) iter = queue.poll();
            else break;
        }

        addChildren(queue, iter, s);

        return false;
    }

    public boolean addChildren(Queue<Entry<String>> queue, Entry<String> iter, String s) {
        while (true) {
            if (iter.isAvailableToAddChildren()) {
                if (iter.availableToAddLeftChildren) {
                    iter.leftChild = new Entry<>(s);
                    iter.leftChild.parent = iter;
                    iter.checkChildren();
                    return true;
                }
                if (iter.availableToAddRightChildren) {
                    iter.rightChild = new Entry<>(s);
                    iter.rightChild.parent = iter;
                    iter.checkChildren();
                    return true;
                }
            } else {
                if (iter.leftChild != null) queue.add(iter.leftChild);
                if (iter.rightChild != null) queue.add(iter.rightChild);
                if (!queue.isEmpty()) iter = queue.poll();
                else break;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        String name = (String) o;
        if (name.equals(root.elementName)) {
            root = null;
            return true;
        }
        Queue<Entry<String>> queue = new LinkedList<>();
        Entry<String> iter = root;
        while (true) {
            if (iter.elementName.equals(name)) {
                if (iter.parent.leftChild == iter) {
                    iter.parent.availableToAddLeftChildren = false;
                    iter.parent.leftChild = null;
                    return true;
                } else {
                    iter.parent.availableToAddRightChildren = false;
                    iter.parent.rightChild = null;
                    return true;
                }
            }
            if (iter.leftChild != null) queue.add(iter.leftChild);
            if (iter.rightChild != null) queue.add(iter.rightChild);
            if (!queue.isEmpty()) iter = queue.poll();
            else break;
        }
        return false;
    }

    @Override
    public int size() {
        int size = 0;
        Queue<Entry<String>> queue = new LinkedList<>();
        Entry<String> iter = root;
        while (true) {
            if (iter.leftChild != null) queue.add(iter.leftChild);
            if (iter.rightChild != null) queue.add(iter.rightChild);
            if (!queue.isEmpty()) iter = queue.poll();
            else break;
            size++;
        }
        return size;
    }

    public String getParent(String s) {
        if (s == null || (s.equals(root.elementName))) return null;
        Queue<Entry<String>> queue = new LinkedList<>();
        Entry<String> iter = root;
        while (true) {
            if (iter.leftChild != null && iter.leftChild.elementName.equals(s)) return iter.elementName;
            if (iter.rightChild != null && iter.rightChild.elementName.equals(s)) return iter.elementName;
            if (iter.leftChild != null) queue.add(iter.leftChild);
            if (iter.rightChild != null) queue.add(iter.rightChild);
            if (!queue.isEmpty()) iter = queue.poll();
            else break;
        }
        return null;
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }
}