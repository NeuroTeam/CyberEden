package com.dekinci.eden.utils;

public interface FactCallback extends ResultCallback<Void> {
    default void success() {
        success(null);
    }
}
