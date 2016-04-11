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

    private static Logger log = LoggerFactory.getLogger(EncryptedLogger.class);
    private static Cipher_AES aes = new Cipher_AES();
    private static File file = new File("./log/log.txt");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    // Static initializer
    static {
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
    public static void info(String message) {
        log.info(message);
        writeToDisk(message);
    }

    /**
     * Error level messages - information about an exception.
     * @param message   The message to log.
     * @param throwable The exception to log.
     */
    public static void error(String message, Throwable throwable) {
        log.error(message, throwable);
        writeToDisk(message);
        writeToDisk(throwable.getMessage());
    }

    private static void writeToDisk(String logInfo) {
        try {
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
