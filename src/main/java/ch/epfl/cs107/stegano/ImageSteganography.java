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

/**
 * <b>Task 3.1: </b>Utility class to perform Image Steganography
 *
 * @author Jonatan Pfister (jonatan.pfister@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImageSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private ImageSteganography(){}
    
    static boolean isRectangular(int[][] image) {
        int ll = image[0].length;
        for (int[] line: image) {
            if (ll != line.length)
                return false;
        }
        return true;
    }

    static boolean containsNullLines(int[][] image) {
        for (int[] line: image) {
            if (line == null) {
                return true;
            }
        }
        return false;
    }

    // ============================================================================================
    // ================================== EMBEDDING METHODS =======================================
    // ============================================================================================

    /**
     * Embed an ARGB image on another ARGB image (the cover)
     * @param cover Cover image
     * @param argbImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedARGB(int[][] cover, int[][] argbImage, int threshold){
        assert argbImage != null;
        assert cover != null;
        assert !containsNullLines(argbImage);
        assert isRectangular(argbImage);

        int[][] image;
        int[][] grayImage;
        grayImage = toGray(argbImage);
        
        image = embedGray(cover, grayImage, threshold);
        return image;
    }

    /**
     * Embed a Gray scaled image on another ARGB image (the cover)
     * @param cover Cover image
     * @param grayImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedGray(int[][] cover, int[][] grayImage, int threshold){
        assert grayImage != null;
        assert cover != null;
        assert !containsNullLines(grayImage);
        assert isRectangular(grayImage);
        
        // is valid gray
        for (int[] line: grayImage) {
            for (int pixel: line) {
                assert pixel >= 0 && pixel <= 255;
            }
        }

        int[][] image;
        boolean[][] bwImage;
        bwImage = toBinary(grayImage, threshold);

        image = embedBW(cover, bwImage);
        return image;
    }

    /**
     * Embed a binary image on another ARGB image (the cover)
     * @param cover Cover image
     * @param load Embedded image
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedBW(int[][] cover, boolean[][] load){
        assert load != null;
        assert cover != null;

        assert load.length > 0;
        assert cover.length > 0;

        assert !containsNullLines(cover);
        
        assert load[0] != null;

        assert cover[0].length > 0;
        assert load[0].length > 0;

        assert (cover.length >= load.length) && (cover[0].length >= load[0].length);
        
        assert isRectangular(cover);

        int[][] image = new int[cover.length][cover[0].length];
        
        for (int x = 0; x < cover.length; x++) {
            for (int y = 0; y < cover[x].length; y++) {
                if (x < load.length && y < load[0].length)
                    image[x][y] = embedInLSB(cover[x][y], load[x][y]);
                else
                    image[x][y] = cover[x][y];
            }
        }

        return image;
    }

    // ============================================================================================
    // =================================== REVEALING METHODS ======================================
    // ============================================================================================

    /**
     * Reveal a binary image from a given image
     * @param image Image to reveal from
     * @return binary representation of the hidden image
     */
    public static boolean[][] revealBW(int[][] image) {
        assert image != null;
        assert !containsNullLines(image);

        assert image.length > 0;
        assert image[0].length > 0;

        boolean[][] bwImage = new boolean[image.length][image[0].length];

        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                bwImage[x][y] = getLSB(image[x][y]);
            }
        }

        return bwImage;
    }

}
