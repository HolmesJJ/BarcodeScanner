package com.example.barcodescanner.thread;

import java.util.concurrent.ThreadFactory;

public class BSThreadFactory implements java.util.concurrent.ThreadFactory {
    private int mPriority = 5;
    private String mName = "BS_";

    public BSThreadFactory() {
    }

    public BSThreadFactory(int priority, String name) {
        this.mPriority = priority;
        this.mName = name;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.mName + System.currentTimeMillis());
        t.setPriority(this.mPriority);
        return t;
    }
}
