
package batch.ftp;

import org.apache.commons.net.ftp.FTPClient;

public class FtpDownloadExample {
    public static void main(String[] args) {
        // Utilizza i valori dal file di configurazione
        String host = FtpConfig.getHost();
        int port = FtpConfig.getPort();
        String username = FtpConfig.getUsername();
        String password = FtpConfig.getPassword();
        String remoteDirectory = FtpConfig.getRemoteDirectory();
        String localDirectory = FtpConfig.getLocalDirectory();

        FTPClient ftpClient = null;
        try {
            // Crea connessione
            ftpClient = FtpDownloadUtil.createFtpConnection(host, port, username, password);

            if (ftpClient != null) {
                // Scarica tutti i file consentiti
                FtpDownloadUtil.downloadAllFiles(ftpClient, remoteDirectory, localDirectory);
            }
        } finally {
            // Chiudi connessione
            FtpDownloadUtil.closeFtpConnection(ftpClient);
        }
    }
}