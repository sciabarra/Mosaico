package mosaico.common

import java.io.{File, FileReader, InputStream}
import java.net.URL

import sbt._

import scala.io.Source

/**
  * File relates utils
  */
trait FileUtils {

  /**
    * Read a whole file as a string
    *
    * @param s
    * @return
    */
  def readFile(s: String): String = {
    readFile(new java.io.File(s))
  }

  /**
    * Read a whole resource as a string
    *
    * @param s
    * @return
    */
  def readResource(s: String): String = {
    readStream(getClass.getResourceAsStream(s))
  }

  /**
    * Read a whole file as a string
    *
    * @param f
    * @return
    */
  def readFile(f: File) = {
    val fr = new FileReader(f)
    val sb = new StringBuilder
    var c = fr.read
    while (c != -1) {
      sb.append(c.asInstanceOf[Char])
      c = fr.read
    }
    fr.close
    sb.toString
  }

  /**
    * Read a stream entirely as a string
    *
    * @param is
    * @return
    */
  def readStream(is: InputStream) = {
    val sb = new StringBuilder
    var c = is.read
    while (c != -1) {
      sb.append(c.asInstanceOf[Char])
      c = is.read
    }
    is.close
    sb.toString
  }

  /**
    * Write a whole file
    *
    * @param file
    * @param body
    * @param log
    */
  def writeFile(file: File, body: String, log: sbt.Logger = null) = {
    //println("*** %s%s****\n".format(file.toString, body))
    if (log != null)
      log.debug("+++ %s".format(file.toString))
    if (file.getParentFile != null)
      file.getParentFile.mkdirs
    val w = new java.io.FileWriter(file)
    w.write(body)
    w.close()
  }

  /**
    * Write a file read as a resource
    *
    * @param file
    * @param resource
    * @param log
    */
  def writeFileFromResource(file: File, resource: String, log: sbt.Logger) = {

    println(">>> %s\n".format(resource))

    val is = this.getClass.getResourceAsStream(resource)
    val sc = new java.util.Scanner(is).useDelimiter("\\A");

    if (sc.hasNext())
      writeFile(file, sc.next, log)
    else
      writeFile(file, "", log)
  }

  /**
    * Replace a file with another
    *
    * @param in
    * @param out
    * @param log
    * @param replace
    */
  def replaceFile(in: File, out: File, log: sbt.Logger = null)(replace: String => String) = {
    println(">>> %s\n".format(out.getAbsolutePath))
    val body = Source.fromFile(in).getLines.map(replace).mkString("\n")
    writeFile(out, body, log)
  }

  /**
    * Replace a file read as a resource
    *
    * @param out
    * @param resource
    * @param log
    * @param replace
    */
  def replaceFileFromResource(out: File, resource: String, log: sbt.Logger)(replace: String => String) = {
    println(">>> %s\n".format(out.getAbsolutePath))
    val is = this.getClass.getResourceAsStream(resource)
    val body = Source.fromInputStream(is).getLines.map(replace).mkString("\n")
    writeFile(out, body, log)
  }

  /**
    * copy files from a src dir to a target dir recursively
    * filter files to copy
    *
    * @param src
    * @param tgt
    * @param log
    * @param sel
    * @return
    */
  def recursiveCopy(src: File, tgt: File, log: Logger)(sel: File => Boolean) = {
    val nSrc = src.getPath.length
    val cpList = (src ** "*").get.filterNot(_.isDirectory).filter(sel) map {
      x =>
        val dest = tgt / x.getPath.substring(nSrc)
        (x, dest)
    }
    if (log != null)
      log.info(s"copying #${cpList.size} files")
    IO.copy(cpList, true).toSeq
  }


  /**
    * Contruct a file using the file part of an url
    *
    * @param base
    * @param url
    * @return
    */
  def fileFromUrl(base: File, url: String): File = {
    //println(s"fileFromUrl ${url}")
    val u = new URL(url)
    val f = new File(u.getFile)
    val name = f.getName
    if (name.length > 0)
      new File(base, name)
    else File.createTempFile("download", ".tmp", base)
  }

  /**
    * Construct a file from a string or if the string is "-" from the provided url
    *
    * @param base
    * @param filename
    * @param url
    * @return
    */
  def fileFromStringOrUrl(base: File, filename: String, url: String): File = {
    if (filename.equals("-"))
      fileFromUrl(base, url)
    else
      new File(base, filename)
  }

}