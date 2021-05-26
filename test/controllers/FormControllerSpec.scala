package controllers

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.http.Status.{NOT_FOUND, OK}
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}
import play.api.test.Helpers.{GET, POST, contentAsString, contentType, defaultAwaitTimeout, route, status, stubControllerComponents, writeableOf_AnyContentAsEmpty}
import play.api.test.{FakeRequest, Injecting}

import scala.concurrent.{ExecutionContext, Future}

class FormControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock: WSClient = mock[WSClient]
  lazy val wsRequest: WSRequest = mock[WSRequest]
  lazy val wsResponse: WSResponse = mock[WSResponse]

  "FormController GET" should {
    "render the simpleForm page from a new instance of controller" in {
      val controller = new FormController(ws, stubControllerComponents(), executionContext)
      val simpleForm = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))

      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include("Vehicle search")
    }

    "render the simpleForm page from the application" in {
      val controller = inject[FormController]
      val simpleForm = controller.simpleForm().apply(FakeRequest(GET, "/simpleForm"))
      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include("Vehicle search")
    }

    "render the simpleForm page from the router" in {
      val request = FakeRequest(GET, "/simpleForm")
      val simpleForm = route(app, request).get
      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include("Vehicle search")
    }
  }

  "FormController POST" should {
    "render the simpleFormPost page from a new instance of controller" in {

      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val simpleFormPost = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 200
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)
      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.successful(wsResponse)

      status(simpleFormPost) mustBe OK
      contentType(simpleFormPost) mustBe Some("text/html")
      contentAsString(simpleFormPost) must include("Vehicle")
    }

    "Failed to render the simpleFormPost page from the application" in {
      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val simpleFormPost = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 404
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)

      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.failed(new Exception)
      status(simpleFormPost) mustBe NOT_FOUND
    }

//    "render the simpleFormPost page from the router" in {
//      val request = FakeRequest(POST, "/simpleForm")
//      val simpleFormPost = route(app, request).get
//
//      status(simpleFormPost) mustBe OK
//      contentType(simpleFormPost) mustBe Some("text/html")
//      contentAsString(simpleFormPost) must include("Vehicle search")
//    }
  }
}
