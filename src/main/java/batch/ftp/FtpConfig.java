package batch.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FtpConfig {
    private static final Properties props = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(FtpConfig.class);

    static {
        try (InputStream input = FtpConfig.class.getClassLoader().getResourceAsStream("ftp.properties")) {
            if (input == null) {
                logger.error("File ftp.properties non trovato");
                throw new IOException("File di configurazione non trovato");
            }
            props.load(input);
        } catch (IOException ex) {
            logger.error("Impossibile caricare la configurazione FTP", ex);
        }
    }

    public static String getHost() {
        return props.getProperty("ftp.host");
    }

    public static int getPort() {
        return Integer.parseInt(props.getProperty("ftp.port", "21"));
    }

    public static String getUsername() {
        return props.getProperty("ftp.username");
    }

    public static String getPassword() {
        return props.getProperty("ftp.password");
    }

    public static String getRemoteDirectory() {
        return props.getProperty("ftp.remote.directory");
    }

    public static String getLocalDirectory() {
        return props.getProperty("ftp.local.directory");
    }

    public static String[] getAllowedFileExtensions() {
        String extensions = props.getProperty("ftp.allowed.extensions", ".csv");
        return extensions.split(",");
    }
}