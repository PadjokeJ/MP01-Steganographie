package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;
import ch.epfl.cs107.utils.Bit;
/**
 * <b>Task 3.2: </b>Utility class to perform Text Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private TextSteganography(){}

    // ============================================================================================
    // =================================== EMBEDDING BIT ARRAY ====================================
    // ============================================================================================

    /**
     * Embed a bitmap message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedBitArray(int[][] cover, boolean[] message) {
        assert cover != null;
        assert cover.length > 0;
        assert cover[0] != null;
        assert cover[0].length > 0;

        assert message != null;

        int image[][] = new int[cover.length][cover[0].length];
        
        for (int i = 0; i < cover.length; i++) {
            for (int j = 0; j < cover[i].length; j++) {
                image[i][j] = cover[i][j];
            }
        }

        int maxEmbed = cover.length * cover[0].length;
        if (message.length <= maxEmbed) 
            maxEmbed = message.length;

        for (int i = 0; i < maxEmbed; i++) {
            int x = i % cover.length;
            int y = i / cover.length;
            image[y][x] = embedInLSB(cover[y][x], message[i]);
        }

        return image;
    }

    /**
     * Extract a bitmap from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static boolean[] revealBitArray(int[][] image) {
        assert image != null;
        if (image.length > 0)
            assert image[0] != null;

        int yLen = image.length;
        int xLen = 0;
        if (yLen != 0)
            xLen = image[0].length;
        
        boolean[] message = new boolean[xLen * yLen];
        if (message.length == 0)
            return message;
        for (int[] l : image) {
            assert l.length == image[0].length;
        }
        
        for (int y = 0; y < yLen; y++) {
            for (int x = 0; x < xLen; x++) {
                message[y * yLen + x] = getLSB(image[y][x]);
            }
        }

        return message;
    }



    // ============================================================================================
    // ===================================== EMBEDDING STRING =====================================
    // ============================================================================================

    /**
     * Embed a String message in an ARGB image
     * @param cover Cover image
     * @param message Embedded message
     * @return ARGB image with the message embedded
     */
    public static int[][] embedText(int[][] cover, byte[] message) {
        assert message != null;
        assert cover != null;
        
        for (int[] l : cover) {
            assert l != null;
            assert l.length == cover[0].length;
        }
        boolean[] bits = new boolean[message.length * 8];
        for (int i = 0; i < message.length; i++) {
            boolean[] bitArray = toBitArray(message[i]);
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = bitArray[j];
            }
        }
       
        return embedBitArray(cover, bits);
    }

    /**
     * Extract a String from an image
     * @param image Image to extract from
     * @return extracted message
     */
    public static byte[] revealText(int[][] image) {
        assert image != null;
        assert image.length > 0;

        assert image[0] != null;
        assert image[0].length > 0;
        
        for (int[] l : image) {
            assert l != null;
            assert l.length == image[0].length;
        }

        boolean[] bits = revealBitArray(image);
        byte[] bytes = new byte[bits.length / Byte.SIZE];
        
        for (int i = 0; i < bits.length / Byte.SIZE; i++) {
            boolean[] bitArray = new boolean[Byte.SIZE];
            for (int j = 7; j > 0; j--) {
                bitArray[j] = bits[Byte.SIZE * i + j];
            }
            bytes[i] = Bit.toByte(bitArray);
        }
        return bytes;
    }

}
