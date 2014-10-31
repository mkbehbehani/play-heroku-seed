package controllers

import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import scala.slick.driver.PostgresDriver.simple._
import models.User
import models.Users

object Application extends Controller {
  	def index = Action {
		  Ok(views.html.index(Users.all))
	}
}
