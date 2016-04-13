package cipher.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class AESKeyGeneratorTest {

    @Test
    public void testGenerateAESKey() {
        String[][] generatedKey = AESKeyGenerator.generateAESKey();

        // check size of block key
        assertEquals(4, generatedKey.length);

        // Test entries not null
        for (String[] column : generatedKey) {
            for (String entry : column) {
                assertNotNull(entry);
            }
        }
    }
}