package controllers

import javax.inject.Inject

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import play.api.Logger
import play.api.mvc._
import play.api.libs.streams._

class ActorController @Inject() (implicit system: ActorSystem, materializer: Materializer) {

    def socket = WebSocket.accept[String, String] { request =>
        ActorFlow.actorRef(out => UserActor.props(out))
    }
}

object UserActor {
    def props(out: ActorRef) = Props(new UserActor(out))
}

class UserActor(out: ActorRef) extends Actor {
    def receive = {
        case msg: String =>
            out ! ("I received your message: " + msg)
            Logger.info(s"Received '$msg' from $out")
    }
}
