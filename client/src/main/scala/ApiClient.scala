import autowire._
import org.scalajs.dom.ext.Ajax

object ApiClient extends autowire.Client[String, upickle.Reader, upickle.Writer]{
    def doCall(req: Request) = {
        Ajax.post(
            url = s"/hotels/api/".replaceAll("//","/") + req.path.mkString("/"),
            data = upickle.write(req.args)
        ).map(_.responseText)
    }
    def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
    def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}