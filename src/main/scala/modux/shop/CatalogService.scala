package modux.shop

import akka.stream.scaladsl.Source
import modux.core.api.Service
import modux.macros.serializer.SerializationSupport
import modux.macros.serializer.codec.Codec
import modux.model.ServiceDef
import modux.model.context.Context
import modux.model.service.Call
import modux.shop.model.{Item, ShopItem}
import modux.shop.store.ItemRepository

final case class CatalogService(context: Context) extends Service with SerializationSupport {

  implicit val shopItemCodec: Codec[ShopItem] = codecFor[ShopItem]
  implicit val simpleItemCodec: Codec[SimpleItem] = codecFor[SimpleItem]

  def getItems: Call[Unit, Source[ShopItem, Any]] = Call {
    Source(ItemRepository.getItems)
  }

  def getItem(id: Int): Call[Unit, ShopItem] = Call {
    ItemRepository.find(id) match {
      case Some(value) => value
      case None => NotFound(s"Item $id not founded 2")
    }
  }

  def addItem(): Call[SimpleItem, Unit] = { item =>
    ItemRepository.addItem(item)
  }

  def removeItem(id: Int): Call[Unit, Unit] = Call {
    ItemRepository.removeItem(id)
  }

  override def serviceDef: ServiceDef = {
    namedAs("catalog-services")
      .withNamespace("catalog/test")
      .withCalls(
        get("/item", getItems _) summary "Get a list of items" returns (200 -> "OK" represented by[ShopItem]),
        get("/item/:id", getItem _) summary "obtains a user" returns(200 -> "if user with id exists" represented by[Item], 404 -> "if not founded"),
        post("/item", addItem _)
          summary "creates a user"
          expects instanceOf[SimpleItem]
          returns (200 -> "Store a new item"),
        delete("/item/:id", removeItem _)
      )
  }
}
