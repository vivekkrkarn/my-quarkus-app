package com.vivekkarn.kafkademo;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Message extends PanacheEntity {

    public String content;

    public Message() {}

    public Message(String content) {
        this.content = content;
    }
}
