package com.ben.p_01_aidl_server;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @author 廖新平
 * @version 1.0.0
 * @decription TODO aidl通信，传递引用类型
 * @create 2023-03-24 15:43
 */
public class Person implements Parcelable {

    private String name;

    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    //TODO parcel相关
    //重写，不需要修改
    protected Person(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    //重写，不需要修改
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    //重写，不需要修改
    @Override
    public int describeContents() {
        return 0;
    }

    //重写，不需要修改
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    //读取顺序与写入顺序要一致
    public void readFromParcel(Parcel source) {
        source.readString();
        source.readInt();
    }
}
