package dao;

import java.sql.*;
import java.util.ArrayList;

/**
 * The DAO (data access object) of Text.
 * connection is database connection.
 * deName is the database name.
 */
public class TextDao {
  Connection connection = null;
  String dbName = "fileDb.sqlite";

  /**
   * The constructor of TextDao, which can also initialize database.
   *
   */
  public TextDao() {
    System.out.println("try to open db.");
    openDB();
    createTable();
  }

  /**
   * Open database, and initialize the connection.
   *
   */
  private void openDB() {
    try {
      System.out.println("try to find the driver.");
      Class.forName("org.sqlite.JDBC");
    } catch (Exception e) {
      System.err.println("Cannot find the driver.");
      System.exit(1);
    }
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
      connection.setAutoCommit(false);
      System.out.println("Successfully connected to the database.");
    } catch (Exception e) {
      System.err.println("openDB" + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Create the table Text..
   *
   */
  private void createTable() {
    if (connection != null) {
      try {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30); // set timeout to 30 sec.

        statement.executeUpdate("drop table if exists Text");
        statement.executeUpdate("create table Text (md5 string,text string)");

      } catch (Exception e) {
        System.err.println("NMDWSM:" + e.getMessage());
      }
    }
  }

  /**
   * Close the database..
   *
   */
  private void closeDB() {
    if (connection != null) {
      try {
        connection.close();
        connection = null;
      } catch (Exception e) {
        // Forget about it
      }
    }
  }
  // TODO: use non-concatenate method to prevent from SQL-injection
  /**
   * Query whether there is a file whose md5 is the {@code String} .
   * Md5 is not case-sensitive here.
   *
   * @param md5 the md5 value need to be queried.
   * @return whether it is in database.
   */
  public boolean query_exist(String md5) {
    System.out.println("exists is on.");
    md5 = md5.toUpperCase();
    boolean exists = false;

    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from Text where md5='" + md5 + "'");
      exists = rs.next();

      statement.close();
      rs.close();
    } catch (SQLException e) {
      System.err.println("printText:" + e.getMessage());
    }

    return exists;
  }

  /**
   * Upload a file to database.
   *
   * @param md5 the md5 value of the file.
   * @param text the content of the file.
   * @return whether the file's md5 value is new to the database.
   */
  public boolean query_upload(String md5, String text) {
    md5 = md5.toUpperCase();
    System.out.println("upload is on.");

    if (!query_exist(md5)) {
      try {
        Statement statement = connection.createStatement();
        String sqlCmd = "insert into Text values('" + md5 + "','" + text + "') ";
        statement.executeUpdate(sqlCmd);

        statement.close();
      } catch (SQLException e) {
        System.err.println("printText:" + e.getMessage());
      }

      return true;
    }

    System.out.println("upload finds exist.");
    return false;
  }

  /**
   * Download a file from the database.
   * Md5 is not case-sensitive here.
   *
   * @param md5 the target file's md5 value.
   * @return whether it is already in database.
   */
  public String query_download(String md5) {
    System.out.println("download is on.");
    String file = null;

    if (query_exist(md5)) {
      try {
        Statement statement = connection.createStatement();
        String sqlCmd = String.format("select text from Text where md5= '%s'", md5);
        ResultSet rs = statement.executeQuery(sqlCmd);
        file = rs.getString("text");

        statement.close();
        rs.close();
      } catch (SQLException e) {
        System.err.println("printText:" + e.getMessage());
      }
    }

    return file;
  }

  /**
   * Query the previews and md5 of all the files in the database.
   *
   * @return a list whose size is 2. the first element is its md5 values, while the second one is its previews.
   */
  public ArrayList<String>[] query_list() {
    System.out.println("list is on.");
    ArrayList<String>[] arrayLists = new ArrayList[2];
    arrayLists[0] = new ArrayList<>();
    arrayLists[1] = new ArrayList<>();

    try {
      Statement statement = connection.createStatement();
      String sqlCmd = "select * from Text";
      ResultSet rs = statement.executeQuery(sqlCmd);

      while (rs.next()) {
        arrayLists[0].add(rs.getString("md5"));
        String preview = rs.getString("text");
        arrayLists[1].add(preview.substring(0, Math.min(1000, preview.length())));
      }

      statement.close();
      rs.close();
    } catch (SQLException e) {
      System.err.println("printText:" + e.getMessage());
    }
    return arrayLists;
  }

}
