package dev.gdalia.commandsplus.service;

public class YmlExecutorService implements DataBaseExecutorService {

    @Override
    public void init() {

    }

    @Override
    public boolean openConnectionPool() {
        return false;
    }

    @Override
    public void closeConnection() {

    }

    @Override
    public void clearCurrentServiceLoader() {

    }

    @Override
    public void commit() {

    }
}
