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


object UsersController extends Controller {
	val userForm = Form(
	    mapping(
	        "name" -> text,
	        "id" -> optional(number)
	)(User.apply)(User.unapply))
  	def index = Action.async {
		  val futureUsers = scala.concurrent.Future {Users.all}
		  futureUsers.map(users => Ok(views.html.index(users)))
	}
	def show(id:Int) = Action.async {
		val futureUser = scala.concurrent.Future {Users.find(id)}
		futureUser.map( user => Ok(views.html.users.show(user)))
	}
	def add = Action {
	    Ok(views.html.users.add(userForm))
	}
	def save = Action{implicit request =>
		val user = userForm.bindFromRequest.get
		Users.create(user)
		Redirect(routes.Application.index)
	}
	def edit(id:Int) = Action.async {
		val futureUser = scala.concurrent.Future {Users.find(id)}
		futureUser.map(user => Ok(views.html.users.edit(id, userForm.fill(user))))
	}
	def update(updateid: Int) = Action {implicit request =>
		val user = userForm.bindFromRequest.get
		val newuser = user.copy(id = Some(updateid))
		Users.update(newuser)
		Redirect(routes.Application.index)
	}
	def delete(id: Int) = Action {implicit request =>
		Users.delete(id)
		Redirect(routes.Application.index)
	}
}