package org.atanu.multithreading.volatilee;

//Volatile is used to solve visibility problem
//Each core caches variable and modifies them in local cache
//In case of volatile all the CPU will read/write to a shared cache , so no visibility problem
//Another way to overcome this problem is using synchronization

//we would expect that threadA would exit the while loop once threadB sets the variable flag to true
//but threadA may unfortunately find itself spinning forever if it has cached the variable flag's value.
//In this scenario, marking flag as volatile will fix the problem as it would ensure to read the value always from Memory
//Note that volatile presents a consistent view of the memory to all the threads

//https://www.educative.io/courses/java-multithreading-for-senior-engineering-interviews/gxkEvN1L1xZ

public class VolatileExample {
    boolean flag = false;

    void threadA() {

        while (!flag) {

            // ... Do something useful
        }

    }

    void threadB() {
        flag = true;
    }
}
