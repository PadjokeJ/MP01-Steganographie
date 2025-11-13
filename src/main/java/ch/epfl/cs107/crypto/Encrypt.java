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
 * <b>Task 2: </b>Utility class to encrypt a given plain text.
 *
 * @author Arthur Delémont (arthur.delemont@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Encrypt {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Encrypt(){}

    // ============================================================================================
    // ================================== CAESAR'S ENCRYPTION =====================================
    // ============================================================================================

    /**
     * Method to encode a byte array message using a single character key
     * the key is simply added to each byte of the original message
     *
     * @param plainText The byte array representing the string to encode
     * @param key the byte corresponding to the char we use to shift
     * @return an encoded byte array
     */
    public static byte[] caesar(byte[] plainText, byte key) {
        assert plainText != null;
        byte[] bytes = new byte[plainText.length];
        for(int i=0;i<plainText.length;i++){
            if(plainText[i]+key>127){
                bytes[i]= (byte) (plainText[i]+key-256);
            }if(plainText[i]+key<=127){
                bytes[i]= (byte) (plainText[i]+key);
            }
        }
        return bytes;
    }

    // ============================================================================================
    // =============================== VIGENERE'S ENCRYPTION ======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a byte array keyword
     * The keyword is repeated along the message to encode
     * The bytes of the keyword are added to those of the message to encode
     * @param plainText the byte array representing the message to encode
     * @param keyword the byte array representing the key used to perform the shift
     * @return an encoded byte array
     */
    public static byte[] vigenere(byte[] plainText, byte[] keyword) {
        assert plainText != null;
        assert keyword != null;
        assert keyword.length >=1;
        byte[] bytes = new byte[plainText.length];
        for(int i=0;i<plainText.length;i++){
            int pos = i % keyword.length;
            if (plainText[i] + keyword[pos] > 127) {
                bytes[i] = (byte) (plainText[i] + keyword[pos] - 256);
            }
            if (plainText[i] + keyword[pos] <= 127) {
                bytes[i] = (byte) (plainText[i] + keyword[pos]);
            }
        }
        return bytes;
        }



    // ============================================================================================
    // =================================== CBC'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method applying a basic chain block counter of XOR without encryption method.
     * @param plainText the byte array representing the string to encode
     * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
     * @return an encoded byte array
     */
    public static byte[] cbc(byte[] plainText, byte[] iv) {
        assert iv != null;
        assert plainText != null;
        // iv ne doit pas être nul
        assert iv.length > 0;

        byte[] cipher = new byte[plainText.length];
        byte[] ivCopy = iv.clone();

        int blockSize = ivCopy.length;

        // Pour compter le nombre de loop on diviser le nombre de lettre du texte par le nombre de lettre du pad
        // et on regarde si il faut un tour en plus si il y a un reste
        int numberOfLoop = (plainText.length / blockSize);
        int remainingBytes = plainText.length % blockSize;

        for (int i = 0; i < numberOfLoop; i++) {

            for (int j = 0; j < blockSize; j++) {

                // Variable qui contient l'index du char à changer dans cipher
                int indexOfChar = i * blockSize + j;

                cipher[indexOfChar] = (byte) (plainText[indexOfChar] ^ ivCopy[j]);

                // ensuite on remplace la valeur de pad par ce qu'on vient
                // de calculer pour le prochain tour
                ivCopy[j] = cipher[indexOfChar];
            }

        }


        // On boucle sur les derniers char
        for (int i = 0; i < remainingBytes; i++) {

            // Variable qui donne l'index du char à changer dans cipher[]
            int indexToChange = (numberOfLoop * blockSize) + i;

            cipher[indexToChange] = (byte) (plainText[indexToChange] ^ ivCopy[i]);
        }

        return cipher;
    }

    // ============================================================================================
    // =================================== XOR'S ENCRYPTION =======================================
    // ============================================================================================

    /**
     * Method to encode a byte array using a XOR with a single byte long key
     * @param plainText the byte array representing the string to encode
     * @param key the byte we will use to XOR
     * @return an encoded byte array
     */
    public static byte[] xor(byte[] plainText, byte key) {
        assert plainText != null;
        byte[] bytes = new byte[plainText.length];
        for (int i =0; i<plainText.length;i++){
            bytes[i]=(byte)(plainText[i]^key);
        }
        return bytes;
    }

    // ============================================================================================
    // =================================== ONETIME'S PAD ENCRYPTION ===============================
    // ============================================================================================

    /**
     * Method to encode a byte array using a one-time pad of the same length.
     *  The method XOR them together.
     * @param plainText the byte array representing the string to encode
     * @param pad the one-time pad
     * @return an encoded byte array
     */
    public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
        assert plainText != null;
        assert pad != null;
        assert pad.length==plainText.length;
        byte[] bytes = new byte[plainText.length];
        for (int i =0; i<plainText.length;i++){
            bytes[i]=(byte)(plainText[i]^pad[i]);
        }
        return bytes;
    }

    /**
     * Method to encode a byte array using a one-time pad
     * @param plainText Plain text to encode
     * @param pad Array containing the used pad after the execution
     * @param result Array containing the result after the execution
     */
    public static void oneTimePad(byte[] plainText, byte[] pad, byte[] result) {
        pad = Helper.generateRandomBytes(plainText.length);
        result = Encrypt.oneTimePad(plainText,pad);
    }

}
