package modux.shop

import modux.core.api.Service
import modux.model.ServiceDef
import modux.model.context.Context

case class WebService(context:Context) extends Service {
  override def serviceDef: ServiceDef =
    namedAs("web service")
      .withCalls(
        statics("app", "public")
      )
}
