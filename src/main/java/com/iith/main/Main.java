package com.iith.main;

import com.iith.streams.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        NumericStream stream = new NumericStream(1000); //100000
        // StockPubNub stream = new StockPubNub();
        
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
}