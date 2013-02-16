package scala.slick.postgis

import org.scalatest.FunSuite
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.postgis._
import scala.slick.postgis.implicits._
import scala.slick.postgis.srid4326._

class PostgisTest extends FunSuite {
    object TestTable extends Table[(Int, Geometry)]("geometry_table") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
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

    val db = Database.forURL("jdbc:postgresql:test_postgis_slick", driver="org.postgresql.Driver")

    test("DDL works") {
        db.withSession { implicit session:Session => 
            clearTables
            TestTable.ddl.createStatements.foreach(info(_))
            TestTable.ddl.create
            TestTable.ddl.dropStatements.foreach(info(_))
            TestTable.ddl.drop
        }
    }

    test("makepoint and x works") {
        db.withSession { implicit session:Session =>
            clearTables
            TestTable.ddl.create
            TestTable.geom.insert(Query(makePoint[Geometry](1.0, 2.0)))
            TestTable.geom.insert(Query(makePoint[Geometry](2.0, 3.0)))
            assert(Query(TestTable.length).first == 2)
            val s = (for {
                t<-TestTable
            } yield t.geom.x).to[Set]
            assert(s==Set(1.0, 2.0))
        }
    }

    test("retrieving geometry column works") {
        db.withSession { implicit session:Session =>
            clearTables
            TestTable.ddl.create
            TestTable.geom.insert(Query(makePoint[Geometry](1.0, 2.0)))
            val geom = (for (t<-TestTable) yield t.geom).first
            TestTable.geom.insert(geom)
            assert(Query(TestTable.length).first == 2)
            assert((for(t<-TestTable) yield t.geom.x).list.filter(_==1.0).size==2)
        }
    }

    test("makeline, point accessors and intersects work") {
        db.withSession { 
            implicit session:Session =>
            clearTables
            TestTable.ddl.create
            TestTable.geom.insert(Query(makePoint[Geometry](1.0, 1.0)))
            TestTable.geom.insert(Query(makePoint[Geometry](2.0, 2.0)))
            TestTable.geom.insert(Query(makePoint[Geometry](2.0, 1.0)))
            TestTable.geom.insert(Query(makePoint[Geometry](1.0, 2.0)))

            def q(p1:Int, p2:Int) = for {
                pt1 <- TestTable if pt1.id === p1
                pt2 <- TestTable if pt2.id === p2
            } yield pt1.geom lineTo pt2.geom

            TestTable.geom.insert(q(1,2))
            TestTable.geom.insert(q(3,4))
            
            assert(TestTable.filter(_.id === 5).map(_.geom.start.x).first == 1.0)
            assert((for {
                l1 <- TestTable if l1.id === 5
                l2 <- TestTable if l2.id === 6
            } yield l1.geom intersects l2.geom).first)
        }
    }

}
