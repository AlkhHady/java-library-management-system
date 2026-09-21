package util;

public class DelayUtil {

    public static void delay(Long delayL) throws InterruptedException{
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            Thread.sleep(delayL);
        }
        Thread.sleep(delayL);
        System.out.println();
    }
}
