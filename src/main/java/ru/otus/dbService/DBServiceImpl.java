package ru.otus.dbService;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.cache.CacheElement;
import ru.otus.cache.CacheEngine;
import ru.otus.dataBase.DBConnection;
import ru.otus.dataSets.DataSet;


import java.util.List;
import java.util.function.Function;

public class DBServiceImpl implements DBService {
    private final DBConnection factory;
    private final CacheEngine cache;

    public DBServiceImpl(CacheEngine cache) {
        this.factory = new DBConnection();
        this.cache = cache;
    }

    public SessionFactory getConnection() {
        return this.factory.getFactory();
    }

    @Override
    public String getLocalStatus() {
        return null;
    }

    @Override
    public <T extends DataSet> void save(T dataSet) {
        try (Session session = getConnection().openSession()) {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            CacheElement<T> elem = new CacheElement<>(dataSet);
            cache.put(elem);
        }
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = getConnection().openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> T read(long id, Class<T> clazz) {
        CacheElement<T> cacheElem = (CacheElement<T>) this.cache.get(id);
        if (cacheElem != null) {
            return cacheElem.getObj();
        }
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.read(id, clazz);
        });
    }

    @Override
    public <T extends DataSet> T readByName(String name, Class<T> clazz) {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readByName(name, clazz);
        });
    }

    @Override
    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll(clazz);
        });
    }

    @Override
    public <T extends DataSet> void addUsers(List<T> users) {
        try (Session session = getConnection().openSession()) {
            for (DataSet user : users) {
                //session.save(user);
                UserDataSetDAO dao = new UserDataSetDAO(session);
                dao.save(user);
                CacheElement<T> elem = new CacheElement<>((T) user);
                cache.put(elem);
            }
        }
    }

    @Override
    public <T extends DataSet> T loadUser(long id, Class<T> clazz) {
        CacheElement<T> cacheElem = (CacheElement<T>) this.cache.get(id);
        if (cacheElem != null) {
            return cacheElem.getObj();
        }
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.read(id, clazz);
        });
    }

    @Override
    public void shutdown() {
        this.factory.close();
    }

    @Override
    public void close() throws Exception {
        shutdown();
        cache.dispose();
    }
}
