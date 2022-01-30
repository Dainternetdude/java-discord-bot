package dain;

public class Start {

    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("nogui")) Settings.useGui = false;
            if (arg.equalsIgnoreCase("local")) Settings.isLocal = true;
        }

        new Bot().startChatBridge();
    }
}