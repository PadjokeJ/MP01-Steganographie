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
 * @author Solved by Jonatan PFISTER (jonatan.pfister@epfl.ch)
 * @version 2.0.0
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
        byte[] decrypted = Decrypt.caesar(hint2, (byte) (255 - 148));
        System.out.println(Text.toString(decrypted));
        
        /* We find 0x37 as a hint */ 
        /* Let's decrypt the third hint with 0x37 and xor */

        byte[] hint3 = Helper.read("challenge/hint3.txt");
        byte[] hi3decrypted = Decrypt.xor(hint3, (byte) 0x37);
        System.out.println(Text.toString(hi3decrypted));
        /* KEYWORD=c4Ptur37hEfL46; IV_POS=120..136 */
        
        /* Let's extract the keyword */
        String hint3Str = Text.toString(hi3decrypted);
        String hint3KeywordStr = hint3Str.split("; ")[0].split("=")[1];
        //System.out.println(hint3KeywordStr); --> "c4Ptur37hEfL46"

        /* We find a keyword and a IV position */
        /* Let's decrypt images */
        int[][] image1 = Helper.readImage("challenge/image1.png");
        byte[] textimage1 = TextSteganography.revealText(image1);
        /* A keyword is the technical term for the key in vigenere, so let's use that */
        byte[] image1decrypted = Decrypt.vigenere(textimage1, Text.toBytes(hint3KeywordStr));
        String image1Str = Text.toString(image1decrypted).substring(120, 136);
        System.out.println(image1Str);
        
        // Let's truncate out the "IV:"
        byte[] posBytes1 = new byte[IV_LEN - 3];
        posBytes1 = Text.toBytes(image1Str.split(":")[1]);

        /* Let's reveal the second image */
        int[][] image2 = Helper.readImage("challenge/image2.png");
        byte[] textimage2 = TextSteganography.revealText(image2);
        
        /* and IV is the technical term for the key in CBC, so we can use that to solve it */
        byte[] image2decrypted = Decrypt.cbc(textimage2, posBytes1);
        String solString = Text.toString(image2decrypted);
        
        /* Let's truncate out the flag until the ending curly bracket */
        int posMax = solString.indexOf('}') + 1;
        String flag = solString.substring(0, posMax);
        System.out.println(flag);
        /* We find the flag FLAG{C5-IO7;F0r743w1Nn} */
        return flag;
    }

    public static int[] freqAnalysis(byte[] text) {

        int[] counter = new int[255];

        for (int i = 0; i < text.length; i++) {
            counter[text[i] + 128] += 1;
        }

        return counter;
    }

    public static void main(String[] args) {
        challenge();
    }
}
