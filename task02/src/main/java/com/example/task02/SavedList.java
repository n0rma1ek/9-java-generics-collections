
package com.example.task02;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {

    private final File file;
    private final List<E> elements;

    public SavedList(File file) {
        this.file = file;
        this.elements = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream in =
                         new ObjectInputStream(
                                 new FileInputStream(file))) {

                @SuppressWarnings("unchecked")
                List<E> loaded = (List<E>) in.readObject();
                elements.addAll(loaded);

            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(
                        "Ошибка загрузки списка", e);
            }
        }
    }

    private void save() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(file))) {

            out.writeObject(elements);

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Ошибка сохранения списка", e);
        }
    }

    @Override
    public E get(int index) {
        return elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public E set(int index, E element) {
        E old = elements.set(index, element);
        save();
        return old;
    }

    @Override
    public void add(int index, E element) {
        elements.add(index, element);
        modCount++;
        save();
    }

    @Override
    public E remove(int index) {
        E removed = elements.remove(index);
        modCount++;
        save();
        return removed;
    }
}
