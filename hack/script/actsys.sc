import $ivy.`com.typesafe.akka::akka-actor:2.3.9`
import $ivy.`com.typesafe.akka::akka-stream-experimental:2.0.3`
import $ivy.`com.typesafe.akka::akka-http-experimental:2.0.3`
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

implicit val as = ActorSystem()
implicit val am = ActorMaterializer()
