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
    val json = Json.toJson(DataBase.getAllRecords)
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
            DataBase.changeRecord(id,data.name,data.number,"name","number")
            Ok(Json.obj("message"-> "success"))
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
        DataBase.deleteRecord(id)
        Ok(Json.obj("message"-> "success"))
      }
    )
  }

  def search(substring:String,columnToSearch:Int) = Action {
    val json = Json.toJson(DataBase.searchBySubstring(substring,columnToSearch))
    Ok(json)
  }
}
