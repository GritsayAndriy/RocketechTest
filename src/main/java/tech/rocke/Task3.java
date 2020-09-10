package tech.rocke;

import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Task3 {

    public static void main(String[] args) {
        IncrementSynchronize increment = new IncrementSynchronize();
        ReentrantLock locker = new ReentrantLock();
        Semaphore semaphore = new Semaphore(1);
        for (int i = 1; i < 10; i++) {
            Thread thread = new Thread(increment);
//            Thread thread = new Thread(new BlockSynchronizeDecoration(increment));
//            Thread thread = new Thread(new SemaphoreSynchronizeDecoration(increment, semaphore));
//            Thread thread = new Thread(new LockSynchronizeDecoration(increment, locker));
            thread.setName("Thread " + i);
            thread.start();
        }
    }
}

class IncrementSynchronize implements Runnable {
    private int value = 0;

    public int getNextValue() {
        return ++value;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": " + getNextValue());
    }
}

class BlockSynchronizeDecoration extends IncrementSynchronize {
    private IncrementSynchronize increment;

    public BlockSynchronizeDecoration(IncrementSynchronize increment) {
        this.increment = increment;
    }

    @Override
    public int getNextValue() {
        synchronized (increment) {
            return increment.getNextValue();
        }
    }
}

class SemaphoreSynchronizeDecoration extends IncrementSynchronize {
    private IncrementSynchronize increment;
    private Semaphore semaphore;

    public SemaphoreSynchronizeDecoration(IncrementSynchronize increment, Semaphore semaphore) {
        this.increment = increment;
        this.semaphore = semaphore;
    }

    @Override
    public int getNextValue() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int value = increment.getNextValue();
        semaphore.release();
        return value;
    }
}

class LockSynchronizeDecoration extends IncrementSynchronize {
    private IncrementSynchronize increment;
    private ReentrantLock locker;

    public LockSynchronizeDecoration(IncrementSynchronize increment, ReentrantLock locker) {
        this.increment = increment;
        this.locker = locker;
    }

    @Override
    public int getNextValue() {
        this.locker.lock();
        int value = increment.getNextValue();
        this.locker.unlock();
        return value;
    }
}