package com.penekhun.ctfjserver.User.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Config")
public class Config {
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