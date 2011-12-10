/*
 * Copyright 2011 The myBatis Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mybatis.scala.samples

import org.mybatis.scala.mapping._
import org.mybatis.scala.config._
import org.mybatis.scala.session._

object DeleteSample {

  // Simple Person POJO
  class Person {
    var id : Int = _
    var firstName : String = _
    var lastName : String = _
  }

  // Simple select method
  val findAll = new SelectList[Nothing,Person] {
    def xsql =
      <xsql>
        SELECT id_ as id, first_name_ as firstName, last_name_ as lastName
        FROM person
        ORDER BY id_
      </xsql>
  }

  val findMaxId = new SelectOne[Nothing,Int] {
    def xsql =
      <xsql>
        SELECT MAX(id_) FROM person
      </xsql>
  }

  val deletePersonById = new Delete[Int] {
    def xsql =
      <xsql>
        DELETE FROM person WHERE id_ = #{{id}}
      </xsql>
  }

  val deletePersonContacts = new Delete[Int] {
    def xsql =
      <xsql>
        DELETE FROM contact_info WHERE owner_id_ = #{{id}}
      </xsql>
  }

  // Load datasource configuration
  val config = Configuration("mybatis.xml")

  // Create a configuration space, add the data access method
  config.addSpace("ns1") { space =>
    space += findAll
    space += findMaxId
    space += deletePersonById
    space += deletePersonContacts
  }

  // Build the session manager
  val db = config.createPersistenceContext

  // Do the Magic ...
  def main(args : Array[String]) : Unit = {

    db.transaction { implicit session =>

      println("Before =>")
      for (p <- findAll())
        println( "\tPerson(%d): %s %s".format(p.id, p.firstName, p.lastName) )

      val id = findMaxId()
      deletePersonContacts(id)
      deletePersonById(id)

      println("After =>")
      for (p <- findAll())
        println( "\tPerson(%d): %s %s".format(p.id, p.firstName, p.lastName) )

    }

  }


}
