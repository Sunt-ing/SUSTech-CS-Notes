package util;

/**
 * respresent document preview, which has md5, length and preview field.
 */
public class DocumentPreview {
  String md5;
  int length;
  String preview;

  /**
   * get md5 value
   * @return md5 value
   */
  public String getMd5() {
    return md5;
  }

  /**
   * set md5 value
   * @param md5 new md5 value
   */
  public void setMd5(String md5) {
    this.md5 = md5;
  }

  /**
   * constructor for DocumentPreview class
   * @param md5 md5 value
   * @param length length value
   * @param preview preview value
   */
  public DocumentPreview(String md5, int length, String preview) {
    this.md5 = md5;
    this.length = length;
    this.preview = preview;
  }

  /**
   * get preview of the DocumentPreview object
   * @return preview field
   */
  public String getPreview() {
    return preview;
  }

  /**
   * set preview value of DocumentPreview
   * @param preview new preview value
   */
  public void setPreview(String preview) {
    this.preview = preview;
  }

  /**
   * get length of the DocumentPreview object
   * @return length field
   */
  public int getLength() {
    return length;
  }

  /**
   * set length value
   * @param length new length
   */
  public void setLength(int length) {
    this.length = length;
  }
}