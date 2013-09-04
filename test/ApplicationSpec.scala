package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
  "Application" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }
    
    "send 303 on the index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get
        status(home) must equalTo(SEE_OTHER)
        redirectLocation(home) must beSome.which(_ == "/tasks")

      }
    }

    "render the tasks index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/tasks")).get

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain ("Todo list")
      }
    }

    "require task in form" in {
      val form = controllers.Application.taskForm.bind(Map.empty[String,String])

      form.hasErrors must beTrue
      form.errors.size must equalTo(1)

      form("text").hasErrors must beTrue

      form.value must beNone
    }

    "create new task" in {
      running(FakeApplication()) {

        val badResult = controllers.Application.newTask(FakeRequest())

        status(badResult) must equalTo(BAD_REQUEST)

        val emptyTask = controllers.Application.newTask(
          FakeRequest().withFormUrlEncodedBody("task" -> "")
        )

        status(emptyTask) must equalTo(BAD_REQUEST)
        contentAsString(emptyTask) must contain("This field is required")

        val result = controllers.Application.newTask(
          FakeRequest().withFormUrlEncodedBody("text" -> "Task one")
        )

        status(result) must equalTo(SEE_OTHER)

        val nextUrl = redirectLocation(result) match {
          case Some(s: String) => s
          case _ => ""
        }
        nextUrl must contain("/tasks")

        val home = route(FakeRequest(GET, nextUrl)).get

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain("Task one")

      }
    }

    "delete task" in {
      running(FakeApplication()) {

        route(FakeRequest(GET, "/tasks/1/delete")) must beNone

        val result = route(FakeRequest(POST, "/tasks/1/delete")).get

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/tasks")

      }
    }
  }
}