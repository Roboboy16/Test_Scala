package database

import java.sql._

import models.Data

object DataBase {
  val url = ""
  private val username = ""
  private val password = ""
  private val tableName = ""
  private var connection : java.sql.Connection = DriverManager.getConnection(url,username,password)
  def openConnection(): Unit = {
    connection = DriverManager.getConnection(url,username,password)
  }
  def closeConnection(): Unit = {
    connection.close()
  }
  def addRecord(value1: String, value2: String, row1:String, row2:String): Unit = {
    var sqlCommand = s"INSERT INTO ${tableName} (${row1},${row2}) VALUES ('${value1}','${value2}')"
    executeSqlCommand(sqlCommand)
  }
  val getAllRecords:List[Data] = {
    var list= List[Data]()
    var sqlCommand = s"SELECT * FROM ${tableName}"
    this.openConnection()
    val statement = connection.createStatement()
    var resultSet = statement.executeQuery(sqlCommand)
    while (resultSet.next()) {
      list =Data(resultSet.getString(2),resultSet.getString(3)) :: list
    }
    resultSet.close()
    statement.close()
    this.closeConnection()
    list.reverse
  }
  def changeRecord(id : Int, value1: String, value2: String, row1:String, row2:String): Unit = {
    var sqlCommand = s"UPDATE ${tableName} SET ${row1} = '${value1}', ${row2} = '${value2}' WHERE id=${id}"
    executeSqlCommand(sqlCommand)
  }
  def deleteRecord(id:Int): Unit = {
    var sqlCommand = s"DELETE FROM ${tableName} WHERE id=${id}"
    executeSqlCommand(sqlCommand)
  }

  def searchBySubstring (subString:String,column:Int): List[Data] = {
    var list = List[Data]()
    var sqlCommand = s"SELECT * FROM ${tableName}"
    this.openConnection()
    val statement = connection.createStatement()
    var resultSet = statement.executeQuery(sqlCommand)
    while (resultSet.next()) {
      if (resultSet.getString(column).contains(subString))
        list =Data(resultSet.getString(2),resultSet.getString(3)) :: list
    }
    resultSet.close()
    statement.close()
    this.closeConnection()
    list.reverse
  }
  def executeSqlCommand(sqlCommand:String): Unit = {
    this.openConnection()
    val statement = connection.createStatement()
    statement.executeUpdate(sqlCommand)
    statement.close()
    this.closeConnection()
  }
}
