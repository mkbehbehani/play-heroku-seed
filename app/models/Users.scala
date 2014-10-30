package models

import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.data.Forms._
case class User(name: String, id: Option[Int] = None)
class Users(tag: Tag) extends Table[User](tag, "USERS") {
  // Auto Increment the id primary key column
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  // The name can't be null
  def name = column[String]("NAME", O.NotNull)
  // the * projection (e.g. select * ...) auto-transforms the tupled
  // column values to / from a User
  def * = (name, id.?) <> (User.tupled, User.unapply)
}

object Users {
	val db = play.api.db.slick.DB
	val users = TableQuery[Users]
	def all: List[User] = db.withSession { implicit session =>
		users.sortBy(_.id.asc.nullsFirst).list
	}
	def create(newuser: User) = db.withTransaction{ implicit session =>
		users += newuser
	}
	def find(userId: Int): User = db.withSession{ implicit session =>
		users.filter(_.id === userId).first
	}
	def update(updateUser: User) = db.withTransaction{ implicit session =>
		users.filter(_.id === updateUser.id).update(updateUser)
	}
	def delete(id: Int) = db.withTransaction{ implicit session =>
		users.filter(_.id === id).delete
	}
}