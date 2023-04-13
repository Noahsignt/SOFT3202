package org.example;

public class MyClass {
    private MyDependency dependency;

    public MyClass(MyDependency dependency) {
        this.dependency = dependency;
    }

    public String doSomething() {
        if(this.dependency.getString(0).equalsIgnoreCase("true")){
            return "ran it";
        }
        else{
            return "not";
        }
    }
}
