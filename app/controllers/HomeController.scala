package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models.Task
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    Redirect(routes.HomeController.tasks)
  }

  def tasks = Action {
      Ok(views.html.index(Task.all(), taskForm))
  }

  def deleteTask(id: Long) = Action {
  	Task.delete(id)
  	Redirect(routes.HomeController.tasks)
  }

  def completeTask(id: Long) = Action {
        Task.complete(id)
        Redirect(routes.HomeController.tasks)
  }

  val taskForm = Form(
    tuple (
      "label" -> nonEmptyText,
      "who" -> nonEmptyText
    )
  )

  def newTask = Action { implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.index(Task.all(), errors)),
        x=>x match { case(label,who) => {
          // Получаем текущее время
          val today = Calendar.getInstance().getTime()
          val timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
          val time = timeFormat.format(today)
          //----------------------------
          Task.create(label, who, time)
          Redirect(routes.HomeController.tasks)
          }
        }
      )
  }
}
