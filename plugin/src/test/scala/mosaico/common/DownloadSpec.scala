package mosaico.common

import java.io.File

import mosaico.TestCommon, TestCommon._
import org.scalatest.FunSuite
import java.net.URL


/**
  * Created by msciab on 12/09/16.
  */
class DownloadSpec extends FunSuite with Download {
  test("download twice small file") {
    val url = new URL("http://s3.amazonaws.com/mosaico-repo/sample.txt")
    //val url = new URL("https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.23-r3/glibc-2.23-r3.apk")
    val file = new File("sample.txt")
    downloadUrl(url, file)
    assert(file.exists())
    assert(file.length() == 12)
    val lastmod = file.lastModified()
    downloadUrl(url, file)
    assert(lastmod == file.lastModified)
    file.delete()
  }

 }
