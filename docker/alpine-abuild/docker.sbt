imageNames in docker := Seq(ImageName(s"sciabarra/alpine-abuild:1"))

val alpine_s6 = project.in(file("..")/"alpine-s6").enablePlugins(ChartsPlugin)

dockerfile in docker := {
  val buildSh = (baseDirectory.value / "build.sh")
  new Dockerfile {
    from((docker in alpine_s6).value.toString)
    runRaw("apk -U add alpine-sdk bash python python-dev py-pip nodejs nodejs-dev file")
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
    copy(buildSh, "/home/packager/")
    entryPoint("/bin/bash", "/home/packager/build.sh")
  }
}
