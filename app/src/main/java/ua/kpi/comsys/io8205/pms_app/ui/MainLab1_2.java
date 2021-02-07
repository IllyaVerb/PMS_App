package ua.kpi.comsys.io8205.pms_app.ui;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MainLab1_2 {
    public static void main(String[] args) {
        first_part();
    }

    @SuppressLint("DefaultLocale")
    private static void first_part(){

        // Частина 1

        // Дано рядок у форматі "Student1 - Group1; Student2 - Group2; ..."

        String studentsStr = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83; Аверкова Анастасія - ІО-83; Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; Лихацька Юлія- ІВ-82; Головенець Руслан - ІВ-83; Ющенко Андрій - ІО-82; Вербовський Ілля - ІО-82; Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; Снігурець Олег - ІВ-81; Роман Олександр - ІО-82; Дудка Максим - ІО-81; Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; Іванов Володимир - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83; Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82";

        // Завдання 1
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – відсортований масив студентів, які відносяться до відповідної групи

        HashMap<String, ArrayList<String>> studentsGroups = new HashMap<>();

        // Ваш код починається тут

        for (String st_gr :
                studentsStr.split("; ?")) {
            String[] st_gr_arr = st_gr.split(" ?- ");

            if (!studentsGroups.containsKey(st_gr_arr[1]))
                studentsGroups.put(st_gr_arr[1], new ArrayList<>());
            studentsGroups.get(st_gr_arr[1]).add(st_gr_arr[0]);
        }

        for (String group :
                studentsGroups.keySet()) {
            Collections.sort(studentsGroups.get(group), String.CASE_INSENSITIVE_ORDER);
        }

        // Ваш код закінчується тут

        System.out.println("Завдання 1");
        for (String key :
                studentsGroups.keySet()) {
            System.out.println(key);
            for (String student :
                    studentsGroups.get(key)) {
                System.out.println("\t"+student);
            }
            System.out.println();
        }

        // Дано масив з максимально можливими оцінками

        int[] points = {12, 12, 12, 12, 12, 12, 12, 16};

        // Завдання 2
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – словник, де:
        //   - ключ – студент, який відносяться до відповідної групи
        //   - значення – масив з оцінками студента (заповніть масив випадковими значеннями, використовуючи функцію `randomValue(maxValue: Int) -> Int`)

        HashMap<String, HashMap<String, ArrayList<Integer>>> studentPoints = new HashMap<>();

        // Ваш код починається тут

        for (String group:
                studentsGroups.keySet()){

            if (!studentPoints.containsKey(group))
                studentPoints.put(group, new HashMap<>());

            for (String student :
                    studentsGroups.get(group)) {
                studentPoints.get(group).put(student, new ArrayList<>());
                for (int point :
                        points) {
                    studentPoints.get(group).get(student).add(randomValue(point));
                }
            }
        }

        // Ваш код закінчується тут

        System.out.println("Завдання 2");
        for (String key :
                studentPoints.keySet()) {
            System.out.println(key);
            for (String student :
                    studentPoints.get(key).keySet()) {
                System.out.print("\t"+student+"\n\t\t");
                for (int p :
                        studentPoints.get(key).get(student)) {
                    System.out.print(p + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        // Завдання 3
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – словник, де:
        //   - ключ – студент, який відносяться до відповідної групи
        //   - значення – сума оцінок студента

        HashMap<String, HashMap<String, Integer>> sumPoints = new HashMap<>();

        // Ваш код починається тут

        for (String group:
                studentsGroups.keySet()){

            if (!sumPoints.containsKey(group))
                sumPoints.put(group, new HashMap<>());

            for (String student :
                    studentsGroups.get(group)) {
                int sum = 0;
                for (int point :
                        studentPoints.get(group).get(student)) {
                    sum += point;
                }

                sumPoints.get(group).put(student, sum);
            }
        }

        // Ваш код закінчується тут

        System.out.println("Завдання 3");
        for (String key :
                sumPoints.keySet()) {
            System.out.println(key);
            for (String student :
                    sumPoints.get(key).keySet()) {
                System.out.println(String.format("\t%s -- %d", student, sumPoints.get(key).get(student)));
            }
            System.out.println();
        }

        // Завдання 4
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – середня оцінка всіх студентів групи

        HashMap<String, Float> groupAvg = new HashMap<>();

        // Ваш код починається тут

        for (String group:
                studentsGroups.keySet()){
            int sum = 0, num = 0;
            for (String student :
                    sumPoints.get(group).keySet()) {
                num++;
                sum += sumPoints.get(group).get(student);
            }
            groupAvg.put(group, (float)sum/num);
        }

        // Ваш код закінчується тут

        System.out.println("Завдання 4");
        for (String key :
                groupAvg.keySet()) {
            System.out.println(String.format("%s -- %f", key, groupAvg.get(key)));
        }
        System.out.println();

        // Завдання 5
        // Заповніть словник, де:
        // - ключ – назва групи
        // - значення – масив студентів, які мають >= 60 балів

        HashMap<String, ArrayList<String>> passedPerGroup = new HashMap<>();

        // Ваш код починається тут

        for (String group:
                studentsGroups.keySet()){

            if (!passedPerGroup.containsKey(group))
                passedPerGroup.put(group, new ArrayList<>());

            for (String student :
                    studentsGroups.get(group)) {
                if (sumPoints.get(group).get(student) >= 60){
                    passedPerGroup.get(group).add(student);
                }
            }
        }

        // Ваш код закінчується тут

        System.out.println("Завдання 5");
        for (String key :
                passedPerGroup.keySet()) {
            System.out.println(key);
            for (String student :
                    passedPerGroup.get(key)) {
                System.out.println("\t"+student);
            }
            System.out.println();
        }

    }

    private static int randomValue(int maxValue){
        Random rand = new Random();
        switch(rand.nextInt(6)) {
            case 1:
                return (int) (maxValue * 0.7);
            case 2:
                return (int) (maxValue * 0.9);
            case 3:
            case 4:
            case 5:
                return maxValue;
            default:
                return 0;
        }
    }

    private static void second_part(){
        CoordinateIV a = new CoordinateIV();
        CoordinateIV b, c, d;

        {
            try {
                b = new CoordinateIV(60, 23, 14, Direction.LATITUDE);
                c = new CoordinateIV(-2, 12, 50, Direction.LATITUDE);
                d = new CoordinateIV(150, 55, 28, Direction.LONGITUDE);
                System.out.println();

                System.out.println("A: " + a.getIntCoordinate());
                System.out.println("B: " + b.getFloatCoordinate());
                System.out.println("C: " + c.getIntCoordinate());
                System.out.println("D: " + d.getFloatCoordinate());

                System.out.println();

                System.out.println("First mid method: " + a.getMiddleCoordinate(b, c).getIntCoordinate());
                System.out.println("Second mid method: " + b.getMiddleCoordinate(c).getIntCoordinate());
                System.out.println("Incorrect mid method: " + b.getMiddleCoordinate(d));

                System.out.println();

                CoordinateIV err = new CoordinateIV(190, 60, 60, Direction.LONGITUDE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
