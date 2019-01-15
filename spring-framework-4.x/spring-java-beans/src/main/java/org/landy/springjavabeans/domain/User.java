package org.landy.springjavabeans.domain;

import java.util.Date;

/**
 * Created by Landy on 2019/1/8.
 */
public class User {
    private int id;
    private String name;
    /**
     * Date
     */
    private Date date;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
