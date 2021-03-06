package ua.kata;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class NestedJoinsTest {

  private NestedJoins<Integer, String> nestedJoins;

  @BeforeEach
  void setUp() {
    nestedJoins = new NestedJoins<>();
  }

  @Test
  void resultStreamIsEmpty_whenJoinLeftTwoEmptyStreams() throws Exception {
    nestedJoins.leftJoin(Flux.empty(), Flux.empty())
        .as(StepVerifier::create)
        .verifyComplete();
  }

  @Test
  void resultStreamHasNullsOnLeft_whenJoinLeftNonemptyAndEmptyStreams() throws Exception {
    nestedJoins.leftJoin(
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "l1"))
                .next(new ImmutablePair<>(2, "l2"))
                .next(new ImmutablePair<>(3, "l3"))
                .complete()
        ),
        Flux.empty()
    ).as(StepVerifier::create)
        .expectNext(new ImmutableTriple<>(1, "l1", null))
        .expectNext(new ImmutableTriple<>(2, "l2", null))
        .expectNext(new ImmutableTriple<>(3, "l3", null))
        .verifyComplete();
  }

  @Test
  void resultStreamHasValues_forCorrespondingKeys_whenJoinLeftTwoNonemptyStreams() throws Exception {
    nestedJoins.leftJoin(
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "l1"))
                .next(new ImmutablePair<>(2, "l2"))
                .next(new ImmutablePair<>(3, "l3"))
                .complete()
        ),
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "r1"))
                .next(new ImmutablePair<>(3, "r3"))
                .complete()
        )
    ).as(StepVerifier::create)
        .expectNext(new ImmutableTriple<>(1, "l1", "r1"))
        .expectNext(new ImmutableTriple<>(2, "l2", null))
        .expectNext(new ImmutableTriple<>(3, "l3", "r3"))
        .verifyComplete();
  }

  @Test
  void resultStreamIsEmpty_whenJoinRightTwoEmptyStreams() throws Exception {
    nestedJoins.rightJoin(Flux.empty(), Flux.empty())
        .as(StepVerifier::create)
        .verifyComplete();
  }

  @Test
  void resultStreamHasNullsOnLeft_whenJoinRightEmptyAndNonemptyStreams() throws Exception {
    nestedJoins.rightJoin(
        Flux.empty(),
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "r1"))
                .next(new ImmutablePair<>(2, "r2"))
                .next(new ImmutablePair<>(3, "r3"))
                .complete()
        )
    ).as(StepVerifier::create)
        .expectNext(new ImmutableTriple<>(1, null, "r1"))
        .expectNext(new ImmutableTriple<>(2, null, "r2"))
        .expectNext(new ImmutableTriple<>(3, null, "r3"))
        .verifyComplete();
  }

  @Test
  void resultStreamHasValuesOnLeft_forCorrespondingKeys_whenJoinRightTwoNonemptyStreams() throws Exception {
    nestedJoins.rightJoin(
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "l1"))
                .next(new ImmutablePair<>(3, "l3"))
                .complete()
        ),
        Flux.create(
            emitter -> emitter
                .next(new ImmutablePair<>(1, "r1"))
                .next(new ImmutablePair<>(2, "r2"))
                .next(new ImmutablePair<>(3, "r3"))
                .complete()
        )
    ).as(StepVerifier::create)
        .expectNext(new ImmutableTriple<>(1, "l1", "r1"))
        .expectNext(new ImmutableTriple<>(2, null, "r2"))
        .expectNext(new ImmutableTriple<>(3, "l3", "r3"))
        .verifyComplete();
  }
}
