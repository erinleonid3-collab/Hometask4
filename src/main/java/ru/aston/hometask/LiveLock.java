package ru.aston.hometask;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {

    static final class Resource {
        private final String name;
        private final Lock lock = new ReentrantLock();
        private long usage;

        Resource(String name) {
            this.name = name;
        }

        Lock getLock() {
            return lock;
        }

        String getName() {
            return name;
        }

        long getUsage() {
            return usage;
        }

        void use() {
            usage++;
        }
    }

    private static final AtomicLong totalAttempts = new AtomicLong();
    private static final AtomicLong totalSuccesses = new AtomicLong();

    public static void livelockDemonstration()  {
        Resource r1 = new Resource("Resource1");
        Resource r2 = new Resource("Resource2");
        totalAttempts.set(0);
        totalSuccesses.set(0);

        // Барьер, чтобы потоки наверняка вошли в livelock и не разрешились сами
        CyclicBarrier barrier = new CyclicBarrier(2);

        Thread t1 = new Thread(() -> lockResources(r1, r2, barrier, "Thread1"), "Thread1");
        Thread t2 = new Thread(() -> lockResources(r2, r1, barrier, "Thread2"), "Thread2");

        t1.start();
        t2.start();
    }

    private static void lockResources(Resource first, Resource second,
                               CyclicBarrier barrier, String name) {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(name + " пытается захватить " + first.getName());
            first.getLock().lock();
            try {
                System.out.println(name + " захватил " + first.getName());
                if (!safeAwait(barrier)) return;
                System.out.println(name + " пытается захватить " + second.getName());
                if (second.getLock().tryLock()) {
                    try {
                        first.use();
                        second.use();
                        totalSuccesses.incrementAndGet();
                        barrier.reset();
                        return;
                    } finally {
                        System.out.println(name + " отпускает " + second.getName());
                        second.getLock().unlock();
                    }
                }
                System.out.println(name + " не удалось захватить " + second.getName());
                if (!safeAwait(barrier)) return;

            } finally {
                System.out.println(name + " отпускает " + first.getName());
                first.getLock().unlock();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if ("Thread1".equals(name)) {
                System.out.println("Всего попыток: " + totalAttempts.get());
                System.out.println("Успешных захватов: " + totalSuccesses.get());
            }
        }
    }

    private static boolean safeAwait(CyclicBarrier b) {
        try {
            b.await();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (BrokenBarrierException e) {
            return false;
        }
    }
}