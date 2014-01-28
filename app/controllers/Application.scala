package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.cache.Cached
import play.api.Play.current


object Application extends Controller with MongoController {

  def collection: JSONCollection = db.collection[JSONCollection]("persons")

  def index = Cached("index") {
    Action.async {

      val futurePeople = collection
        .find(Json.obj())
        .sort(Json.obj("created" -> -1))
        .cursor[JsObject]
        .collect[List](upTo = 50)

      for {
        people <- futurePeople
      } yield Ok(Json.obj("people" -> people))

    }
  }

  def loaderio() = Action {
    Ok("loaderio-92f1a8a29de94e0b0ba549a0860ec0de")
  }
}