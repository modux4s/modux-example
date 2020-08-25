package modux.shop.store

import modux.shop.model.User

import scala.collection.mutable

object UserRepository {
  private val store: mutable.ArrayBuffer[User] = mutable.ArrayBuffer.empty

  def addUser(x: User): Unit = store.append(x)

  def removeUser(name: String): Unit = {
    store.clear()
    store.append(store.filterNot(_.name == name): _*)
  }
}
