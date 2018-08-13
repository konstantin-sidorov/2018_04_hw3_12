package ru.otus.cache;

import ru.otus.dataSets.DataSet;

public class CacheElement<T extends DataSet> {
    private T obj;
    private final long creationTime;
    private long lastAccessTime;

    public CacheElement(T obj, long creationTime, long lastAccessTime) {
        this.obj = obj;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
    }

    public CacheElement(T obj) {
        this.obj = obj;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public long getKey() {
        return obj.getId();
    }

    public T getValue() {
        return obj;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    public T getObj() {
        return obj;
    }
}
