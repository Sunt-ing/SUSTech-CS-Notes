package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * basic respionse class
 */
public class Response {
  int code;
  String message;
  ObjectNode result;

  static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * constructor without arguments
   */
  public Response() {
    this.code = 2000;
    this.message = "message0";
    this.result = objectMapper.createObjectNode();
  }

  /**
   * constructor for Response class
   * @param code response code
   * @param message detailed error meesage
   */
  public Response(int code, String message) {
    this.code = code;
    this.message = message;
    //临时注释
    this.result = objectMapper.createObjectNode();
  }

  /**
   * constructor for Response class
   * @param code response code
   * @param message detailed error meesage
   * @param result result field in Response class
   */
  public Response(int code, String message, ObjectNode result) {
    this.code = code;
    this.message = message;
    this.result = result;
  }

  /**
   * get code from Response
   * @return code
   */
  public int getCode() {
    return code;
  }

  /**
   * get message from Response
   * @return message
   */
  public String getMessage() {
    return message;
  }

  /**
   * get reslut from Response
   * @return result
   */
  public ObjectNode getResult() {
    return result;
  }

  /**
   * set code in Response
   * @param code new code
   * @return this
   */
  public Response setCode(int code) {
    this.code = code;
    return this;
  }

  /**
   * set message in Response
   * @param message new code
   * @return this
   */
  public Response setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * set result in Response
   * @param result new code
   * @return this
   */
  public Response setResult(ObjectNode result) {
    this.result = result;
    return this;
  }

  /**
   * set objectMapper in Response
   * @param objectMapper new code
   */
  public static void setObjectMapper(ObjectMapper objectMapper) {
    Response.objectMapper = objectMapper;
  }
}
