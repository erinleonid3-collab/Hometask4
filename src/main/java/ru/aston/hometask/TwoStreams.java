package ru.aston.hometask;

public class TwoStreams {
    private static final Object lock = new Object();
    private static boolean isFirstTurn = true;

    public static void startTwoStreams() {

        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        while (!isFirstTurn) {
                            lock.wait();
                        }
                        System.out.println("1");
                        isFirstTurn = false;
                        lock.notify();
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        while (isFirstTurn) {
                            lock.wait();
                        }
                        System.out.println("2");
                        isFirstTurn = true;
                        lock.notify();
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();
    }
}
