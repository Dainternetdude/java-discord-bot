package dain.events;

import dain.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

import java.io.IOException;

public class DiscordMessageReceiver extends ListenerAdapter {

    private enum Commands { // todo maybe better as an enum?
            KILL("kill"),
            SCORE("score"),
            HELP("help");

            private String value;

        private Commands(String valueIn) {
            this.value = valueIn;
        }
    };

    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getAuthor().equals(event.getJDA().getSelfUser())) return;

        String messageSent = event.getMessage().getContentRaw();
        String[] messageSentCharArray = messageSent.split("");
        String[] messageSentWordArray = messageSent.split(" ");

        if(messageSentCharArray[0].equals(Settings.COMMAND_PREFIX)) { //todo fix quotes activating commands

            String commandName = messageSentWordArray[0].substring(1).toLowerCase();

            switch (commandName) {
                case "kill": // kill //todo remove
                    runKill(event);
                    break;
                case "score": // score
                    runScore(event);
                    break;
                case "help": // help
                    runHelp(event);
                    break;
                default:
                    break;
            }
        }

        for (int id = 0; id < Settings.DISCORD_CHANNEL_IDS.length; id++) {
            if (event.getChannel() == event.getJDA().getTextChannelById(Settings.DISCORD_CHANNEL_IDS[id])) {
                sendMessageToMc(event.getMessage());
            }
        }
        return;
    }

    private void runKill(MessageReceivedEvent event) {
        String messageAuthor = event.getAuthor().getName();
        event.getChannel().sendMessage("*" + messageAuthor + " went*").queue();
    }

    private void runScore(MessageReceivedEvent event) { //TODO
        //event.getChannel().sendMessage("soon:tm:").queue();

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

    private void runHelp(MessageReceivedEvent event) {
        event.getChannel().sendMessage("There are approximately " + Commands.values().length + " commands.\n" +
                "`" + Settings.COMMAND_PREFIX + "help" + "` = displays this message\n" +
                "`" + Settings.COMMAND_PREFIX + "kill" + "` = kills the who sent the message").queue();
    }

    private void sendMessageToMc(Message message) {
        //TODO replace emojis with :emoji_name:

        //TODO replace attachments with text

        String command = "tellraw @a [\"\",{\"text\":\"<\"},{\"text\":\"@\",\"color\":\"gray\"}, {\"text\":\"" + message.getAuthor().getName() + "\",\"color\":\"white\"},{\"text\":\"> " + message.getContentRaw() + "\"}]";

        for (int id = 0; id < Settings.SERVER_IPS.length; id++) {
            try {
                Rcon rcon = new Rcon(Settings.SERVER_IPS[id], Integer.decode(Settings.RCON_PORTS[id]), Settings.RCON_PASSWORDS[id].getBytes());
                rcon.command(command);
                rcon.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }
    }
}
