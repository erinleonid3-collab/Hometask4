package ru.aston.hometask;

public class DeadLock {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void deadLockDemonstration() {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Поток " + Thread.currentThread().getName() + " захватил монитор lock1");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("Поток " + Thread.currentThread().getName() + " держит монитор lock1 и ждет освобождения монитора lock2");
                synchronized (lock2) {
                    //Поток держит монитор lock2 и ждет освобождения понитора lock2
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Поток " + Thread.currentThread().getName() + " захватил монитор lock2");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("Поток " + Thread.currentThread().getName() + " держит монитор lock2 и ждет освобождения монитора lock2");
                synchronized (lock1) {

                    //Поток держит монитор lock2 и ждет освобождения понитора lock2
                }
            }
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток был прерван во время ожидания");
            Thread.currentThread().interrupt();
        }
    }
}
