package ru.otus.dataSets;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones;

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
        for (PhoneDataSet item : phones) {
            item.setUser(this);
        }
    }

    public void addPhone(PhoneDataSet phone) {
        if (phones == null) {
            phones = new ArrayList<PhoneDataSet>();
        }
        phones.add(phone);
        phone.setUser(this);
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public UserDataSet(long id, String name, int age, AddressDataSet address) {
        super(id);
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public UserDataSet(long id, String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
        super(id);
        this.name = name;
        this.age = age;
        this.address = address;
        setPhones(phones);
    }

    public UserDataSet() {
        super();

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataSet that = (UserDataSet) o;

        if (age != that.age) return false;
        if (id != that.id) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserDataSet{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phones);
        sb.append('}');
        return sb.toString();
    }
}
