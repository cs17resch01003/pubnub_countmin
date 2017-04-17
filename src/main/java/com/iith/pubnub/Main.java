package com.iith.pubnub;

import com.google.common.base.Joiner;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            PubNubImpl.start("sub-c-4377ab04-f100-11e3-bffd-02ee2ddab7fe",
                    "pubnub-market-orders", "symbol");
        });
        thread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            
            if ("listItems".equals(command)) {                
                String list = Joiner.on(", ").join(Counter.getItems());
                System.out.println(list);
            } else if (command.startsWith("exactCount")) {
                String item = command.replace("exactCount ", "");
                long count = Counter.getExact(item);
                System.out.println(count);
            } else if (command.startsWith("approxCount")) {
                String item = command.replace("approxCount ", "");
                long count = Counter.getExact(item);
                System.out.println(count);
            }
        }
    }
}