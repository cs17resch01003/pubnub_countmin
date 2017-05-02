package com.iith.main;

import com.iith.streams.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        bulkHashs();
    }
    
    private static void interactive() {
        NumericStream stream = new NumericStream(100000, 10, 10);
        
        Thread thread = new Thread(() -> {
            stream.start();
        });
        thread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            
            if ("listItems".equals(command)) {                
                String list = stream.getItems();
                System.out.println(list);
            } else if (command.startsWith("exactCount")) {
                String item = command.replace("exactCount ", "");
                long count = stream.getCounter(item);
                System.out.println(count);
            } else if (command.startsWith("approxCount")) {
                String item = command.replace("approxCount ", "");
                long count = stream.getCountMin(item);
                System.out.println(count);
            } else if (command.startsWith("pause")) {
                stream.setPaused(true);
            } else if (command.startsWith("unpause")) {
                stream.setPaused(false);
            } else if (command.startsWith("size")) {
                long count = stream.getSize();
                System.out.println(count);
            }
        }
    }
    
    private static void bulkThresholds() {
        double[] epsilons = new double[] { 0.0001, 0.001, 0.005, 0.01, 0.05, 0.1 };
        double[] confidences = new double[] { 0.5, 0.75, 0.99, 0.999, 0.9999, 0.99999, 0.999999, 0.9999999 };
        long[] elements = new long[] { 100, 1000, 10000, 100000 };
        
        for(long element : elements) {
            for (double epsilon : epsilons) {
                for (double confidence : confidences) {
                    NumericStream stream = new NumericStream(element, epsilon, confidence);

                    Thread thread = new Thread(() -> {
                        stream.start();
                    });
                    thread.start();
                }
            }
        }
    }
    
    private static void bulkHashs() {
        int[] widths = new int[] { 10, 20, 50, 100, 250, 500, 1000, 2500, 5000, 10000 };
        int[] depths = new int[] { 1, 2, 5, 10, 12, 15, 18, 20, 25 };
        long[] elements = new long[] { 100, 1000, 10000, 100000 };
        
        for(long element : elements) {
            for (int width : widths) {
                for (int depth : depths) {
                    NumericStream stream = new NumericStream(element, width, depth);

                    Thread thread = new Thread(() -> {
                        stream.start();
                    });
                    thread.start();
                }
            }
        }
    }
}