import dao.TextDao;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import service.TextService;

/**
 * server end of the program
 */
public class Server {

  /**
   * main method of server end
   * @param args main arguments
   */
  public static void main(String[] args) {
    TextService service = new TextService(new TextDao());

    Javalin app = Javalin.create(config -> {
      config.registerPlugin(getConfiguredOpenApiPlugin());
    }).start(7001);
    // handle exists
    app.get("/files/exists/:md5", service::handleExists);
    // handle upload
    app.post("/files/:md5", service::handleUpload);
    // handle compare
    app.get("/files/:md51/compare/:md52", service::handleCompare);
    // handle download
    app.get("/files/:md5", service::handleDownload);
    // handle list
    // app.get("/files", service::handleList);
    // app.get("", service::handleList);
    app.get("/", service::handleList);
  }

  /**
   * get configured open API plugin
   * @return a OpenApiPlugin object
   */
  private static OpenApiPlugin getConfiguredOpenApiPlugin() {
    Info info = new Info().version("1.0").description("RESTful Corpus Platform API");
    OpenApiOptions options = new OpenApiOptions(info)
        .activateAnnotationScanningFor("cn.edu.sustech.java2.RESTfulCorpusPlatform")
        .path("/swagger-docs") // endpoint for OpenAPI json
        .swagger(new SwaggerOptions("/swagger-ui")); // endpoint for swagger-ui
    //                .reDoc(new ReDocOptions("/redoc")); // endpoint for redoc
    return new OpenApiPlugin(options);
  }
}