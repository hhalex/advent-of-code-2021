import cats.effect._
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
import org.http4s.blaze.client.BlazeClientBuilder
import java.nio.file.Paths
import fs2.text.utf8
import cats.syntax.all._

class Day1 extends AsyncFreeSpec with AsyncIOSpec with Matchers:

  val measuresStream = fs2.io.file.readAll[IO](Paths.get("src/test/resources/day1Input.txt"), 4096)
    .through(utf8.decode)
    .through(fs2.text.lines)
    .map(_.toInt)

  val countIncreases: fs2.Pipe[IO, Int, Int] = _
    .sliding(2)
    .fold(0)((total, c) => total + (if (c(0) < c(1)) then 1 else 0))

  given org.http4s.Charset = org.http4s.Charset.`UTF-8`

  "1/2" in {
    for
      count <- measuresStream.through(countIncreases).compile.lastOrError
      _ <- IO.println("Nb increases: " + count)
    yield 1 shouldBe 1
  }

  "2/2" in {
    for
      count <- measuresStream.sliding(3).map(_.fold).through(countIncreases).compile.lastOrError
      _ <- IO.println("Nb increases: " + count)
    yield 1 shouldBe 1
  }
