package ch.epfl.cs107;

import ch.epfl.cs107.crypto.Decrypt;
import ch.epfl.cs107.crypto.Encrypt;
import ch.epfl.cs107.utils.Text;
import ch.epfl.cs107.stegano.TextSteganography;

import java.nio.charset.StandardCharsets;

/**
 * <b>06</b> Provided for you to attempt the challenge in
 *
 * @author Mehdi ALAOUI (mehdi.alaoui@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public class Challenge {
    public static int IV_LEN = 16;
    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Challenge(){}

    // ============================================================================================
    // ======================================== CHALLENGE =========================================
    // ============================================================================================

    /**
     * Solves the challenge
     *
     * @return the flag in the format FLAG{********}
     */
    public static String challenge(){
        /* First hint is used to tell us about freq. analysis, so we ignore it */

        byte[] hint2 = Helper.read("challenge/hint2.txt");

        /* Let's try to decrypt it using freq analysis */

        int[] count = freqAnalysis(hint2);

        /* We find that 80 comes up the most often */
        assert count[80] == 6;
        /* We also see that there is a triple character at the end (lets attempt for ...) */
        byte[] decrypted = Decrypt.caesar(hint2, (byte) (255 - 149));
        //System.out.println(Text.toString(decrypted));
        
        /* We find 0x37 as a hint */ 
        /* Let's decrypt the third hint with 0x37 and diverse algs */

        byte[] hint3 = Helper.read("challenge/hint3.txt");
        byte[] hi3decrypted = Decrypt.xor(hint3, (byte) 0x37);
        //System.out.println(Text.toString(hi3decrypted));
        /* KEYWORD=c4Ptur37hEfL46; IV_POS=120..136 */

        /* We find a keyword and a IV position */
        /* Let's decrypt images */
        int[][] image1 = Helper.readImage("challenge/image1.png");
        byte[] textimage1 = TextSteganography.revealText(image1);
        byte[] image1decrypted = Decrypt.vigenere(textimage1, Text.toBytes("c4Ptur37hEfl46"));
        //System.out.println(Text.toString(image1decrypted).substring(120, 136));
        
        byte[] posBytes = new byte[IV_LEN];
        for (int i = 3; i < IV_LEN; i++) {
            int k = 120 + i;
            posBytes[i - 3] = image1decrypted[k];
        }
        //System.out.println(Text.toString(posBytes));

        int[][] image2 = Helper.readImage("challenge/image2.png");
        byte[] textimage2 = TextSteganography.revealText(image2);
        
        byte[] image2decrypted = Decrypt.cbc(textimage2, posBytes);
        String solString = Text.toString(image2decrypted);
        int posMax = solString.indexOf('}');
        System.out.println(Text.toString(image2decrypted).substring(0, 1000));

        return "FLAG{C5-IO7}";
    }

    public static int[] freqAnalysis(byte[] text) {

        int[] counter = new int[255];

        for (int i = 0; i < text.length; i++) {
            counter[text[i] + 128] += 1;
        }

        //for (int i = 0; i < counter.length; i++ ) {
        //    if (counter[i] > 0)
        //        System.out.println("-" + i + "\t:  " + counter[i]);
        //}

        return counter;
    }

    public static void main(String[] args) {
        challenge();
    }
}
