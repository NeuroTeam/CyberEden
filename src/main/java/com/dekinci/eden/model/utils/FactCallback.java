package com.dekinci.eden.model.utils;

public interface FactCallback extends ResultCallback<Void> {
    default void success() {
        success(null);
    }
}
