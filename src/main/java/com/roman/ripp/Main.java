package com.roman.ripp;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You have to provide path to codecollaborator credentials file.");
            return;
        }

        var googleAppCredentialsEnvVar = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (googleAppCredentialsEnvVar == null)
        {
            System.out.println("You need to set GOOGLE_APPLICATION_CREDENTIALS environment variable." +
                    " It needs to point to the credentials file. Example: " +
                    "export GOOGLE_APPLICATION_CREDENTIALS=/Users/AdolfHitler/collabominator/res/gsecret.json");
            return;
        }

        var ccbCredentialsPath = args[0];
        var reviewObserver = new ReviewObserver(ccbCredentialsPath);
        reviewObserver.RunOnce();

        System.exit(0);
    }
}
