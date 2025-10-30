package ch.epfl.cs107;

import ch.epfl.cs107.crypto.Decrypt;
import ch.epfl.cs107.crypto.Encrypt;
import ch.epfl.cs107.stegano.ImageSteganography;
import ch.epfl.cs107.stegano.TextSteganography;
import ch.epfl.cs107.utils.Bit;
import ch.epfl.cs107.utils.Image;
import ch.epfl.cs107.utils.Text;

public final class Cli {
  private Cli() {}

  public static void main(String[] args) {
    assert args.length > 0;
    
    if (args[0].startsWith("e")) {
      assert args.length > 1;
      encode(args);
      return;
    }

    if (args[0].startsWith("d")) {
      assert args.length > 1;
      decode(args);
      return;
    }

    System.out.println("usage: cli <command> [arguments]");
    System.out.println();
    System.out.println("commands:");
    System.out.print("  e[ncode]");
    System.out.print("\033[15G - ");
    System.out.println("encodes a text using a given cypher, key");
    
    System.out.print("  d[ecode]"); 
    System.out.print("\033[15G - ");
    System.out.println("decodes a text using a given cypher, key");

    System.out.println();
    System.out.println("cyphers :");
    System.out.println("- c[aesar]");
    System.out.println("- v[igenere]");
    System.out.println("- x[or]");
    System.out.println("- b[c]");

  }

  static void encode(String[] args) {
    byte[] text = Text.toBytes(args[1]);
    
    String cyph;
    String key ;

    if (args.length == 2) {
      cyph = "c";
      key  = "7";
    } else {
      assert args.length >= 4;
      cyph = args[2].substring(0, 1);
      key  = args[3];
    }
    
    byte[] dec;

    switch (cyph) {
      case "c":
        dec = Encrypt.caesar(text, (byte) Integer.parseInt(key));
        break;
      case "v":
        dec = Encrypt.vigenere(text, Text.toBytes(key));
        break;
      case "x":
        dec = Encrypt.xor(text, (byte) Integer.parseInt(key));
        break;
      case "b":
        dec = Encrypt.cbc(text, Text.toBytes(key));
        break;
      default:
        dec = new byte[0];
    }
    System.out.println(Text.toString(dec));
  }

  static void decode(String[] args) {
    byte[] text = Text.toBytes(args[1]);

    String cyph;
    String key ;
    
    if (args.length == 2) {
      cyph = "c";
      key  = "7";
    } else {
      assert args.length >= 4;
      cyph = args[2].substring(0, 1);
      key  = args[3];
    }
    
    byte[] dec;

    switch (cyph) {
      case "c":
        dec = Decrypt.caesar(text, (byte) Integer.parseInt(key));
        break;
      case "v":
        dec = Decrypt.vigenere(text, Text.toBytes(key));
        break;
      case "x":
        dec = Decrypt.xor(text, (byte) Integer.parseInt(key));
        break;
      case "b":
        dec = Decrypt.cbc(text, Text.toBytes(key));
        break;
      default:
        dec = new byte[0];
    }
    System.out.println(Text.toString(dec));
  }

}
