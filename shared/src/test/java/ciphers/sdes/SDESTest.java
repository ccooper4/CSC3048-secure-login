package ciphers.sdes;

import ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SDESTest extends BaseCipherTest  {

    private Cipher_SDES cipher;
    private ArrayList<int[]> expected;
    private ArrayList<int[]> expected2;
    private ArrayList<int[]> actualResult;

    public SDESTest() {
        cipher = new Cipher_SDES();
    }

    @Before
    public void setup() {
        expected = new ArrayList<>();
        expected.add(new int[]{0,0,0,1,1,1,1,1});
        expected.add(new int[]{0,1,0,1,1,0,1,0});
        expected.add(new int[]{1,1,0,0,0,0,1,0});
        expected.add(new int[]{0,0,1,0,0,1,1,1});

        expected2 = new ArrayList<>();
        expected2.add(new int[]{0,1,0,0,0,0,1,1});
        expected2.add(new int[]{0,1,0,1,1,0,1,0});
        expected2.add(new int[]{1,0,1,0,0,1,1,1});
        expected2.add(new int[]{0,1,1,1,1,1,1,1});
        expected2.add(new int[]{0,0,0,0,0,1,0,0});
        expected2.add(new int[]{1,0,1,0,0,1,1,1});
        expected2.add(new int[]{1,1,0,1,1,1,1,1});
        expected2.add(new int[]{0,0,1,0,1,1,0,1});
        expected2.add(new int[]{0,1,1,0,1,1,0,0});
        expected2.add(new int[]{1,1,0,1,1,1,1,1});
        expected2.add(new int[]{0,1,1,0,1,1,1,1});
        expected2.add(new int[]{1,1,0,1,0,0,0,1});
        expected2.add(new int[]{0,1,1,1,0,1,1,1});
    }

    @Test
    @Override
    public void testEncryption() {
        //First word to test
        actualResult = cipher.encryptWord("wait");

        for(int i = 0; i < expected.size(); i++){
            Assert.assertArrayEquals(expected.get(i), actualResult.get(i));
        }

        //Second word to test
        actualResult = cipher.encryptWord("markfrequency");

        for(int i = 0; i < expected.size(); i++){
            Assert.assertArrayEquals(expected2.get(i), actualResult.get(i));
        }

//        TODO
//        testEncryption("Plain text", "Expected Cipher text");
    }

    @Test
    @Override
    public void testDecryption() {
        //First word to decrypt
        Assert.assertEquals(cipher.decryptWord(expected), "wait");

        //Second word to decrypt
        Assert.assertEquals(cipher.decryptWord(expected2), "markfrequency");

//        TODO
//        testDecryption("Cipher text", "Expected plain text");
    }
}