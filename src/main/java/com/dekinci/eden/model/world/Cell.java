package com.dekinci.eden.model.world;

import com.dekinci.eden.model.animal.Animal;
import com.dekinci.eden.model.animal.Hare;
import com.dekinci.eden.model.animal.Wolf;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Cell implements Iterable<Animal> {
    private Animal a1;
    private Animal a2;

    public Cell() {
    }

    public boolean add(Animal animal) {
        if (a1 == null) {
            a1 = animal;
            return true;
        }
        if (a2 == null) {
            a2 = animal;
            return true;
        }

        return false;
    }

    public boolean isActive() {
        return a1 != null && a1.alive() || a2 != null && a2.alive();
    }

    public boolean isFull() {
        return a1 != null && a2 != null;
    }

    public boolean remove(Animal animal) {
        if (a1 != null && a1.equals(animal)) {
            a1 = null;
            return true;
        }
        if (a2 != null && a2.equals(animal)) {
            a2 = null;
            return true;
        }

        return false;
    }

    public Animal getOther(Animal a) {
        if (a1 == a)
            return a2;
        if (a2 == a)
            return a2;
        return null;
    }

    @Override
    public String toString() {
        return "{" + a1 + ";" + a2 + "}";
    }

    @Override
    public Iterator<Animal> iterator() {
        return new Iterator<>() {
            int pos = 0;

            @Override
            public boolean hasNext() {
                return (a1 != null ? 1 : 0) + (a2 != null ? 1 : 0) > pos;
            }

            @Override
            public Animal next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                return pos++ == 0 ? (a1 != null ? a1 : a2) : a2;
            }
        };
    }
}
