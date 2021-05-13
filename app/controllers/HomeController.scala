package controllers

import models.Vehicle

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.ws._
import play.mvc.Results.status

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, implicit val ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index() = Action{ implicit request:Request[AnyContent] =>
    Ok(views.html.index())
  }

  def vehicle(vehicleName:String): Action[AnyContent]= Action.async{implicit request:Request[AnyContent] =>
      val futureResult = ws.url(s"http://localhost:9001/vehicle/${vehicleName}").get()
        println("This is response " + futureResult)
    futureResult.map { response =>
      //println(response.json.getClass)
      val js = Json.fromJson[Vehicle](response.json)
      val veh = js.get
      Ok(views.html.vehicle(veh))
      //      Ok(response.json)
    }
  }

}

//val futureResult: Future[String] = ws.url(url).get().map { response =>
//  (response.json \ "person" \ "name").as[String]
//  }

//def getById(itemId: Long) = Action {
//  val foundItem = todoList.find(_.id == itemId)
//  foundItem match {
//  case Some(item) => Ok(Json.toJson(item))
//  case None => NotFound
//  }
//  }
