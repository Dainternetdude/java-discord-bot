package dain;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.ConnectException;
import java.util.Random;
import java.util.Scanner;

public class MinecraftChatBridge implements Runnable {

    private final TextChannel [] channels = new TextChannel[Settings.DISCORD_CHANNEL_IDS.length];
    private String [] lastLines = { "", "" };
    private String [] previousLines = { "", "" };
    private final JDA jda;
    private int id;

    private int requestID = new Random().nextInt(Integer.MAX_VALUE);

    public MinecraftChatBridge(Bot botIn, int idIn) {
        jda = botIn.getJda();
        id = idIn;
        for(int i = 0; i < Settings.DISCORD_CHANNEL_IDS.length; i++) {
            channels[i] = jda.getTextChannelById(Settings.DISCORD_CHANNEL_IDS[i]);
        }
    }

    @Override
    public void run() {
        while(true) {
            sendNewMessages();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshLogFile() {

        int port = 21;
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(Settings.SERVER_IPS[id], port);
            ftpClient.login(Settings.FTP_USERNAMES[id], Settings.FTP_PASSWORDS[id]);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //sendMessageAllChannels("Getting file on thread " + id);

            String logFile = "/logs/latest.log";
            File downloadFile1 = new File(id + "latest.log");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(logFile, outputStream1);
            outputStream1.close();

            /*
            if (success) {
                sendMessageAllChannels("`latest.log` has been downloaded successfully on thread " + id);
            } else {
                sendMessageAllChannels("issue downloading file on thread " + id);
            }
             */

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNewMessages() {
        //sendMessageAllChannels("thread " + id + " sending new messages");

        refreshLogFile();
        try {
            File file = new File(id + "latest.log");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                lastLines[id] = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Where is latest.log?");
            e.printStackTrace();
        }
        if (lastLines[id].equals(previousLines[id])) return;
        previousLines[id] = lastLines[id];

        //sendMessageAllChannels(lastLines[id]);

        // only grabs stuff after the "[Server thread/INFO]: "
        try {
            lastLines[id] = lastLines[id].split("\\[Server thread/INFO]: ")[1];
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        lastLines[id] = lastLines[id].replaceAll("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", "[IP ADDRESS REDACTED]");
        // ^above replaces ipv4 IP addresses with "[IP ADDRESS REDACTED]"

        //break the message up into an array of strings for each word
        String[] lastLineArray = lastLines[id].split(" ");

        //change @ into mentions
        /*
        for (int i = 0; i < lastLineArray.length; i++) {
            if (lastLineArray[i].startsWith("@")) {
                String name = lastLineArray[i].substring(1);
                User user = jda.getUsersByName(name, true).get(0); //second parameter "true" means ignore case
                lastLineArray[i] = user.getAsMention();
            }
        }*/

        // make the <name> part of chat messages bold
        boolean isChatMessage;
        if (lastLineArray[0].startsWith("<") && lastLineArray[0].endsWith(">")) {
            lastLineArray[0] = "**" + lastLineArray[0] + "**";
            isChatMessage = true;
        } else isChatMessage = false;

        //join the array of strings back up to a complete message
        lastLines[id] = lastLineArray[0];
        for (int i = 1; i < lastLineArray.length; i++) {
            lastLines[id] = lastLines[id] + " " + lastLineArray[i];
        }

        //make non-chat messages bold
        if (!isChatMessage) {
            lastLines[id] = "**" + lastLines[id] + "**";
        }

        //say if its coming from smp or cmp
        lastLines[id] = "[" + Settings.SERVER_NAMES[id] + "] " + lastLines[id];

        //scoreboard junk

        sendMessageAllChannels(lastLines[id]);
    }

    private void sendMessageAllChannels(String message) {
        for (int i = 0; i < Settings.DISCORD_CHANNEL_IDS.length; i++) {
            channels[i].sendMessage(message).queue();
        }
    }
}
