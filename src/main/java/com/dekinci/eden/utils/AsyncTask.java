package com.dekinci.eden.utils;

import javafx.application.Platform;


public abstract class AsyncTask<Params, Progress, Result> {
    private boolean daemon = false;
    private Params[] params;

    public void onPreExecute() {
    }

    public abstract Result doInBackground(Params... params);

    public void onPostExecute(Result params) {
    }

    public void progressCallback(Progress... params) {
    }

    @SafeVarargs
    protected final void publishProgress(final Progress... params) {
        Platform.runLater(() -> progressCallback(params));
    }

    private final Thread backGroundThread = new Thread(() -> {
        final Result param = doInBackground(params);
        Platform.runLater(() -> onPostExecute(param));
    });

    @SafeVarargs
    public final void execute(final Params... params) {
        this.params = params;
        Platform.runLater(() -> {
            onPreExecute();
            backGroundThread.setDaemon(daemon);
            backGroundThread.start();
        });
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public final boolean isInterrupted() {
        return this.backGroundThread.isInterrupted();
    }

    public final boolean isAlive() {
        return this.backGroundThread.isAlive();
    }
}

