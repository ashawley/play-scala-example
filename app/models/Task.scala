package models

import com.mongodb.casbah.Imports._

case class Task(id: Long, text: String)

object Task {

  val databaseName = "test"
  val collectionName = "tasks"

  def mongoCollection = {
    val uri = MongoClientURI(sys.env("MONGOHQ_URL"))
    val db = MongoClient(uri)(databaseName)
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

  def create(text: String) {
    val tasks = mongoCollection
    val orderBy = MongoDBObject("id" -> 1)
    val maxId = tasks.find().sort(orderBy).limit(1).map(task).toList(0).id
    val nextId = maxId + 1
    val rec = MongoDBObject("id" -> nextId.toDouble, "text" -> text)
    tasks.insert(rec)
  }

  def update(id: Long, text: String) {}

  def delete(id: Long) {
    val tasks = mongoCollection
    val where = MongoDBObject("id" -> id)
    tasks.remove(where)
  }

}
