package mosaico.common

import java.io.File
import java.net.URL

import mosaico.TestCommon.Slow
import org.scalatest.FunSuite
import org.scalatest.Ignore


/**
  * Created by msciab on 12/09/16.
  */
@Ignore
class DownloadSlowSpec extends FunSuite with Download {
  test("download big file", Slow) {
    val url = new URL("https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.23-r3/glibc-2.23-r3.apk")
    val file = new File("bigfile.apk")
    downloadUrl(url, file)
    assert(file.exists())
    //file.delete()
  }

  test("download jdk with cookie", Slow) {
    val url = new URL("http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz")
    val file = new File("jdk.tgz")
    val cookie = "Cookie: oraclelicense=accept-securebackup-cookie"
    downloadUrl(url, file, Some(cookie))
    assert(file.exists())
    //file.delete()
  }

}
