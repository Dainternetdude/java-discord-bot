package dain;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class FTPHandler {

    public static void grabFile(String file, int serverId) {

        String filename = file.split("/")[file.split("/").length - 1]; //oops hyperslow but clean

        int port = 21;
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(Settings.SERVER_IPS[serverId], port);
            ftpClient.login(Settings.FTP_USERNAMES[serverId], Settings.FTP_PASSWORDS[serverId]);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //sendMessageAllChannels("Getting file on thread " + serverId);

            File downloadFile1 = new File(serverId + filename);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(file, outputStream1);
            outputStream1.close();

            /*
            if (success) {
                sendMessageAllChannels("`latest.log` has been downloaded successfully on thread " + serverId);
            } else {
                sendMessageAllChannels("issue downloading file on thread " + serverId);
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
}
