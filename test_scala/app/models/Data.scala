package models

import play.api.data.Form
import play.api.libs.json.{JsPath, JsValue, Json, Reads, Writes}
import play.api.libs.functional.syntax._

case class Data(name:String,number:String)

object Data {
  implicit val dataReader: Reads[Data] =
    ((JsPath \ "name").read[String] and
      (JsPath \ "number").read[String])(Data.apply _)
  implicit val dataWriter = new Writes[Data] {
    def writes (data:Data) = Json.obj(
      "name" -> data.name,
             "number" -> data.number
    )
  }
  val numberFormat = "\\+\\d{1,4} \\d{9,11}".r
}

