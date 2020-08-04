import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final int CARS_COUNT = 3;
    public static boolean isStart = false;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        ///
        final CountDownLatch everyoneIsReady = new CountDownLatch(CARS_COUNT);
        final CountDownLatch everyoneFinished = new CountDownLatch(CARS_COUNT);
        Semaphore ТЫНЕПРОЙДЁШЬ = new Semaphore(2); // Сколько машин влезает в туннель.
        AtomicInteger ai = new AtomicInteger(0); // Проверка победы.
        ///

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), everyoneIsReady, everyoneFinished,
                    ТЫНЕПРОЙДЁШЬ, ai);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        ///
        try {
            everyoneIsReady.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            isStart = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            everyoneFinished.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.interrupted();
        ///

    }
}
