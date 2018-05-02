package com.dekinci.eden.model.utils;

public interface Callback<Result, Error, Message> {
    void success(Result result);

    default void error(Error error) {
    }

    default void update(Message message) {
    }
}
