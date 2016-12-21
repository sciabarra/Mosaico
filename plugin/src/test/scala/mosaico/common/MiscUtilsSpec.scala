package mosaico.common

import org.scalatest.{Matchers, FunSuite}

/**
  * Created by msciab on 20/12/16.
  */
class MiscUtilsSpec
  extends FunSuite
    with Matchers
    with MiscUtils {

  test("simple") {
    includeExcludeRegex("a",
      "+[a-z]", "-[b-d]", "+[a-d]") shouldBe true
    includeExcludeRegex("b",
      "+[a-z]", "-[b-d]", "+[a-d]") shouldBe false
    includeExcludeRegex("c",
      "+[a-z]", "-[b-d] ", "+[a-d] ") shouldBe false
  }

  test("degraded") {
    includeExcludeRegex("a") shouldBe true
  }
}
