package dev.gdalia.commandsplus.service;

public interface DataBaseExecutorService {

    void init();

    boolean openConnectionPool();

    void closeConnection();

    void clearCurrentServiceLoader();

    void commit();
}
