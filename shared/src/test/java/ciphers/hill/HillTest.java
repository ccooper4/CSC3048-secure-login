package ciphers.hill;

import ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Test;

public class HillTest extends BaseCipherTest {

    public HillTest() {
    }

    @Test
    public void testEncryption() {
//        
//        Hill_David testEncryptionObj = new Hill_David();
//        
//        String plainText  = "pay more money";
//        String cipherText = "lns hdle wmtrw";
//        
//        String res = testEncryptionObj.encrypt(plainText);
//        
//        Assert.assertTrue(res.equals(cipherText));
//        
//        System.out.println("Input of  : " + plainText);
//        System.out.println("should result in");
//        System.out.println("Output of : " + cipherText);
    }

    @Test
    public void testDecryption() {
        Hill_David testDecryptionObj = new Hill_David();
        
        String plainText  = "pay more money";
        String cipherText = "lns hdle wmtrw";
        
        String res = testDecryptionObj.decrypt(cipherText);        
        
        System.out.println("Input of  : " + cipherText);
        System.out.println("should result in");
        System.out.println("Output of : " + plainText);
        
        Assert.assertTrue(res.equals(plainText));
    }
}