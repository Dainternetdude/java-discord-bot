package dain;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.*;
import java.util.*;

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
                Logger.log("thread interrupted while sleeping", Logger.LoggingLevel.FATAL);
                System.exit(-1);
            }
        }
    }

    public void sendNewMessages() {
        //sendMessageAllChannels("thread " + id + " sending new messages");

        if (!Settings.isLocal) FTPHandler.grabFile("/logs/latest.log", id); // if its local dont do that

        try {
            File file = (Settings.isLocal ? new File("logs/latest.log") : new File(id + "latest.log")); // if its local grab from logs directory
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) { //todo change to detect new lines instead of most recent line
                lastLines[id] = myReader.nextLine();
            }

            myReader.close();
        } catch (FileNotFoundException e) {

            Logger.log("File not found. Where is latest.log?", Logger.LoggingLevel.ERROR);
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
        Logger.log("Message received from Minecraft: " + lastLines[id], Logger.LoggingLevel.INFO);

        String processedLine = processLine(lastLines[id]);

        sendMessageAllChannels(processedLine);

        Logger.log("Message sent to Discord: " + processedLine, Logger.LoggingLevel.INFO);
    }

    private void sendMessageAllChannels(String message) {
        for (int i = 0; i < Settings.DISCORD_CHANNEL_IDS.length; i++) {
            channels[i].sendMessage(message).queue();
        }
    }

    private String processLine(String line) {

        // replace ipv4 IP addresses with "[IP ADDRESS REDACTED]"
        line = line.replaceAll("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)", "[IP ADDRESS REDACTED]");

        //break the message up into an array of strings for each word
        String[] lineArray = line.split(" ");

        //todo change @ into mentions
        /*
        for (int i = 0; i < lastLineArray.length; i++) {
            if (lastLineArray[i].startsWith("@")) {
                String name = lastLineArray[i].substring(1);
                User user = jda.getUsersByName(name, true).get(0); //second parameter "true" means ignore case
                lastLineArray[i] = user.getAsMention();
            }
        }*/

        //todo change :emoji: to emojis

        // make the <name> part of chat messages bold
        boolean isChatMessage;
        String author = "";
        if (lineArray[0].startsWith("<") && lineArray[0].endsWith(">")) {
            lineArray[0] = "**" + lineArray[0] + "**";
            isChatMessage = true;
            author = lineArray[0].substring(1, lineArray[0].length() - 2);

            if (lineArray[1].startsWith(">")) {

                String[] parameterArray = new String[lineArray.length - 2];
                for (int i = 2; i < lineArray.length; i++) {
                    parameterArray[i - 2] = lineArray[i];
                }

                CommandHandler.handleMcCommand(lineArray[1].substring(1).toLowerCase(), parameterArray, id);
            }
        } else isChatMessage = false;

        //join the array of strings back up to a complete message
        line = lineArray[0];
        String message = "";
        for (int i = 1; i < lineArray.length; i++) {
            line = line + " " + lineArray[i];
            message = message + lineArray[i] + " ";
        }

        //make non-chat messages bold
        if (!isChatMessage) {
            message = line;
            line = "**" + line + "**";
        }

        //say if its coming from smp or cmp
        line = "[" + Settings.SERVER_NAMES[id] + "] " + line;

        if (isChatMessage) {
            sendMessageAllOtherServers(author, message);
        } else {
            sendMessageAllOtherServers(message);
        }

        return line;
    }

    private void sendMessageAllOtherServers(String author, String message) {
        for (int i = 0; i < Settings.SERVER_IPS.length; i++) {
            if (i != id) {
                sendMessageToServer(author, message, i);
            }
        }
    }

    private void sendMessageAllOtherServers(String message) {
        for (int i = 0; i < Settings.SERVER_IPS.length; i++) {
            if (i != id) {
                sendMessageToServer(message, i);
            }
        }
    }

    public static void sendMessageAllServers(String message) {
        for (int i = 0; i < Settings.SERVER_IPS.length; i++) {
            sendMessageToServer(message, i);
        }
    }

    public static void sendMessageToMc(Message message) {
        //TODO replace emojis with :emoji_name:

        //TODO replace attachments with text

        String command = generateCommand(message.getAuthor().getName(), message.getContentRaw());

        for (int id = 0; id < Settings.SERVER_IPS.length; id++) {
            sendCommandToServer(command, id);
        }
        Logger.log("Message sent to Minecraft: " + message, Logger.LoggingLevel.INFO);
    }

    public static void sendMessageToServer(String author, String message, int serverId) {

        String command = generateCommand(author, message);

        sendCommandToServer(command, serverId);
    }

    public static void sendMessageToServer(String message, int serverId) {

        String command = generateCommand(message);

        sendCommandToServer(command, serverId);
    }

    private static String generateCommand(String author, String message) {
        return "tellraw @a [\"\",{\"text\":\"<\"},{\"text\":\"@\",\"color\":\"gray\"}, {\"text\":\"" + author + "\",\"color\":\"white\"},{\"text\":\"> " + message + "\"}]";
    }

    private static String generateCommand(String message) {
        return "tellraw @a \"" + message + "\"";
    }

    public static void sendCommandToServer(String command, int serverId) {
        try {
            Rcon rcon = new Rcon(Settings.SERVER_IPS[serverId], Integer.decode(Settings.RCON_PORTS[serverId]), Settings.RCON_PASSWORDS[serverId].getBytes());
            rcon.command(command);
            rcon.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }


}
