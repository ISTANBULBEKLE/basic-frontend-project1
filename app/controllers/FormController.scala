package controllers

import models.Vehicle
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.libs.json.Json
import play.api.libs.ws
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import javax.inject._
import scala.concurrent.ExecutionContext


@Singleton
class FormController @Inject()(ws: WSClient,
                               cc: ControllerComponents,
                               implicit val ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {


  def simpleForm() = Action {  implicit request: Request[AnyContent] =>
    Ok(views.html.form(BasicForm.form))
  }

  def simpleFormPost() = Action.async{ implicit request =>
    val postData = request.body.asFormUrlEncoded
    val vehicleName = postData.map{args =>
     args("Vehicle Name").head
    }.getOrElse(Ok("Don't work"))

    val dataToBeSend = Json.obj(
      "Vehicle Name" -> s"$vehicleName"
    )

    val futureResult = ws.url(s"http://localhost:9001/receivedForm").post(dataToBeSend)

    futureResult.map { response =>
      val js = Json.fromJson[Vehicle](response.json)
      val veh = js.get
      Ok(views.html.vehicle(veh))
    } recover {
      case _ => NotFound
    }
  }
}

case class BasicForm(name:String)

// this could be defined somewhere else,
// but I prefer to keep it in the companion object
object BasicForm {
  val form: Form[BasicForm] = Form(
    mapping(
//      "wheels"    -> number,
//      "heavy"     -> boolean,
      "name"   -> text

    )(BasicForm.apply)(BasicForm.unapply)
  )
}


