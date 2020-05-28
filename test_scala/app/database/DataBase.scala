package database

import java.sql._

import models.Data

object DataBase {
  private val url = "jdbc:mysql://f0431629.xsph.ru/f0431629_TestScala?useSSL=false"
  private val username = "f0431629"
  private val password = "xeciutegzu"
  private val tableName = "Data"
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
  def getAllRecords():List[Data] = {
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
  def changeRecord(id : Int, value1: String, value2: String, row1:String, row2:String): Either[String,Unit] = {
    if (!existRecord(id)) Left("Record doesn't exist")
    else {
      var sqlCommand = s"UPDATE ${tableName} SET ${row1} = '${value1}', ${row2} = '${value2}' WHERE id=${id}"
      executeSqlCommand(sqlCommand)
      Right()
    }
  }
  def deleteRecord(id:Int): Either[String,Unit] = {
    if (!existRecord(id)) Left("Record doesn't exist")
    else {
      var sqlCommand = s"DELETE FROM ${tableName} WHERE id=${id}"
      executeSqlCommand(sqlCommand)
      Right()
    }
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

private  def executeSqlCommand(sqlCommand:String): Unit = {
    this.openConnection()
    val statement = connection.createStatement()
    statement.executeUpdate(sqlCommand)
    statement.close()
    this.closeConnection()
  }

private  def existRecord(id:Int):Boolean = {
    val sqlCommand = s"SELECT * FROM ${tableName} WHERE id=${id}"
    this.openConnection()
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(sqlCommand)
    val hasNext = resultSet.next()
    resultSet.close()
    statement.close()
    this.closeConnection()
    hasNext
  }
}
