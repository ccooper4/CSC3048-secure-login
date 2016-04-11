package util;

import ciphers.aes.Cipher_AES;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A logger wrapper to write encrypted log files to disk.
 */
public class EncryptedLogger {

    private static Logger log;

    private Cipher_AES aes;
    private File file = new File("log/log.txt");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public EncryptedLogger(Class clazz) {
        log = LoggerFactory.getLogger(clazz);
        ensureFileExists(file);
    }

    /**
     * Ensure that the given file exists.
     * @param file  The file ot check.
     */
    private void ensureFileExists(File file) {
        // If the log file doesn't exist
        if (!file.exists()) {
            try {
                // Check if the directory exists
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                // Create the file
                file.createNewFile();
            } catch (IOException e) {
                log.error("Error creating log file", e);
            }
        }
    }

    /**
     * Info level messages - general information about the application.
     * @param message   The message to log.
     */
    public void info(String message) {
        log.info(message);
        writeToDisk(message);
    }

    /**
     * Error level messages - information about an exception.
     * @param message   The message to log.
     * @param throwable The exception to log.
     */
    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
        writeToDisk(message);
        writeToDisk(throwable.getMessage());
    }

    private void writeToDisk(String logInfo) {
        try {
            // Initialise the aes cipher.
            if (aes == null) {
                aes = new Cipher_AES();
            }

            String timestamp = sdf.format(new Date());
            logInfo = timestamp + " : " + logInfo + "\n";
//            String encryptedInfo = aes.encrypt(logInfo); TODO : Handle large strings with AES
            String encryptedInfo = logInfo;
            FileUtils.writeStringToFile(file, encryptedInfo, true);
        } catch (IOException e) {
            log.error("Error encrypting log message", e);
        }
    }
}
