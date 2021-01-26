package model

sealed trait Item{
  def id: Int
}

final case class ShopItem(id: Int, name: String, description: Option[String], stock: Int) extends Item

final case class CarItem(id: Int, model: String, description: Option[String], stock: Int) extends Item