package dain.events;

import dain.CommandHandler;
import dain.Logger;
import dain.MinecraftChatBridge;
import dain.Settings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getAuthor().equals(event.getJDA().getSelfUser())) return;

        String messageSent = event.getMessage().getContentRaw();
        String[] messageSentCharArray = messageSent.split("");
        String[] messageSentWordArray = messageSent.split(" ");

        if(messageSentCharArray[0].equals(Settings.COMMAND_PREFIX) && messageSentWordArray[0].length() > 1) {

            String commandName = messageSentWordArray[0].substring(1).toLowerCase();

            String[] parameters = new String[messageSentWordArray.length - 1];
            for (int i = 1; i < messageSentWordArray.length; i++) {
                parameters[i - 1] = messageSentWordArray[i];
            }

            CommandHandler.handleDiscordCommand(commandName, parameters, event); //todo remove event
        }

        for (int id = 0; id < Settings.DISCORD_CHANNEL_IDS.length; id++) { // for all Discord channels ids in config
            if (event.getChannel() == event.getJDA().getTextChannelById(Settings.DISCORD_CHANNEL_IDS[id])) { // if the channel id from the config matches one of the channel id that the message was sent from //todo need to optimize this
                Logger.log("Message received from Discord: " + messageSent, Logger.LoggingLevel.INFO);
                MinecraftChatBridge.sendMessageToMc(event.getMessage()); // send message to mc
            }
        }
        return;
    }
}
