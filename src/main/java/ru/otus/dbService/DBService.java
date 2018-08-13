package ru.otus.dbService;


import ru.otus.cache.CacheEngine;
import ru.otus.dataSets.DataSet;
import ru.otus.dataSets.UserDataSet;

import java.util.List;

public interface DBService extends AutoCloseable{
    String getLocalStatus();

    public<T extends DataSet> void save(T dataSet);

    public<T extends DataSet> T read(long id , Class<T> clazz);

    public<T extends DataSet> T readByName(String name, Class<T> clazz);

    public<T extends DataSet> List<T> readAll(Class<T> clazz);

    <T extends DataSet> void addUsers(List<T> users);

    <T extends DataSet> T loadUser(long id, Class<T> clazz);

    void shutdown();
}
