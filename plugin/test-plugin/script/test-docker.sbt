prpLookup += baseDirectory.value.getParentFile -> "docker"

imageNames in docker := Seq(ImageName(prp.value("alpine.script")))

dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    amm.toTask(" hello")
  }
}
