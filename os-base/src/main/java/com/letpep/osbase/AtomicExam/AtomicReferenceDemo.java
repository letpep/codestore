package com.letpep.osbase.AtomicExam;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Copyright (C), 2020-04-06
 * FileName: AtomicReferenceDemo
 * Author:   lx
 * Date:     2020/4/6 11:44 AM
 * Description: demo
 */
@Data
@AllArgsConstructor
class Person{
    private  String name;
    private  Integer age;

}

public class AtomicReferenceDemo {
    public static void main(String[] args) {
        AtomicReference<Person> atomicReferenceDemo = new AtomicReference<Person>();
        Person p = new Person("z3",20);
        Person p1 = new Person("l4",21);
        atomicReferenceDemo.set(p);
        atomicReferenceDemo.compareAndSet(p,p1);
        System.out.println(atomicReferenceDemo.get());

    }



}
