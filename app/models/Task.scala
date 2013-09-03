package models

import com.mongodb.casbah.Imports._

case class Task(id: Long, text: String)

object Task {

  val databaseName = "test"
  val collectionName = "tasks"

  def mongoCollection = {
    val db = MongoClient()(databaseName)
    db(collectionName)
  }

  def task(t: DBObject) = {
    val id = t.as[Any]("id") match {
      case n: Double => n.toLong
      case n: Int => n.toLong
      case _ => throw new Exception("Expected id to be a number")
    }
    val text = t.as[String]("text")
    Task(id, text)
  }

  def all(): List[Task] = {
    mongoCollection.find().map(task).toList
  }

  def create(label: String) {}

  def update(id: Long, text: String) {}

  def delete(id: Long) {}

}
