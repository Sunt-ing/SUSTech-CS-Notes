package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.TextDao;
import io.javalin.http.Context;
import util.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static util.FailureCause.*;
import static util.Utils.calculateLevenshteinDistance;
import static util.Utils.calculateSimpleSimilarity;

/**
 * Contains all the services for data manipulations.
 */
public class TextService {

  TextDao dao;

  /**
   * A constructor for TextService class.
   *
   * @param dao used to access database.
   */
  public TextService(TextDao dao) {
    this.dao = dao;
  }

  /**
   * handle exists command
   *
   * @param ctx caller's context, can be used to return response
   */
  public void handleExists(Context ctx) {
    ctx.res.setCharacterEncoding("UTF-8");
    String md5 = ctx.pathParam("md5").toUpperCase();
    Response response;
    if (dao.query_exist(md5)) {
      response = new SuccessResponse();
      response.getResult().put("exists", true);
    } else {
      response = new FailureResponse(FILE_NOT_FOUND);
      response.getResult().put("exists", false);
    }

    ctx.json(response);
  }

  /**
   * handle upload command
   *
   * @param ctx caller's context, can be used to return response
   */
  public void handleUpload(Context ctx) {
    ctx.res.setCharacterEncoding("UTF-8");
    String givenMd5 = ctx.pathParam("md5").toUpperCase();
    String md5 = Utils.calculateMD5(ctx.body().getBytes(StandardCharsets.UTF_8));
    if (!givenMd5.equals(md5)) {
      Response response = new FailureResponse(HASH_NOT_MATCH);
      response.getResult().put("success", false);
      ctx.json(response);
      return;
    }

    if (dao.query_upload(md5, ctx.body())) {
      Response response = new SuccessResponse();
      response.getResult().put("success", true);
      ctx.json(response);
    } else {
      Response response = new FailureResponse(ALREADY_EXIST);
      response.getResult().put("success", false);
      ctx.json(response);
    }
  }

  /**
   * handle compare command
   *
   * @param ctx caller's context, can be used to return response
   */
  public void handleCompare(Context ctx) {
    ctx.res.setCharacterEncoding("UTF-8");
    String md51 = ctx.pathParam("md51").toUpperCase();
    String md52 = ctx.pathParam("md52").toUpperCase();
    Boolean md51Exists = dao.query_exist(md51);
    Boolean md52Exists = dao.query_exist(md52);
    Response response = null;
    if (md51Exists && md52Exists) {
      String str1 = dao.query_download(md51);
      String str2 = dao.query_download(md52);

      response = new SuccessResponse();
      response.getResult().put("simple_similarity", calculateSimpleSimilarity(str1, str2));
      response.getResult().put("levenshtein_distance", calculateLevenshteinDistance(str1, str2));
    } else if (!md51Exists && !md52Exists) {
      response = new Response(4, "File with the " + md51 + " and " + md52 + " do not exist");
      response.getResult().put("success", false);
    } else if (!md51Exists) {
      response = new Response(4, "File with the " + md51 + " does not exist");
      response.getResult().put("success", false);
    } else {
      response = new Response(4, "File with the " + md52 + " does not exist");
      response.getResult().put("success", false);
    }

    ctx.json(response);
  }

  /**
   * handle download command
   *
   * @param ctx caller's context, can be used to return response
   */
  public void handleDownload(Context ctx) {
    ctx.res.setCharacterEncoding("UTF-8");
    String md5 = ctx.pathParam("md5").toUpperCase();
    if (dao.query_exist(md5)) {
      Response response = new SuccessResponse();
      response.getResult().put("content", dao.query_download(md5));
      ctx.json(response);
    } else {
      Response response = new FailureResponse(FILE_NOT_FOUND);
      response.getResult().put("success", false);
      ctx.json(response);
    }
  }

  /**
   * handle list command
   *
   * @param ctx caller's context, can be used to return response
   */
  public void handleList(Context ctx) {
    ctx.res.setCharacterEncoding("UTF-8");
    ArrayList<String>[] arrayLists = dao.query_list();
    List<DocumentPreview> documentPreviews = new ArrayList<>();
    for (int i = 0; i < arrayLists[0].size(); i++) {
      String str = arrayLists[1].get(i);
      String preview = str.substring(0, Math.min(1000, str.length()));
      documentPreviews.add(new DocumentPreview(arrayLists[0].get(i), preview.length(), preview));
    }

    Response response = new SuccessResponse();
    response.getResult().set("files", new ObjectMapper().valueToTree(documentPreviews));

    ctx.json(response);
  }

}
