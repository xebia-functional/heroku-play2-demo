package plugins

import play.api.{Logger, Plugin}
import play.api.libs.json.{JsString, Json}
import java.util.UUID
import controllers.Application
import play.api.Application
import scala.concurrent.Await
import scala.concurrent.duration._
import reactivemongo.bson.BSONObjectID

class DbInitializer(application: Application) extends Plugin {

  override def onStart() {
    import play.api.libs.concurrent.Execution.Implicits._
    Logger.debug("initializeData")
    def collection = Application.collection
    def initialize = for {
      _ <- collection.create()
      _ <- collection.stats() map {
        case stats if stats.count == 0 =>
          Logger.debug("initializing")
          (1 to 50) map { index =>
            collection.insert(Json.obj("_id" -> JsString(BSONObjectID.generate.stringify), "name" -> JsString(index + "_" + UUID.randomUUID().toString)))
          } map { _ =>
            Logger.debug("initialized")
          }
        case _ =>
          Logger.debug("already initialized")
      }
    } yield "done"
    try {
      Await.result( initialize, 1 minute)
    } catch {
      case e : Exception => Logger.error(s"Error initializing database: ${e.getMessage}")
    }
  }

}