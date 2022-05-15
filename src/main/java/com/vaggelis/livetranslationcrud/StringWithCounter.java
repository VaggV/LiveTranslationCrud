package com.vaggelis.livetranslationcrud;

public class StringWithCounter {
    private String value;
    private int counter;

    public StringWithCounter(String value, int counter) {
        this.value = value;
        this.counter = counter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return value + " *" + counter;
    }
}
