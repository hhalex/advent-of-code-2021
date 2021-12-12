import cats.effect._
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
import org.http4s.blaze.client.BlazeClientBuilder
import java.nio.file.Paths
import fs2.text.utf8
import cats.syntax.all._

class Day2 extends AsyncFreeSpec with AsyncIOSpec with Matchers:

  enum Move:
    case Forward(x: Int)
    case Up(x: Int)
    case Down(x: Int)

  private val pat = "(forward|up|down) ([0-9]+)".r
  extension (s: String)
    def parseMove: Either[Throwable, Move] = s match
      case pat("forward", value) => Right(Move.Forward(value.toInt))
      case pat("up", value) => Right(Move.Up(value.toInt))
      case pat("down", value) => Right(Move.Down(value.toInt))
      case els => Left(new Exception(s"Unknown command: '$els'"))


  val commandsStream = fs2.io.file.readAll[IO](Paths.get("src/test/resources/day2Input.txt"), 4096)
    .through(utf8.decode)
    .through(fs2.text.lines)
    .map(_.parseMove)
    .rethrow

  given org.http4s.Charset = org.http4s.Charset.`UTF-8`

  object State:
    val initial = State(0, 0)
  case class State(horizontalPos: Int, depth: Int):
    def product = horizontalPos * depth
    def next(move: Move): State = move match
      case Move.Up(v) => State(horizontalPos, depth - v)
      case Move.Down(v) => State(horizontalPos, depth + v)
      case Move.Forward(v) => State(horizontalPos + v, depth)

  val computeFinalState: fs2.Pipe[IO, Move, State] = _
    .fold(State.initial)((state, move) => state.next(move))

  "1/2" in {
    for
      finalState <- commandsStream.through(computeFinalState).compile.lastOrError
      _ <- IO.println("Final state: " + finalState)
      _ <- IO.println("Multiplication: " + finalState.product)
    yield 1 shouldBe 1
  }


  object State2:
    val initial = State2(0, 0, 0)
  case class State2(horizontalPos: Int, depth: Int, aim: Int):
    def product = horizontalPos * depth
    def next(move: Move): State2 = move match
      case Move.Up(v) => State2(horizontalPos, depth, aim - v)
      case Move.Down(v) => State2(horizontalPos, depth, aim + v)
      case Move.Forward(v) => State2(horizontalPos + v, depth + aim * v, aim)

  val computeFinalState2: fs2.Pipe[IO, Move, State2] = _
    .fold(State2.initial)((state, move) => state.next(move))

  "2/2" in {

    for
      finalState <- commandsStream.through(computeFinalState2).compile.lastOrError
      _ <- IO.println("Final state: " + finalState)
      _ <- IO.println("Multiplication: " + finalState.product)
    yield 1 shouldBe 1
  }

