package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class MyClassTest {
    @Test
    public void doSomethingTest() {
        MyDependency dependency = mock(MyDependency.class);

        MyClass myClass = new MyClass(dependency);

//        when(dependency.getString(0)).thenReturn("true");

        String result = myClass.doSomething();
        verify(dependency).getString(ArgumentMatchers.anyInt());
    }
}
