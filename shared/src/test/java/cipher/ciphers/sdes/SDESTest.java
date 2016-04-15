package cipher.ciphers.sdes;

import cipher.ciphers.BaseCipherTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SDESTest extends BaseCipherTest  {

    public SDESTest() {
        cipher = new Cipher_SDES();
    }

    @Test
    @Override
    public void testEncryption() {
        testEncryption("markfrequency",
                "01000011 01011010 10100111 01111111 00000100 10100111 11011111 00101101 01101100 11011111 01101111 11010001 01110111 ");
    }

    @Test
    @Override
    public void testDecryption() {
        testDecryption("01000011 01011010 10100111 01111111 00000100 10100111 11011111 00101101 01101100 11011111 01101111 11010001 01110111 ",
                "markfrequency");
    }
}