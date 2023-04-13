package org.example;

public class MyDependencyImpl implements MyDependency {
    private String someString ="";

    public MyDependencyImpl(String someString) {
        this.someString = someString;
    }

    public String getString(int number) {
        return this.someString;
    }

    public int getInt() {
        return 0;
    }
}