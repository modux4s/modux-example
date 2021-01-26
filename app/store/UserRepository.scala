package store

import model.User

import scala.collection.mutable

object UserRepository {
  private val store: mutable.ArrayBuffer[User] = mutable.ArrayBuffer.empty

  def addUser(x: User): Unit = store += x

  def removeUser(name: String): Unit = {
    store.clear()
    store ++= store.filterNot(_.name == name)
  }
}
