package controllers

import com.google.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing._
import play.api.routing.sird._

class PostRouter @Inject()(cc: PostController) extends SimpleRouter{
  override def routes: Routes = {
    case POST(p"/phones/createNewPhone") =>
      cc.addNewRecord()
    case POST(p"/phone/$id") =>
      cc.changeValue(id.toInt)
    case GET(p"/phones/searchByName" ? q"nameSubstring=$substring") =>
      cc.search(substring,2)
    case GET(p"/phones/searchByNumber" ? q"numberSubstring=$substring") =>
      cc.search(substring,3)
    case DELETE(p"/phone/$id") =>
      cc.deleteValue(id.toInt)
    case GET(p"/phones") =>
      cc.getAllValues()
  }
}
