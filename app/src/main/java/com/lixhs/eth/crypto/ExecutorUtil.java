package com.lixhs.eth.crypto;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtil {
    public static Executor executor = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            executor.execute(runnable);
        }
    }
}