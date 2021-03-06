package cipher.ciphers.hill;

import java.math.BigInteger;

public class MatrixOperations {

    public static int[][] inverse(int[][] key) {

        BigInteger[][] keyInverse = new BigInteger[3][3];
        int[][] keyInverseInt = new int[3][3];

        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < key.length; j++) {
                keyInverse[i][j] = BigInteger.valueOf(key[i][j]);
            }
        }

        ModMatrix obj2 = new ModMatrix(keyInverse);
        ModMatrix inverse2 = obj2.inverse(obj2);

        keyInverse = inverse2.getData();

        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < key.length; j++) {
                keyInverseInt[i][j] = keyInverse[i][j].intValue();
            }
        }

        return keyInverseInt;

    }

}

//Source github.com/PraAnj/Modular-Matrix-Inverse-Java