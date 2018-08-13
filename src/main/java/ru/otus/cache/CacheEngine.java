package ru.otus.cache;


import ru.otus.dataSets.DataSet;

import java.lang.ref.SoftReference;

public interface CacheEngine<T extends DataSet> {
    void put(CacheElement<T> element);
    CacheElement<T> get(long key);
    void dispose();
    int getHitCount();
    int getMissCount();
    int getMaxElements();
    int getСurrElements();
}
