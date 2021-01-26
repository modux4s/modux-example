package services

import modux.core.api.Service
import modux.model.ServiceDef

class WebService extends Service {
  override def serviceDef: ServiceDef = {
    namedAs("Web service")
      .entry(
        statics("app/", "public/")
      )
  }
}
