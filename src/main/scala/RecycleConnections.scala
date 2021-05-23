
import cats.effect.{ExitCode, IO, IOApp}
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.client.asynchttpclient.AsyncHttpClient
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.dsl.io._
import org.http4s.dsl.io.GET
import org.http4s.ember.client.EmberClientBuilder

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.DurationInt


object RecycleConnections extends IOApp {

  def runRequest(client: Client[IO]): IO[Unit] =
    for {
      req <- GET.apply(uri = Uri.unsafeFromString("http://example.com"))
      runAndThrow = client.run(req).use { resp =>
        resp.body.compile.drain *>
          IO.raiseError(new Exception("Unexpected Response"))
      }
      _ <- runAndThrow.handleErrorWith(_ => IO.unit)
    } yield ()

  val blaze = BlazeClientBuilder[IO](global).resource
  val ember = EmberClientBuilder.default[IO].build
  val asyncHttp = AsyncHttpClient.resource[IO]()

  val io =
    for {
      _ <- IO.pure(println("====== Running with AHC ======="))
      _ <- asyncHttp.use(c => runRequest(c) *> runRequest(c))
      _ <- IO.pure(println("====== Finished with AHC ======"))
      _ <- IO.sleep(3.seconds)

      _ <- IO.pure(println("====== Running with Blaze ======="))
      _ <- blaze.use(c => runRequest(c) *> runRequest(c))
      _ <- IO.pure(println("====== Finished with Blaze ======"))
      _ <- IO.sleep(3.seconds)

      _ <- IO.pure(println("====== Running with Ember ======="))
      _ <- ember.use(c => runRequest(c) *> runRequest(c))
      _ <- IO.pure(println("====== Finished with Ember ======"))

    } yield ()

  def run(args: List[String]): IO[ExitCode] =
    io.as(ExitCode.Success)
}
