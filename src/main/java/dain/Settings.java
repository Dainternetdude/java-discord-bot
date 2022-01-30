package dain;

import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.util.Scanner;

public class Settings {

    // bot settings
    public static String COMMAND_PREFIX;

    public static String [] SERVER_NAMES;
    public static String [] SERVER_IPS;
    public static String [] RCON_PORTS;
    public static String [] RCON_PASSWORDS;

    public static String [] FTP_USERNAMES;
    public static String [] FTP_PASSWORDS;

    public static String DISCORD_TOKEN;
    public static String [] DISCORD_CHANNEL_IDS;
    public static String [] DISCORD_CHANNEL_NAMES;

    public static final String filename = "settings.txt";

    // program settings
    public static boolean useGui = true;
    public static boolean isLocal = false;

    public static void initialize() {

        String line;

        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                writeExampleFile();
            }
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) {
                line = myReader.nextLine();

                processLine(line);
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isLocal) {
            Logger.log("Bot is running in local mode. Server IPs & FTP login details will be overridden.", Logger.LoggingLevel.WARN);
            SERVER_IPS = new String[] {"127.0.0.1"};
            FTP_USERNAMES = new String[0];
            FTP_PASSWORDS = new String[0];
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

                RCON_PORTS = new String[lineArray.length - 1];
                System.arraycopy(lineArray, 1, RCON_PORTS, 0, lineArray.length - 1);

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

    public static void writeToFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write("command_prefix: " + COMMAND_PREFIX + "\n" +
                    "\n" +
                    "server_names: " + combine(SERVER_NAMES) + "\n" +
                    "server_ips: " + combine(SERVER_IPS) + "\n" +
                    "rcon_ports: " + combine(RCON_PORTS) + "\n" +
                    "rcon_passwords: " + combine(RCON_PASSWORDS) + "\n" +
                    "\n" +
                    "ftp_usernames: " + combine(FTP_USERNAMES) + "\n" +
                    "ftp_passwords: " + combine(FTP_PASSWORDS) + "\n" +
                    "\n" +
                    "discord_bot_token: " + DISCORD_TOKEN + "\n" +
                    "discord_channel_ids: " + combine(DISCORD_CHANNEL_IDS) + "\n" +
                    "discord_channel_names: " + combine(DISCORD_CHANNEL_NAMES) + "\n");
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeExampleFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write("command_prefix: >\n" +
                    "\n" +
                    "server_names: MySurvivalServer MyCreativeServer\n" +
                    "server_ips: yourserver.net yourcmp.net\n" +
                    "rcon_ports: 25540 25541\n" +
                    "rcon_passwords: secure_password another_secure_password\n" +
                    "\n" +
                    "ftp_usernames: survivalusername usernameforcreative\n" +
                    "ftp_passwords: yet_another_secure_password extremely_secure_password\n" +
                    "\n" +
                    "discord_bot_token: le.epic.token89413186135478651684518451684513851685136851385195\n" +
                    "discord_channel_ids: 865981656454 98465189654654\n" +
                    "discord_channel_names: MyChannel MyOtherChannel\n");
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String combine(String[] stringArray) {
        String stringOut = "";
        for (int i = 0; i < stringArray.length; i++) {
            stringOut = stringOut + stringArray[i];
            if (i != stringArray.length - 1) stringOut = stringOut + " ";
        }
        return stringOut;
    }
}
