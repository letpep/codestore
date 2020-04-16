package com.letpep.osbase.casExam;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C), 2020-04-06
 * FileName: AutomicIntergerDemo
 * Author:   lx
 * Date:     2020/4/6 9:43 AM
 * Description:
 */
public class AutomicIntergerDemo {
    public static void main(String[] args) {
        AtomicInteger exam = new AtomicInteger(5);

        exam.getAndIncrement();
    }


}

