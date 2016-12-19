package mosaico.support

import sbt._, Keys._, plugins._


object SupportPlugin
  extends AutoPlugin
    with DockerSettings
    with GeneratorSettings {

  object autoImport
    extends DockerKeys
      with GeneratorKeys

  override val projectSettings =
    generatorSettings ++
      dockerSettings

  override def requires =
    JvmPlugin
}
