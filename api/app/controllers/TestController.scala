package controllers

import io.swagger.annotations.{Api, ApiOperation}
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
@Api(value = "test")
class TestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  @ApiOperation(
    produces = "application/json",
    consumes = "application/json",
    httpMethod = "GET",
    value = "fetch user by id"
  )
  def test = Action { Ok }

}
