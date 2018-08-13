package ru.otus.dataSets;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    public DataSet(long id) {
        this.id = id;
    }
    public DataSet() {

    }
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
