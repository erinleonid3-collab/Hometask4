package ru.aston.hometask;


import java.util.Scanner;

public class Main {
    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Введите демонстрацию:");
            System.out.println("1 — TwoStreams (чередование 1 и 2)");
            System.out.println("2 — LiveLock (демонстрация)");
            System.out.println("3 — DeadLock (демонстрация)");
            System.out.println("0 — выход");
            System.out.println("При выборе демонстраций программу можно будет остановить только принудительно");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Ошибка ввода — введите число 0–4");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            switch (choice){
                case 1-> {
                    TwoStreams.startTwoStreams();
                    return;
                }
                case 2-> {
                    LiveLock.livelockDemonstration();
                    return;
                }
                case 3-> {
                    DeadLock.deadLockDemonstration();
                    return;
                }
                case 0 -> {
                    System.out.println("Выход");
                    return;
                }
                default -> System.out.println("Неверный выбор: " + choice);
            }
        }
    }
}