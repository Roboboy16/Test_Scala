package controllers

import database.DataBase
import javax.inject._
import models.Data
import play.api._
import play.api.data.Form
import play.api.libs.json.{JsError, Json}
import play.api.mvc._

import scala.concurrent.Future


@Singleton
class PostController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action{ request=>
    Ok(views.html.index())
  }

  def addNewRecord = Action(parse.json) { request =>
    val dataResult = request.body.validate[Data]
    dataResult.fold(
      errors=> {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        data.number match {
          case Data.numberFormat() =>
            DataBase.addRecord(data.name,data.number,"name","number")
            Ok(Json.obj("message"-> "success"))
          case _ =>
            BadRequest(Json.obj("message" -> "Invalid number"))
        }
      }
    )
  }

  def getAllValues = Action {
    val json = Json.toJson(DataBase.getAllRecords())
    Ok(json)
  }

  def changeValue(id:Int) = Action(parse.json) { request =>
    val dataResult = request.body.validate[Data]
    dataResult.fold(
      errors=> {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        data.number match {
          case Data.numberFormat() =>
            DataBase.changeRecord(id,data.name,data.number,"name","number") match {
              case Left(x) =>
                BadRequest(Json.obj("Error"->x))
              case _ =>
                Ok(Json.obj("message"-> "success"))
            }
          case _ =>
            BadRequest(Json.obj("Error" -> "Invalid number format"))
        }
      }
    )
  }
  def deleteValue(id:Int) = Action(parse.json) { request =>
    val dataResult = request.body.validate[Data]
    dataResult.fold(
      errors=> {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        DataBase.deleteRecord(id) match {
          case Left(x) =>
            BadRequest(Json.obj("Error"->x))
          case _ =>
            Ok(Json.obj("message"-> "success"))
        }
      }
    )
  }

  def search(substring:String,columnToSearch:Int) = Action {
    var founded = DataBase.searchBySubstring(substring,columnToSearch)
    founded match {
      case Nil =>
        BadRequest(Json.obj("message" -> "Didn't find"))
      case _ =>
        val json = Json.toJson(founded)
        Ok(json)
    }
  }
}
