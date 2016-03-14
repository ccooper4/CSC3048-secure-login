package ciphers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseCipher {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract String encrypt(String plaintext);

    public abstract String decrypt(String encryptedText);
}
