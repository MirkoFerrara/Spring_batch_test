
package batch.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FtpDownloadUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpDownloadUtil.class);

    public static boolean downloadFile(FTPClient ftpClient, String remoteFilePath, String localFilePath) {
        try {
            // Imposta il tipo di trasferimento su binario
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            // Crea la directory locale se non esiste
            File localFile = new File(localFilePath);
            localFile.getParentFile().mkdirs();

            // Stream per salvare il file
            try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
                // Scarica il file
                boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);

                if (success) {
                    logger.info("File scaricato con successo: {}", remoteFilePath);
                    return true;
                } else {
                    logger.error("Impossibile scaricare il file: {}", remoteFilePath);
                    return false;
                }
            }
        } catch (IOException e) {
            logger.error("Errore durante il download del file", e);
            return false;
        }
    }

    public static void downloadAllFiles(FTPClient ftpClient, String remoteDirectory, String localDirectory) {
        try {
            if (!ftpClient.changeWorkingDirectory(remoteDirectory)) {
                logger.error("Impossibile cambiare directory: {}", remoteDirectory);
                return;
            }

            FTPFile[] files = ftpClient.listFiles();

            String[] allowedExtensions = FtpConfig.getAllowedFileExtensions();
            int successfulDownloads = 0;
            int failedDownloads = 0;
            int skippedFiles = 0;

            for (FTPFile file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();

                    boolean isAllowedFile = Arrays.stream(allowedExtensions)
                            .anyMatch(ext -> fileName.toLowerCase().endsWith(ext.trim().toLowerCase()));

                    if (isAllowedFile) {
                        String remoteFilePath = remoteDirectory + "/" + fileName;
                        String localFilePath = localDirectory + File.separator + fileName;

                        if (downloadFile(ftpClient, remoteFilePath, localFilePath)) {
                            successfulDownloads++;
                        } else {
                            failedDownloads++;
                        }
                    } else {
                        skippedFiles++;
                        logger.info("File ignorato (estensione non consentita): {}", fileName);
                    }
                }
            }

            logger.info("Riepilogo download - Scaricati: {}, Falliti: {}, Saltati: {}",
                    successfulDownloads, failedDownloads, skippedFiles);

        } catch (IOException e) {
            logger.error("Errore durante il download dei file", e);
        }
    }

    public static FTPClient createFtpConnection(String host, int port, String username, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            // Connessione al server
            ftpClient.connect(host, port);

            // Login
            if (!ftpClient.login(username, password)) {
                logger.error("Login FTP fallito");
                ftpClient.disconnect();
                return null;
            }

            // Modalità passiva
            ftpClient.enterLocalPassiveMode();

            logger.info("Connessione FTP stabilita con successo");
            return ftpClient;
        } catch (IOException e) {
            logger.error("Errore durante la connessione FTP", e);
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                logger.error("Errore durante la chiusura della connessione", ex);
            }
            return null;
        }
    }

    public static void closeFtpConnection(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                logger.info("Connessione FTP chiusa");
            } catch (IOException e) {
                logger.error("Errore durante la chiusura della connessione FTP", e);
            }
        }
    }
}