import $ivy.`org.rauschig:jarchivelib:0.7.0`
import org.rauschig.jarchivelib._
import java.io._

val exclude = "jdk1.8.0_\\d+/(src\\.zip|javafx-src\\.zip.*|db/.*|man/.*|include/.*|lib/(missioncontrol|visualvm)/.*|jre/lib/desktop/.*)".r
val outfile = new File("trimjdk.tar.gz")
val infile = new File("jdk.tgz")
val base = pwd.toIO
val outdir = (pwd/"usr").toIO

if(!infile.exists || outfile.exists) {
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

println("\nArchiving...")
arc.create(outfile.getName, base, outdir)
println("done. Removing...")
rm! pwd/"usr"
println("done")