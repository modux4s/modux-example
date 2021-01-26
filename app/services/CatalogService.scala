package services

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import codecs.CodecDef
import com.typesafe.scalalogging.LazyLogging
import model.{Item, Metrics, ShopItem, SimpleItem}
import modux.core.api.Service
import modux.model.ServiceDef
import modux.model.service.Call
import store.ItemRepository

import scala.concurrent.Future

class CatalogService extends Service with LazyLogging with CodecDef {

  def getItems: Call[Unit, Source[ShopItem, NotUsed]] = onCall {
    Future(Source(ItemRepository.getItems))
  }

  def sumTotal: Call[Source[ShopItem, Any], Metrics] = extractBody { src =>
    val result = src.runWith(Sink.fold(Metrics(0)) { case (acc, x) => Metrics(acc.total + x.stock) })
    result
  }

  def getItem(id: Int): Call[Unit, ShopItem] = onCall {
    ItemRepository.find(id) match {
      case Some(value) => value
      case None => NotFound(s"Item $id not founded")
    }
  }

  def addItem(): Call[SimpleItem, Unit] = extractBody { item =>
    ItemRepository.addItem(item)
  }

  def removeItem(id: Int): Call[Unit, Unit] = onCall {
    ItemRepository.removeItem(id)
  }

  override def serviceDef: ServiceDef = {
    namedAs("Catalog services")
      .entry(
        namespace("catalog")(
          get("/item", getItems _) summary "Get a list of items" returns (200 -> "OK" represented by[ShopItem]),
          get("/item/:id", getItem _) summary "obtains a item" returns(200 -> "if user with id exists" represented by[Item], 404 -> "if not founded"),
          post("/item/total", sumTotal _),
          post("/item", addItem _)
            summary "creates a user"
            expects instanceOf[SimpleItem]
            returns (200 -> "Store a new item"),
          delete("/item/:id", removeItem _)
        )
      )
  }
}
