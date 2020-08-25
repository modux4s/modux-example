package modux.shop.store

import modux.shop.model.{ShopItem, SimpleItem}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object ItemRepository {
  private val items: mutable.ArrayBuffer[ShopItem] = mutable.ArrayBuffer.empty

  def getItems: List[ShopItem] = items.toList

  def addItem(x: SimpleItem): Unit = {
    if (items.isEmpty) {
      items.append(ShopItem(0, x.name, x.description, x.stock))
    } else {
      items.append(ShopItem(items.maxBy(_.id).id + 1, x.name, x.description, x.stock))
    }
  }

  def find(id: Int): Option[ShopItem] = items.find(_.id == id)

  def removeItem(id: Int): Unit = {
    val datum: ArrayBuffer[ShopItem] = items.filterNot(_.id == id)
    items.clear()
    items.append(datum: _*)
  }
}
