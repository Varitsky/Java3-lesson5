import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    /////\\\\\\
    private CountDownLatch everyoneIsReady;
    private CountDownLatch everyoneFinished;
    private Semaphore ТЫНЕПРОЙДЁШЬ;
    private AtomicInteger ai; // if ai.get()==1 - WIN

    public void tunnelBlock() throws InterruptedException {
        this.ТЫНЕПРОЙДЁШЬ.acquire();
    }
    public void tunnelUnblock() {
        this.ТЫНЕПРОЙДЁШЬ.release();
    }
    /////////

    public Car(Race race, int speed, CountDownLatch everyoneIsReady, CountDownLatch everyoneFinished,
               Semaphore ТЫНЕПРОЙДЁШЬ, AtomicInteger ai) {
        this.race = race;
        this.speed = speed;
        this.everyoneIsReady = everyoneIsReady;
        this.everyoneFinished = everyoneFinished;
        this.ТЫНЕПРОЙДЁШЬ = ТЫНЕПРОЙДЁШЬ;
        this.ai = ai;

        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");

            ///\\\
            everyoneIsReady.countDown();
            everyoneIsReady.await();

            /** Проблема с "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!".
             *
             *  надпись "Гонка началась" иногда появляется после того как поедет первый участник.
             *
             *            Thread.currentThread().setPriority(Thread.MAX_PRIORITY); в Main не помогает
             *
             *  Потому введена переменна isStart и вот эти следующие две строки.
             * */
            while (!Main.isStart){
            }
            //////

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        try {
            everyoneFinished.countDown();
            ai.incrementAndGet();
            if (ai.get() == 1) {
                System.out.println(this.name + " - WIN");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.interrupted();
        }
    }
}
