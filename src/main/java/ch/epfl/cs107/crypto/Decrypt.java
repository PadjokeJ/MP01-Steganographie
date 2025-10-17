package ch.epfl.cs107.crypto;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 2: </b>Utility class to decrypt a given cipher text.
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Decrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Decrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to decode a byte array message using a single character key
     * <p>
     * @param cipher Cipher message to decode
     * @param key Key to decode with
     * @return decoded message
     */
    public static byte[] caesar(byte[] cipher, byte key) {
        assert cipher != null;
        byte[] bytes = new byte[cipher.length];
        for(int i=0;i<cipher.length;i++) {
            if (cipher[i] - key < -128) {
                bytes[i] = (byte) (cipher[i] - key + 256);
            }
            if (cipher[i] - key >= -128) {
                bytes[i] = (byte) (cipher[i] - key);
            }
        }
        return bytes;
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * @param cipher Cipher message to decode
     * @param keyword Key to decode with
     * @return decoded message
     */
    public static byte[] vigenere(byte[] cipher, byte[] keyword) {
        assert cipher!= null;
        assert keyword != null;
        assert keyword.length>=1;
        byte[] bytes = new byte[cipher.length];
        for(int i=0;i<cipher.length;i++){
            int pos = i % keyword.length;
            if (cipher[i] - keyword[pos] < -128) {
                bytes[i] = (byte) (cipher[i] - keyword[pos] + 256);
            }
            if (cipher[i] - keyword[pos] >=-128) {
                bytes[i] = (byte) (cipher[i] - keyword[pos]);
            }
        }
        return bytes;
    }

    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode cbc-encrypted ciphers
     * @param cipher message to decode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return decoded message
     */
    public static byte[] cbc(byte[] cipher, byte[] iv) {
        assert iv != null;
        assert cipher != null;
        assert iv.length > 0;

        // La réciproque est très similaire à Encrypt
        // il y a seulement un détail qui change ci-dessous

        byte[] plainText = new byte[cipher.length];
        byte[] ivCopy = iv.clone();

        int blockSize = ivCopy.length;
        int numberOfBlocks = cipher.length / blockSize;
        int remainingBytes = cipher.length % blockSize;

        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < blockSize; j++) {
                int index = i * blockSize + j;
                plainText[index] = (byte) (cipher[index] ^ ivCopy[j]);

                // La seule chose qui change avec encrypt
                // est que le prochain iv est cipher[index] et non
                // plainText[index]
                // Autrement dit on inverse pas exactement ici
                // tandis que l'on inverse partout
                // plainText et cipherText dans le reste du code)
                ivCopy[j] = cipher[index];
            }
        }

        for (int i = 0; i < remainingBytes; i++) {
            int index = numberOfBlocks * blockSize + i;
            plainText[index] = (byte) (cipher[index] ^ ivCopy[i]);
        }

        return plainText;
        }

    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to decode xor-encrypted ciphers
     * @param cipher text to decode
     * @param key the byte we will use to XOR
     * @return decoded message
     */
    public static byte[] xor(byte[] cipher, byte key) {
        return Encrypt.xor(cipher, key);
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to decode otp-encrypted ciphers
     * @param cipher text to decode
     * @param pad the one-time pad to use
     * @return decoded message
     */
    public static byte[] oneTimePad(byte[] cipher, byte[] pad) {
        return Encrypt.oneTimePad(cipher,pad) ;
    }

}
