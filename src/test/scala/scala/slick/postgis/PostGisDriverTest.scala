package scala.slick.postgis

import org.scalatest.FunSuite
import scala.slick.driver.PostgisDriver.simple._
import scala.slick.postgis._

class PostGisDriverTest extends FunSuite {
    object TestTable extends Table[(Int, Geometry)]("geometry_table") {
        def id = column[Int]("id", O.PrimaryKey)
        def geom = column[Geometry]("geom")
        def * = id ~ geom
    }

    object TestTable2 extends Table[(Int, Geometry)]("geometry_table2") with GeoOptions {
        def id = column[Int]("id")
        def geom = column[Geometry]("geom", GO.Srid(3906), GO.GeoType("LineString"))
        def * = id ~ geom
    }

    def clearTables(implicit session:Session) {
        try {
            TestTable.ddl.drop
        } catch {
            case e:Exception => 
        }
        try {
            TestTable2.ddl.drop
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
            TestTable2.ddl.createStatements.foreach(info(_))
            TestTable2.ddl.create
            TestTable.ddl.dropStatements.foreach(info(_))
            TestTable.ddl.drop
            TestTable2.ddl.dropStatements.foreach(info(_))
            TestTable2.ddl.drop
        }
    }

}
