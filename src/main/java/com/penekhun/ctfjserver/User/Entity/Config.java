package com.penekhun.ctfjserver.User.Entity;

import javax.persistence.*;

@Entity
@Table(name = "Config", schema = "ctf")
public class Config {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "`key`", nullable = false, length = 45)
    private String key;

    @Lob
    @Column(name = "value", nullable = false)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}