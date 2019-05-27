package com.example.diuteacher.PoJo;

import java.io.Serializable;

public class StudentPoJo implements Serializable {
    String id;
    String name;

    public StudentPoJo() {
    }

    public StudentPoJo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
