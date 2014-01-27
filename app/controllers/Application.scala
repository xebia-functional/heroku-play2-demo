package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection


object Application extends Controller with MongoController {

  def collection: JSONCollection = db.collection[JSONCollection]("persons")

  def index = Action.async {

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