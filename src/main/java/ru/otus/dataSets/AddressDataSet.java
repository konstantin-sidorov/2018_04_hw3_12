package ru.otus.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {
    @Column(name = "street")
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AddressDataSet{");
        sb.append("street='").append(street).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
