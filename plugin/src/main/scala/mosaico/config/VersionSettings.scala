package mosaico.config

import java.nio.charset.Charset
import sbt.Keys._
import sbt._

/**
 * Generate versioning informations in your jar
 */
trait VersionSettings {
  this: AutoPlugin =>

  trait VersionKeys {
    val versionClassName = settingKey[String]("Version Class Name")
    val versionGenerateClass = taskKey[Seq[java.io.File]]("Version Class Generator")
  }
  import MosaicoConfigPlugin.autoImport._

  def generateVersionClass(vClass: String, base: File, name: String, organization: String, version: String): Seq[File] = {
    val date = new java.util.Date()
    val vFile = base / "agilesites" / "version" / (vClass + ".java")
    IO.write(vFile, s"""package mosaico.version;
public class ${vClass} { public static void main(String[] args) {
 System.out.println("Name: ${name}");
 System.out.println("Organization: ${organization}");
 System.out.println("Version: ${version}");
 System.out.println("Date: ${date.toString}");
 System.out.println("Timestamp: ${date.getTime().toString}");
} }""", Charset.forName("UTF-8"), false)
    Seq(vFile)
  }

  val versionSettings = Seq(
    versionClassName := name.value.replace('-', '_').replace('.', '_'),
    packageOptions in(Compile, packageBin) +=
      Package.ManifestAttributes(java.util.jar.Attributes.Name.MAIN_CLASS
        -> s"mosaico.version.${versionClassName.value}"),
    sourceGenerators in Compile += Def.task {
      generateVersionClass(versionClassName.value, (sourceManaged in Compile).value,
        name.value, organization.value, version.value)
    }.taskValue
  )
}

