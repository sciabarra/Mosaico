imageNames in docker := Seq(ImageName(s"sciabarra/alpine-abuild:1"))

dockerfile in docker := {
  val buildSh = (baseDirectory.value / "build.sh")
  new Dockerfile {
    from((docker in common).value.toString)
    runRaw("apk -U add alpine-sdk bash python python-dev py-pip nodejs nodejs-dev file linux-headers")
    runRaw("pip install --upgrade setuptools")
    runRaw("pip install pypi-show-urls")
    runRaw(
      s"""
         |adduser -D packager &&
         |addgroup packager abuild &&
         |mkdir -p /var/cache/distfiles /home/packager/.abuild &&
         |chown packager:packager /home/packager/.abuild/ &&
         |echo "packager    ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers &&
         |yes | su - packager -c 'abuild-keygen -a -i ; echo PACKAGER_PRIVKEY=\"/home/packager/y\" >/home/packager/.abuild/abuild.conf'
         |""".stripMargin('|').replace('\n', ' '))
    user("packager")
    copyRaw(buildSh.getAbsolutePath, "/home/packager/")
    entryPoint("/bin/bash", "/home/packager/build.sh")
  }
}
