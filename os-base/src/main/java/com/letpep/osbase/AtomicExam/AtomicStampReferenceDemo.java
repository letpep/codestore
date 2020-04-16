package com.letpep.osbase.AtomicExam;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Copyright (C), 2020-04-06
 * FileName: AtomicStampReferenceDemo
 * Author:   lx
 * Date:     2020/4/6 12:22 PM
 * Description: demo
 */
public class AtomicStampReferenceDemo {
   static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<Integer>(100,1);

    public static void main(String[] args) {
        System.out.println(atomicStampedReference.compareAndSet(100,101,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1));
    }
}
