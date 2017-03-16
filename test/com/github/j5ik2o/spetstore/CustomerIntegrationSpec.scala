import com.github.j5ik2o.spetstore.Inject
import com.github.j5ik2o.spetstore.application.controller.json.CustomerJsonSupport
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import io.circe.parser._
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{PlaySpec, PortNumber}
import org.scalatestplus.play.guice.{GuiceOneAppPerTest, GuiceOneServerPerSuite}
import play.api.libs.circe.Circe
import play.api.libs.ws.WSClient
import play.api.test.Helpers._
import play.test.WithServer

class CustomerIntegrationSpec extends PlaySpec with GuiceOneAppPerTest with CustomerJsonSupport with Inject with Circe with ScalaFutures {

  override val identifierService: IdentifierService = IdentifierService()
  implicit val wsClient: WSClient = inject[WSClient]
  private def createCustomer(port: Int): Long = {
    val body =
      """
    {
      "name": "hoge",
      "sexType": 1,
      "zipCode1": "1000",
      "zipCode2": "001",
      "prefCode": 1,
      "cityName": "hoge",
      "addressName": "fuga",
      "buildingName": "aaa",
      "email": "j5ik2o@gmail.com",
      "phone": "00-0000-0000",
      "loginName": "hoge",
      "password": "fuga"
    }
    """
    implicit val portNumber = PortNumber(port)
    val f = wsUrl(s"/customers").post(parse(body).right.get)
    val r = f.futureValue
    r.status mustBe OK
    val result = parse(r.body).right.get
    result.hcursor.downField("id").as[Long].right.get
  }


  private def updateCustomer(port: Int, id: Long): Long = {
    val body =
      s"""
      {
        "name": "hoge",
        "sexType": 1,
        "zipCode1": "1000",
        "zipCode2": "001",
        "prefCode": 1,
        "cityName": "hoge",
        "addressName": "fuga",
        "buildingName": "aaa",
        "email": "j5ik2o@gmail.com",
        "phone": "00-0000-0000",
        "loginName": "hoge",
        "password": "fuga"
      }
      """
    implicit val portNumber = PortNumber(port)
    val f = wsUrl(s"/customers/${id}").put(parse(body).right.get)
    val r = f.futureValue
    r.status mustBe OK
    val result = parse(r.body).right.get
    result.hcursor.downField("id").as[Long].right.get
  }


  //"CustomerController" should {
    //
    //"create the model" in new WithServer {
    //  createCustomer(port) mustBe > (0)
    //}
    //
    //    "update the model by id" in new WithServer {
    //      val id = createCustomer(port)
    //      updateCustomer(port, id) must not beNull
    //    }
    //
    //    "get the model" in new WithServer {
    //      val id = createCustomer(port)
    //      id must not beNull
    //      val f = WS.url(s"http://localhost:${port}/customers/${id}").get
    //      val r = await(f)
    //      r.status must_== OK
    //    }
    //
    //    "get models" in new WithServer {
    //      val f = WS.url(s"http://localhost:${port}/customers").get
    //      val r = await(f)
    //      r.status must_== OK
    //    }
    //
    //    "delete the model by id" in new WithServer {
    //      val id = createCustomer(port)
    //      id must not beNull
    //      val f = WS.url(s"http://localhost:${port}/customers/${id}").delete()
    //      val r = await(f)
    //      r.status must_== OK
    //    }
    //
  //}

}
