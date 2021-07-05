package dain;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.channels.SeekableByteChannel;
import java.util.Scanner;

public class Settings { //todo read settings from external text file and
                        //todo upload to github

    public static String COMMAND_PREFIX;

    public static String [] SERVER_NAMES;
    public static String [] SERVER_IPS;
    public static int [] RCON_PORTS;
    public static String [] RCON_PASSWORDS;

    public static String [] FTP_USERNAMES;
    public static String [] FTP_PASSWORDS;

    public static String DISCORD_TOKEN;
    public static String [] DISCORD_CHANNEL_IDS; //{ "412344354980495370", "805864136121516063" };//803345769972760599
    public static String [] DISCORD_CHANNEL_NAMES;

    public static void initialize() {

        String line;

        try {
            File file = new File("settings.txt");
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) {
                line = myReader.nextLine();

                processLine(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Where is settings.txt?");
            e.printStackTrace();
        }
    }

    private static void processLine(String line) {

        if (line.length() == 0) return;

        String[] lineArray = line.split(" ");
        switch (lineArray[0].split(":")[0]) {
            case "command_prefix":

                COMMAND_PREFIX = lineArray[1];

                break;
            case "server_names":

                SERVER_NAMES = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, SERVER_NAMES, 0, lineArray.length - 1);

                break;
            case "server_ips":

                SERVER_IPS = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, SERVER_IPS, 0, lineArray.length - 1);

                break;
            case "rcon_ports":

                RCON_PORTS = new int[lineArray.length - 1];
                String[] rconPorts = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, rconPorts, 0, lineArray.length - 1);

                for (int i = 0; i < rconPorts.length; i++) {
                    RCON_PORTS[i] = Integer.decode(rconPorts[i]);
                }

                break;
            case "rcon_passwords":

                RCON_PASSWORDS = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, RCON_PASSWORDS, 0, lineArray.length - 1);

                break;
            case "ftp_usernames":

                FTP_USERNAMES = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, FTP_USERNAMES, 0, lineArray.length - 1);

                break;
            case "ftp_passwords":

                FTP_PASSWORDS = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, FTP_PASSWORDS, 0, lineArray.length - 1);

                break;
            case "discord_bot_token":

                DISCORD_TOKEN = lineArray[1];

                break;
            case "discord_channel_ids":

                DISCORD_CHANNEL_IDS = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, DISCORD_CHANNEL_IDS, 0, lineArray.length - 1);

                break;
            case "discord_channel_names":

                DISCORD_CHANNEL_NAMES = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, DISCORD_CHANNEL_NAMES, 0, lineArray.length - 1);

                break;
        }
    }
}
