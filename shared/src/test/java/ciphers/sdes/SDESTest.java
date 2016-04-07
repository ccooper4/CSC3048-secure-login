package ciphers.sdes;

import ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SDESTest {
    Cipher_SDES cipher;
    ArrayList<int[]> expected;
    ArrayList<int[]> actualResult;

    public SDESTest() {
        cipher = new Cipher_SDES();
    }

    @Test
    public void testEncryption() {
        setup();
        actualResult = cipher.encryptWord("wait");

        for(int i = 0; i < expected.size(); i++){
            Assert.assertArrayEquals(expected.get(i), actualResult.get(i));
        }
    }

    @Test
    public void testDecryption() {
        setup();
        Assert.assertEquals(cipher.decryptWord(expected), "wait");
    }

    public void setup(){
        expected = new ArrayList<>();
        expected.add(new int[]{1,0,1,0,0,1,1,0});
        expected.add(new int[]{1,0,0,1,1,1,0,1});
        expected.add(new int[]{1,1,0,0,0,1,1,1});
        expected.add(new int[]{1,0,0,0,1,1,1,0});
    }
}