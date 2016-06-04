name := "abuild"

imageNames in docker in ThisBuild := Seq(ImageName(s"${organization.value}/${name.value}:${version.value}"))

lazy val alpine = project.in(file("..") / "alpine").enablePlugins(MosaicoPlugin)

dockerfile in docker := {
  val buildSh = (baseDirectory.value / "build.sh")
  new Dockerfile {
    from((docker in alpine).value.toString)
    runRaw("apk -U add alpine-sdk bash python python-dev py-pip nodejs nodejs-dev")
    runRaw(
      s"""
         |adduser -D packager &&
         |addgroup packager abuild &&
         |mkdir -p /var/cache/distfiles /home/packager/.abuild &&
         |chown packager:packager /home/packager/.abuild/ &&
         |echo "packager    ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers &&
         |yes | su - packager -c 'abuild-keygen -a -i ; echo PACKAGER_PRIVKEY=\"/home/packager/y\" >/home/packager/.abuild/abuild.conf'
         |echo
         |""".stripMargin('|').replace('\n', ' '))

    user("packager")
    copy(buildSh, "/home/packager/")
    entryPoint("/bin/bash", "/home/packager/build.sh")
  }
}
