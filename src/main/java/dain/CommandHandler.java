package dain;

import dain.events.DiscordMessageReceiver;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler {

    public static void handleDiscordCommand(String commandName, String[] parameters, MessageReceivedEvent event) {

        switch (commandName) {
            case "help":
            case "h":
                runDiscordHelp(event);
                break;
            case "score":
            case "s":
                runDiscordScore(event);
                break;
            case "kill":
            case "k":
                runDiscordKill(event);
                break;
            default:
                event.getChannel().sendMessage("Unknown command. Try \"" + Settings.COMMAND_PREFIX + "help\" for options.").queue();
                break;
        }
    }

    public static void handleMcCommand(String commandName, String[] parameters, int serverId) {



        switch (commandName) {
            case "score":
            case "s":
                runMcScore(parameters, serverId);
        }
    }

    private static void runMcScore(String[] parameters, int serverId) {
        if (parameters.length == 1) {
            if (parameters[0].equals("clear")) {
                MinecraftChatBridge.sendCommandToServer("scoreboard objectives setDisplay sidebar", serverId);
            } else {
                MinecraftChatBridge.sendCommandToServer("scoreboard objectives setDisplay sidebar " + parameters[0], serverId);
            }
        } else if (parameters.length == 0) {
            MinecraftChatBridge.sendCommandToServer("scoreboard objectives setDisplay sidebar", serverId);
        } else {
            MinecraftChatBridge.sendMessageAllServers("prodlem,,. Please provide a command in the form: " + Settings.COMMAND_PREFIX + "s [scoreboard name]");
        }
    }

    private static void runDiscordKill(MessageReceivedEvent event) {
        String messageAuthor = event.getAuthor().getName();
        event.getChannel().sendMessage("*" + messageAuthor + " went*").queue();
    }

    private static void runDiscordScore(MessageReceivedEvent event) { //TODO
        event.getChannel().sendMessage("soon:tm:").queue();

        /*
        int port = 21;

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(Settings.SERVER_IP, port);
            ftpClient.login(Settings.FTP_USERNAME, Settings.FTP_PASSWORD);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String logFile = "/logs/latest.log";
            File downloadFile1 = new File("latest.log");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(logFile, outputStream1);
            outputStream1.close();

            event.getChannel().sendMessage("Getting file...").queue();

            if (success) {
                event.getChannel().sendMessage("File #1 has been downloaded successfully.").queue();
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
         */
    }

    private static void runDiscordHelp(MessageReceivedEvent event) {
        event.getChannel().sendMessage("There are approximately 3 commands.\n" +  //+ DiscordMessageReceiver.Commands.values().length + " commands.\n" +
                "`" + Settings.COMMAND_PREFIX + "help" + "` = displays this message\n" +
                "`" + Settings.COMMAND_PREFIX + "kill" + "` = kills the who sent the message").queue();
    }
}