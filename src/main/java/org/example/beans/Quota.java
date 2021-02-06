package org.example.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Author sushanghai
 * @Date 2021/2/3
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quota {

    private String type;
    private Value value;

    public Quota() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" + "type=‘" + type + "\'" + ",value=" + value + "}";
    }

}
