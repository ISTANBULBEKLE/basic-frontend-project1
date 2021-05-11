package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def string(element: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.string(element))
  }

  def checkVehicle(vehicle: String) = Action { implicit request: Request[AnyContent] =>
    print("Input is " + vehicle)
    vehicle match{
      case "car" =>  Ok(views.html.checkVehicle("this is a car"))
      case "bike" =>  Ok(views.html.checkVehicle("this is a bike"))
      case _ => Ok(views.html.checkVehicle("Please specify a vehicle"))
    }
  }
}
