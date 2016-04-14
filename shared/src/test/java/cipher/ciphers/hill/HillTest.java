package cipher.ciphers.hill;

import cipher.ciphers.BaseCipherTest;
import org.junit.Test;

public class HillTest extends BaseCipherTest {
    
    private String plainText = "pay more money";
    private String cipherText = "lwo hxvo movwq";

    private String plainText1 = "pay more money";
    private String cipherText1 = "lns hdle wmtrw";

    private int[][] key1 = {
        {17, 17, 05},
        {21, 18, 21},
        {02, 02, 19}
    };
    
    private String plainText2 = "pay more money";
    private String cipherText2 = "nnd qpzq kivnj";

    private int[][] key2 = {
        {17, 17, 04},
        {21, 17, 21},
        {01, 02, 19} };

    @Test
    @Override
    public void testEncryption() {
        cipher = new Cipher_Hill();
        testEncryption(plainText, cipherText);
    }
    
    @Test
    public void testEncryption2() {        
        cipher = new Cipher_Hill(key1);
        testEncryption(plainText1, cipherText1);
    }
    
    @Test
    public void testEncryption3() {
        cipher = new Cipher_Hill(key2);
        testEncryption(plainText2, cipherText2);
    }

    @Test
    @Override
    public void testDecryption() {
        //cipher = new Cipher_Hill();
        //testDecryption(cipherText1, plainText1);
    }
    
    @Test
    public void testDecryption2() {        
        cipher = new Cipher_Hill(key1);
        testDecryption(cipherText1, plainText1);
    }
    
    @Test
    public void testDecryption3() {
        cipher = new Cipher_Hill(key2);
        testDecryption(cipherText2, plainText2);
    }
}
