package ciphers.sdes;

import ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SDESTest extends BaseCipherTest  {

    private Cipher_SDES cipher;
    private ArrayList<int[]> expected;
    private ArrayList<int[]> actualResult;

    public SDESTest() {
        cipher = new Cipher_SDES();
    }

    @Before
    public void setup() {
        expected = new ArrayList<>();
        expected.add(new int[]{1,0,1,0,0,1,1,0});
        expected.add(new int[]{1,0,0,1,1,1,0,1});
        expected.add(new int[]{1,1,0,0,0,1,1,1});
        expected.add(new int[]{1,0,0,0,1,1,1,0});
    }

    @Test
    @Override
    public void testEncryption() {
        actualResult = cipher.encryptWord("wait");

        for(int i = 0; i < expected.size(); i++){
            Assert.assertArrayEquals(expected.get(i), actualResult.get(i));
        }

//        TODO
//        testEncryption("Plain text", "Cipher text");
    }

    @Test
    @Override
    public void testDecryption() {
        Assert.assertEquals(cipher.decryptWord(expected), "wait");

//        TODO
//        testDecryption("Cipher text", "Plain text");
    }
}