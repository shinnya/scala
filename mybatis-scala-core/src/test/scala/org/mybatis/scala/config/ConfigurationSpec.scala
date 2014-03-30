package org.mybatis.scala.config

import org.scalatest._
import org.mybatis.scala.mapping.{SelectOne, Delete, T}
import org.apache.ibatis.scripting.defaults.RawLanguageDriver
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver

/**
 * An UnitTest for Configuration.
 */
class ConfigurationSpec extends FlatSpec {
  behavior of "Configuration"

  it should "use XMLLanguageDriver if defaultScriptingLanguage is not specified" in {
    val builder = new Configuration.Builder()
    val statement = new Delete[Unit] {
      def xsql = <xsql>DELETE * FROM account</xsql>
    }

    builder.statements(statement)

    Configuration(builder)

    assert(statement.languageDriver != null)
    assert(statement.languageDriver.isInstanceOf[XMLLanguageDriver] === true)
  }

  it should "use the specified LanguageDriver" in {
    val builder = new Configuration.Builder()
    val statement = new SelectOne[Unit] {
      def xsql = <xsql>SELECT 1</xsql>
    }

    builder.defaultScriptingLanguage(T[RawLanguageDriver])
    builder.statements(statement)

    Configuration(builder)

    assert(statement.languageDriver != null)
    assert(statement.languageDriver.isInstanceOf[RawLanguageDriver] === true)
  }
}
