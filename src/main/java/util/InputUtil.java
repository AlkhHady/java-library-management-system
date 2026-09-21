package util;

import java.util.Scanner;

public class InputUtil {

    public static String input(String desc) {
        Scanner sc = new Scanner(System.in);

        System.out.print(desc + " : ");
        var result = sc.nextLine();

        return  result;
    }
}
