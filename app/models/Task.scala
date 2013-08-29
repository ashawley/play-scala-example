package models

case class Task(id: Long, text: String)

object Task {

  def all(): List[Task] = Nil

  def create(label: String) {}

  def update(id: Long, text: String) {}

  def delete(id: Long) {}

}
