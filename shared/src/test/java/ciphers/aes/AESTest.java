package ciphers.aes;

import ciphers.BaseCipherTest;
import java.util.Arrays;
import org.junit.Test;

public class AESTest extends BaseCipherTest {

    public AESTest() {
        cipher = new Cipher_AES();
    }

    @Test
    @Override
    public void testEncryption() {
//        testEncryption("mark frequency  ", "expected result");

//        m   q c
//        a f u y
//        r r e
//        k e n

//        6d 20 71 63
//        61 66 75 79
//        72 72 65 20
//        6b 65 6e 20
    }

    @Test
    @Override
    public void testDecryption() {
//        testDecryption("cipher text", "expected result");
    }
    
    @Test
    public void testKeyExpansion() {
        
        //keyExpansion("2B7E151628AED2A6ABF7158809CF4F3C", 4, numRounds, numBlocks);
        
        String[][] res = KeyExpansion_AES.keyExpansion("2B7E151628AED2A6ABF7158809CF4F3C", 4, 10, 4);
        
        System.out.println(Arrays.deepToString(res));
        
    }
}