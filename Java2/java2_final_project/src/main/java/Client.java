import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import util.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * client end of thre program
 */
public class Client {

  static String endpoint = "http://localhost:7001/files";
  static String listAddr = "http://localhost:7001";

  /**
   * operation class
   */
  enum Operation {
    UPLOAD, DOWNLOAD, COMPARE, EXISTS, LIST
  }

  /**
   * parse the command string to operation
   * @param op command string
   * @return one enum class of Operation
   */
  public static Operation parseOperation(String op) {
    switch (op) {
    case "UPLOAD":
      return Operation.UPLOAD;
    case "DOWNLOAD":
      return Operation.DOWNLOAD;
    case "COMPARE":
      return Operation.COMPARE;
    case "EXISTS":
      return Operation.EXISTS;
    case "LIST":
      return Operation.LIST;
    }
    return null;
  }

  /**
   * main method
   * @param args user arguments
   * @throws IOException when operation cannot be handled well
   */
  public static void main(String[] args) throws IOException {

    while (true) {
      Scanner in = new Scanner(System.in);
      args = in.nextLine().split("\\s+");

      if (args.length > 3) {
        System.out.println("Simple Client");
        printUsage();
        continue;
      }

      Operation operation = parseOperation(args[0].toUpperCase());
      if (operation == null) {
        System.err.println("Unknown operation");
        printUsage();
        continue;
      }

      switch (operation) {
      case UPLOAD:
        handleUpload(args);
        break;
      case DOWNLOAD:
        handleDownload(args);
        break;
      case COMPARE:
        handleCompare(args);
        break;
      case EXISTS:
        handleExists(args);
        break;
      case LIST:
        handleList();
        break;
      }
    }
  }

  /**
   * handle exists command
   * @param args user input args
   * @throws IOException when args cannot be handled well
   */
  private static void handleExists(String[] args) throws IOException {
    String responseString = Request.Get(endpoint + "/exists" + "/" + args[1])
                                   .execute().returnContent().asString();
    ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Object> response =
        (Map<String, Object>) objectMapper.readValue(responseString, Map.class);
    Map<String, Object> result = (Map<String, Object>) response.get("result");

    System.out.println("Is the file exists? " + result.get("exists"));
  }

  /**
   * handle compare command
   * @param args user input args
   * @throws IOException when args cannot be handled well
   */
  private static void handleCompare(String[] args) throws IOException {
    System.out.println("handlecompare开始跑了" + endpoint + "/" + args[1] + "/compare" + args[2]);
    String responseString = Request.Get(endpoint + "/" + args[1] + "/compare/" + args[2]).execute().returnContent().asString();
    ObjectMapper objectMapper = new ObjectMapper();

    System.out.println("这个response string是: \n" + responseString);
    Map<String, Object> response = (Map<String, Object>) objectMapper.readValue(responseString, Map.class);
    if ((int) response.get("code") != 0) {
      System.out.println((String) response.get("message"));
      return;
    }

    Map<String, Object> result = (Map<String, Object>) response.get("result");
    System.out.println("Simple Similarity is " + result.get("simple_similarity"));
    System.out.println("Levenshtein distance is " + result.get("levenshtein_distance"));
  }

  /**
   * handle download command
   * @param args user input args
   * @throws IOException when args cannot be handled well
   */
  private static void handleDownload(String[] args) throws IOException {
    String responseString = Request.Get(endpoint + "/" + args[1]).execute().returnContent().asString();
    ObjectMapper objectMapper = new ObjectMapper();

    // System.out.println("这个response string是: \n" + responseString);
    Map<String, Object> response = (Map<String, Object>) objectMapper.readValue(responseString, Map.class);
    if ((int) response.get("code") != 0) {
      System.out.println((String) response.get("message"));
      return;
    }

    Map<String, Object> result = (Map<String, Object>) response.get("result");
    String addr = args[1]+".txt";
    if (args.length == 3) addr = args[2];
    FileOutputStream fos = new FileOutputStream(addr);
    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
    osw.write((String) result.get("content"));
    osw.flush();

    System.out.println("Write over.");
    // System.out.println("Content is " + (String) result.get("content"));
  }

  /**
   * handle upload command
   * @param args user input args
   * @throws IOException when args cannot be handled well
   */
  private static void handleUpload(String[] args) throws IOException {
    Path path = Paths.get(args[1]);
    byte[] bytes = Utils.getUtf8Bytes(Files.readAllBytes(path));
    String md5 = Utils.calculateMD5(bytes);
    String responseString = Request.Post(endpoint + "/" + md5)
                                   .bodyByteArray(bytes).execute().returnContent().asString();
    // responseString = Request.Post(endpoint + "/" + md5)
    //                         .bodyString(new String(bytes), StandardCharsets.UTF_8).execute().returnContent().asString();
    ObjectMapper objectMapper = new ObjectMapper();

    // System.out.println("这个response string是: \n" + responseString);
    Map<String, Object> response = (Map<String, Object>) objectMapper.readValue(responseString, Map.class);
    if ((int) response.get("code") != 0) {
      System.out.println((String) response.get("message"));
      return;
    }

    System.out.println("Uploaded successfully.");
  }

  /**
   * handle list command
   * @throws IOException when args cannot be handled well
   */
  private static void handleList() throws IOException {
    // String responseString = Request.Get(endpoint+"/files").execute().returnContent().asString();
    String responseString = Request.Get(listAddr).execute().returnContent().asString();
    ObjectMapper objectMapper = new ObjectMapper();

    // System.out.println("这个response string是: \n" + responseString);
    Map<String, Object> response = (Map<String, Object>) objectMapper.readValue(responseString, Map.class);
    if ((int) response.get("code") != 0) {
      System.out.println((String) response.get("message"));
      return;
    }

    Map<String, Object> result = (Map<String, Object>) response.get("result");
    List<Map<String, Object>> documentPreviews = (List<Map<String, Object>>) result.get("files");
    if (documentPreviews != null && documentPreviews.size() != 0) {
      System.out.println("Previews:");
      for (Map<String, Object> documentPreview : documentPreviews) {
        System.out.println(
            "md5:" + documentPreview.get("md5") +
            "  length:" + documentPreview.get("length") +
            "  preview:" + documentPreview.get("preview"));
      }
    } else
      System.out.println("Database is empty now.");
  }

  /**
   * print usage of user input
   */
  private static void printUsage() {
    System.out.println("Usage: [op] [params]");
    System.out.println("Available Operation: upload, download, compare, exists, list");
  }

}
