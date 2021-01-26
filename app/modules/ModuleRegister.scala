package modules

import modux.core.api.{ModuleX, Service}
import modux.model.context.Context
import services.{CatalogService, ChatService, UserService, WebService}

 class ModuleRegister extends ModuleX {
  override def providers: Seq[Service] = Seq(
    wire[WebService],
    wire[CatalogService],
    wire[UserService],
    wire[ChatService],
  )
}
