package controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.{Consumes, POST, Path, Produces}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import javax.inject._
import scala.annotation.meta.getter

@Schema(description = "Add integers request")
case class AddRequest(
  @(Schema @getter)(description = "The integers to add")
  numbers: Seq[Int],
)
object AddRequest {
  implicit val format: OFormat[AddRequest] = Json.format[AddRequest]
}

@Schema(description = "Add integers response")
case class AddResponse(
  @(Schema @getter)(description = "The total sum")
  total: Int,
)
object AddResponse {
  implicit val format: OFormat[AddResponse] = Json.format[AddResponse]
}

@Singleton
class AddController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  @POST
  @Path("/add")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Add integers",
    description = "Calculate the total sum of integer numbers",
    requestBody = new RequestBody(
      required = true,
      content = Array(new Content(schema = new Schema(implementation = classOf[AddRequest]))),
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Add response",
        content = Array(new Content(schema = new Schema(implementation = classOf[AddResponse])))
      ),
      new ApiResponse(responseCode = "500", description = "Internal server error"),
    )
  )
  def add = Action(parse.json[AddRequest]) { implicit request =>
    val AddRequest(numbers) = request.body
    Ok(Json.toJson(AddResponse(numbers.sum)))
  }
}
