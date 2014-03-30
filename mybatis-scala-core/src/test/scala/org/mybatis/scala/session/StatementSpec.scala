package org.mybatis.scala.session

import org.scalatest._
import org.mybatis.scala.config.Configuration
import org.mybatis.scala.mapping._
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver

/**
 * An UnitTest for Statement.
 */
class StatementSpec extends FlatSpec {
  case class TestStatement() extends Statement {
    override def parameterTypeClass: Class[_] = getClass
    override def xsql = <xsql>SELECT 1</xsql>
  }

  behavior of "Statement"

  it should "have null value for the languageDriver field as default" in {
    assert(TestStatement().languageDriver === null)
  }

  it should "change languageDriver" in {
    val statement = TestStatement()
    statement.languageDriver = new XMLLanguageDriver()

    assert(statement.languageDriver != null)
    assert(statement.languageDriver.isInstanceOf[XMLLanguageDriver] === true)
  }

  "Statement.languageDriver" should "be set automatically if it is null" in {
    val builder = new Configuration.Builder
    val statement = new Perform {
      def xsql = "SELECT 1"
    }

    builder.statements(statement)

    Configuration(builder)

    assert(statement.languageDriver != null)
  }
}
