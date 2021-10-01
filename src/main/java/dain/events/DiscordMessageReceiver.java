package dain.events;

import dain.CommandHandler;
import dain.MinecraftChatBridge;
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

        for (int id = 0; id < Settings.DISCORD_CHANNEL_IDS.length; id++) {
            if (event.getChannel() == event.getJDA().getTextChannelById(Settings.DISCORD_CHANNEL_IDS[id])) {
                MinecraftChatBridge.sendMessageToMc(event.getMessage());
            }
        }
        return;
    }
}
