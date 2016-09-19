import $ivy.`org.rauschig:jarchivelib:0.7.0`
import org.rauschig.jarchivelib._
import java.io._

val exclude = "jdk1.8.0_\\d+/(src\\.zip|javafx-src\\.zip.*|db/.*|man/.*|include/.*|lib/(missioncontrol|visualvm)/.*|jre/lib/desktop/.*)".r
val infile = new File("jdk.tgz")
val base = pwd.toIO
val outdir = (pwd/"usr").toIO

if(!infile.exists || outdir.exists) {
  println("input missing or out dir exists - skipping")
  System.exit(0)
}

val arc = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
val str = arc.stream(infile)
var ent = str.getNextEntry

outdir.mkdirs
while (ent != null) {
  val curr = ent.getName
  exclude.findFirstIn(curr) match {
    case Some(_) => print(s"-")
    case None =>
      print("+")
      ent.extract(outdir)
  }
  ent = str.getNextEntry
}
