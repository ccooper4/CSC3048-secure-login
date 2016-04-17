package cipher.ciphers.rsa;

import cipher.ciphers.BaseCipherTest;
import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RSATest extends BaseCipherTest {

    public RSATest() {
        cipher = new Cipher_RSA();
    }

    //region Main Crypto Tests

    @Test
    @Override
    public void testEncryption() {
        testEncryption("markfrequency", "80200 20944 24574 4979 54423 60810 59202");
    }

    @Test
    @Override
    public void testDecryption() {
        testDecryption("80200 20944 24574 4979 54423 60810 59202", "MARKFREQUENCY ");
    }

    //endregion

    //region RSA Component Tests

    @Test
    public void testExponentiationBySquaringDivision() {

        long num = 21;
        long exp = 7;
        long n = 133;

        Cipher_RSA rsaCipher = (Cipher_RSA)cipher;

        long res = rsaCipher.applyExponentiationBySquaringDivision(num, exp, n);

        assertEquals(14, res);
    }

    @Test
    public void testExponentiationBySquaringDivisionLargerPQ() {

        //C = M^E mod(n)
        long num = 131;
        long exp = 65777;
        long n = 86609;

        Cipher_RSA rsaCipher = (Cipher_RSA)cipher;

        long res = rsaCipher.applyExponentiationBySquaringDivision(num, exp, n);

        assertEquals(70695, res);
    }

    @Test
    public void testCalculateEFromEuclidGcd() {

        int d = 31;
        int w = 108;

        Cipher_RSA rsaCipher = (Cipher_RSA)cipher;

        int e = rsaCipher.calculateEFromEuclidGcd(d,w,w);

        assertEquals(7, e);

    }


    //endregion
}