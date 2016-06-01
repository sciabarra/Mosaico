imageNames in docker := Seq(
  ImageName("mosaico/base:1")
)
dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    runRaw(s"""
apk update && apk add s6
    """.replace('\n', ' '))
  }
}
