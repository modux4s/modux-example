package modux.shop

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import modux.core.api.Service
import modux.macros.serializer.SerializationSupport
import modux.macros.serializer.codec.Codec
import modux.model.ServiceDef
import modux.model.context.Context
import modux.model.service.Call
import modux.shop.model.{Item, Metrics, ShopItem, SimpleItem}
import modux.shop.store.ItemRepository

import scala.concurrent.Future

final case class CatalogService(context: Context) extends Service with SerializationSupport {

  implicit val shopItemCodec: Codec[ShopItem] = codecFor[ShopItem]
  implicit val metricsCodec: Codec[Metrics] = codecFor[Metrics]
  implicit val simpleItemCodec: Codec[SimpleItem] = codecFor[SimpleItem]

  def getItems: Call[Unit, Source[ShopItem, NotUsed]] = Call.empty {
    Future(Source(ItemRepository.getItems))
  }

  def sumTotal: Call[Source[ShopItem, Any], Metrics] = Call { src =>
    src.runWith(Sink.fold(Metrics(0)) { case (acc, x) => Metrics(acc.total + x.stock) })
  }

  def getItem(id: Int): Call[Unit, ShopItem] = Call.empty {
    ItemRepository.find(id) match {
      case Some(value) => Future(value)
      case None => NotFound(s"Item $id not founded")
    }
  }

  def addItem(): Call[SimpleItem, Unit] = Call { item =>
    Future(ItemRepository.addItem(item))
  }

  def removeItem(id: Int): Call[Unit, Unit] = Call.empty {
    ItemRepository.removeItem(id)
  }

  override def serviceDef: ServiceDef = {
    namedAs("Catalog services")
      .withNamespace("catalog")
      .withCalls(
        get("/item", getItems _) summary "Get a list of items" returns (200 -> "OK" represented by[ShopItem]),
        get("/item/:id", getItem _) summary "obtains a user" returns(200 -> "if user with id exists" represented by[Item], 404 -> "if not founded"),
        post("/item/total", sumTotal _),
        post("/item", addItem _)
          summary "creates a user"
          expects instanceOf[SimpleItem]
          returns (200 -> "Store a new item"),
        delete("/item/:id", removeItem _)
      )
  }
}
