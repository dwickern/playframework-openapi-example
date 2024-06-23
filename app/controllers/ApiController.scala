package controllers

import io.swagger.v3.core.util.Json
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import play.api.mvc._

import javax.inject._
import scala.jdk.CollectionConverters._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ApiController @Inject()(val controllerComponents: ControllerComponents, assets: Assets) extends BaseController {

  def swaggerJson = Action {
    // create OpenAPI spec with any desired customizations
    val openApi = new OpenAPI()

    // alternatively, use an @OpenAPIDefinition annotation on one of the api classes
    openApi.setInfo(new Info().title("Play Framework + OpenAPI v3 example"))

    // read OpenAPI annotations using Java reflection
    val reader = new Reader(openApi)
    val apiClasses: Set[Class[_]] = Set(classOf[AddController])
    val result = reader.read(apiClasses.asJava)

    // serialize to json
    val swaggerJson = Json.pretty.writeValueAsString(result)
    Ok(swaggerJson)
  }

  def swaggerUiRedirect = Action {
    Redirect(controllers.routes.ApiController.swaggerUiIndex)
  }
  def swaggerUiIndex = swaggerUiAsset("index.html")
  def swaggerUiAsset(file: String) = assets.at("/public/lib/swagger-ui", file)

  // this is used to customize swagger-ui
  def swaggerInitializer = assets.at("/javascripts/swagger-initializer.js")
}
