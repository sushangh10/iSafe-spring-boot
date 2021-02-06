package org.example.beans;

import java.util.logging.Logger;

/**
 * @Author sushanghai
 * @Date 2021/2/3
 */
public class Value {

    public Value() {
    }

    private Long id;

    private String quote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" + "id=" + id + ", quote='" + quote + '\'' + '}';
    }

}
