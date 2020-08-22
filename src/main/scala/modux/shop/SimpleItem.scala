package modux.shop

case class SimpleItem(name: String, description: Option[String], stock: Int)

object SimpleItem {
  def apply(name: String, description: Option[String], stock: Int): SimpleItem = new SimpleItem(name, description, stock)
}
