package ciphers.hill;

import ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Test;

public class HillTest extends BaseCipherTest {

    String plainText1 = "pay more money";
    String cipherText1 = "lns hdle wmtrw";
    int[][] key1 = {
        {17, 17, 05},
        {21, 18, 21},
        {02, 02, 19}
    };
    
    String plainText2 = "pay more money";
    String cipherText2 = "nnd qpzq kivnj";
    int[][] key2 = {
        {17, 17, 04},
        {21, 17, 21},
        {01, 02, 19}
    };

    @Test
    @Override
    public void testEncryption() {

        System.out.println("-------------------------------------------------");
        Hill_David testEncryptionObj = new Hill_David(key1);
        String res = testEncryptionObj.encrypt(plainText1);
        System.out.println("Input of  : " + plainText1);
        System.out.println("should result in");
        System.out.println("Output of : " + cipherText1);
        System.out.println("Got       : " + res);
        Assert.assertTrue(res.equals(cipherText1));
        System.out.println("-------------------------------------------------");
        testEncryptionObj = new Hill_David(key2);
        res = testEncryptionObj.encrypt(plainText2);
        System.out.println("Input of  : " + plainText2);
        System.out.println("should result in");
        System.out.println("Output of : " + cipherText2);
        System.out.println("Got       : " + res);
        Assert.assertTrue(res.equals(cipherText2));
        System.out.println("-------------------------------------------------");
        
    }

    @Test
    @Override
    public void testDecryption() {

        System.out.println("-------------------------------------------------");
        Hill_David testDecryptionObj = new Hill_David(key1);
        String res = testDecryptionObj.decrypt(cipherText1);
        System.out.println("Input of  : " + cipherText1);
        System.out.println("should result in");
        System.out.println("Output of : " + plainText1);
        System.out.println("Got       : " + res);
        Assert.assertTrue(res.equals(plainText1));
        System.out.println("-------------------------------------------------");
        testDecryptionObj = new Hill_David(key2);
        res = testDecryptionObj.decrypt(cipherText2);
        System.out.println("Input of  : " + cipherText2);
        System.out.println("should result in");
        System.out.println("Output of : " + plainText2);
        System.out.println("Got       : " + res);
        Assert.assertTrue(res.equals(plainText2));
        System.out.println("-------------------------------------------------");
        
    }
}
