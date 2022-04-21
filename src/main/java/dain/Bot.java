package dain;

import dain.events.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

import javax.security.auth.login.*;
import java.util.*;

public class Bot {

    private JDA jda;
    private TokenEntryFrame tokenEntryFrame = null;

    public Bot() {

        Settings.initialize();

        while (jda == null) {
            jda = loginJDAClient();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        jda.addEventListener(new DiscordMessageReceiver());

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Settings.useGui) new DiscordBotFrame(); // creates gui
    }

    public void startChatBridge() {

        TextChannel [] channels = new TextChannel[Settings.DISCORD_CHANNEL_IDS.length];
        for(int i = 0; i < Settings.DISCORD_CHANNEL_IDS.length; i++) {
            channels[i] = jda.getTextChannelById(Settings.DISCORD_CHANNEL_IDS[i]);
            Logger.info("Chat Bridge active.");
            channels[i].sendMessage("Chat bridge active.").queue();
        }

        /*Thread myThread = new Thread(new MinecraftChatBridge(this, 0));
        myThread.setDaemon(true);
        myThread.start();*/

        /*Thread myThread2 = new Thread(new MinecraftChatBridge(this, 1));
        myThread2.setDaemon(true);
        myThread2.start();*/

        Thread [] threads = new Thread[Settings.SERVER_NAMES.length];
        for (int i = 0; i < Settings.SERVER_NAMES.length; i++) {
            //channels[0].sendMessage("Initializing thread " + i).queue();
            threads[i] = new Thread(new MinecraftChatBridge(this, i));
            threads[i].setDaemon(true);
            threads[i].start();
            //channels[0].sendMessage("Initialized thread " + i).queue();
        }

        //MinecraftChatBridge chatBridge = new MinecraftChatBridge(this, 0);
        //chatBridge.run();

        //new MinecraftChatBridge(this, 1).run();

        Scanner in = new Scanner(System.in);

        String word = "";
        while(!(word.toLowerCase(Locale.ROOT).contains("stop"))) {
            word = in.nextLine();
        }

        in.close();

        System.exit(0);
    }

    public JDA getJda() {
        return jda;
    }

    private JDA loginJDAClient() {
        try {
            return JDABuilder.createDefault(Settings.DISCORD_TOKEN).setActivity(Activity.listening(Settings.COMMAND_PREFIX + "help")).build();
        } catch (LoginException e) {
            if (tokenEntryFrame == null) {
                tokenEntryFrame = new TokenEntryFrame();
            }
            return null;
        }
    }
}
