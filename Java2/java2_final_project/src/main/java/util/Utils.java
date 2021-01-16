package util;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * collection of all the util method used in this program
 */
public class Utils {

  /**
   * calculate md5 value using given bytes
   * @param bytes orginal bytes
   * @return md5 value
   */
  public static String calculateMD5(byte[] bytes) {
    String ret = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(bytes);
      ret = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
      System.out.println(ret);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return ret;
  }

  /**
   * calculate simple similarity
   * @param str1 one string
   * @param str2 another string
   * @return similarity ratye
   */
  public static double calculateSimpleSimilarity(String str1, String str2) {
    int cnt = 0;
    for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
      if (str1.charAt(i) == str2.charAt(i)) cnt++;
    }
    return (double) (cnt * 1000 / Math.max(str1.length(), str2.length())) / 1000;
  }

  /**
   * calculate Levenshtein distance
   * @param a one string
   * @param b another string
   * @return Levenshtein distance
   */
  public static int calculateLevenshteinDistance(String a, String b) {
    int[] costs = new int[b.length() + 1];
    for (int j = 0; j < costs.length; j++)
      costs[j] = j;
    for (int i = 1; i <= a.length(); i++) {
      costs[0] = i;
      int nw = i - 1;
      for (int j = 1; j <= b.length(); j++) {
        int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                          a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
        nw = costs[j];
        costs[j] = cj;
      }
    }
    return costs[b.length()];
  }

  /**
   * convert a byte array encoded by some charset to UTF8 cahrset
   * @param srcBytes source byte
   * @return result UTF8 byte
   * @throws IOException when charsetMatch cannot guess the original charset
   */
  public static byte[] getUtf8Bytes(byte[] srcBytes) throws IOException {
    CharsetDetector detector = new CharsetDetector();
    detector.setText(srcBytes);
    CharsetMatch charsetMatch = detector.detect();
    byte[] utf8Bytes = charsetMatch.getString().getBytes(StandardCharsets.UTF_8);
    // process with-bom
    if (utf8Bytes.length >= 3
        && utf8Bytes[0] == -17
        && utf8Bytes[1] == -69
        && utf8Bytes[2] == -65) {
      byte[] tmp = new byte[utf8Bytes.length - 3];
      System.arraycopy(utf8Bytes, 3, tmp, 0, tmp.length);
      utf8Bytes = tmp;
    }
    return utf8Bytes;
  }
}
