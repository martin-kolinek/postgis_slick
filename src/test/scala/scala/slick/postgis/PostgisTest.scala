package scala.slick.postgis

import org.scalatest.FunSuite
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.postgis._
import scala.slick.postgis.srid4326._

class PostgisTest extends FunSuite {
    object TestTable extends Table[(Int, Geometry)]("geometry_table") {
        def id = column[Int]("id", O.PrimaryKey)
        def geom = column[Geometry]("geom")
        def * = id ~ geom
    }

    def clearTables(implicit session:Session) {
        try {
            TestTable.ddl.drop
        } catch {
            case e:Exception => 
        }
    }

    test("DDL works") {
        val db = Database.forURL("jdbc:postgresql:test_postgis_slick", driver="org.postgresql.Driver")
        db.withSession { implicit session:Session => 
            clearTables
            TestTable.ddl.createStatements.foreach(info(_))
            TestTable.ddl.create
            TestTable.ddl.dropStatements.foreach(info(_))
            TestTable.ddl.drop
        }
    }

}
