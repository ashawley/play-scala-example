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
    Task(t.as[Double]("id").toLong,
         t.as[String]("text"))
  }

  def all(): List[Task] = {
    mongoCollection.find().map(task).toList
  }

  def create(label: String) {}

  def update(id: Long, text: String) {}

  def delete(id: Long) {}

}
