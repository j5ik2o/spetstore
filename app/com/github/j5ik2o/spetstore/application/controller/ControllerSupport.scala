package com.github.j5ik2o.spetstore.application.controller

import org.json4s.DefaultFormats
import com.github.tototoshi.play2.json4s.jackson.Json4s
import play.api.mvc.Controller

trait ControllerSupport extends Controller with Json4s {

  implicit val formats = DefaultFormats

}
