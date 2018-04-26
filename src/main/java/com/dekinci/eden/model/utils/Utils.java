package com.dekinci.eden.model.utils;

import java.util.Objects;
import java.util.function.Consumer;

public class Utils {
    public static void forEach(int n, Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < n; i++)
            action.accept(i);
    }
}
