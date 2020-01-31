package com.roman.ripp;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You have to provide path to credentials file.");
            return;
        }

        var credentialsPath = args[0];
        var reviewObserver = new ReviewObserver(credentialsPath);
        reviewObserver.RunOnce();

        System.exit(0);
    }
}
