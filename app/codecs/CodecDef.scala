package codecs

import model.{Message, Metrics, ShopItem, SimpleItem, User}
import modux.macros.serializer.SerializationSupport
import modux.macros.serializer.codec.Codec
import modux.model.converter.WebSocketCodec

trait CodecDef extends SerializationSupport{

  implicit val userCodec: Codec[User] = codecFor[User]
  implicit val shopItemCodec: Codec[ShopItem] = codecFor[ShopItem]
  implicit val metricsCodec: Codec[Metrics] = codecFor[Metrics]
  implicit val simpleItemCodec: Codec[SimpleItem] = codecFor[SimpleItem]
  implicit val messageWSCodec: WebSocketCodec[Message, Message] = codecFor[Message, Message]
}
