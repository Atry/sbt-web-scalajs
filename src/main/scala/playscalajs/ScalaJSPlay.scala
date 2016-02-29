package playscalajs

import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

/**
 * Auto-plugin added to Scala.js projects
 */
object ScalaJSPlay extends AutoPlugin {

  override def requires = ScalaJSPlugin

  object autoImport {
    val sourceMappings = Def.settingKey[Seq[(File, String)]]("Mappings of files to their hashed canonical path")
  }
  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    emitSourceMaps in fullOptJS := false,
    sourceMappings := SourceMappings.fromFiles((unmanagedSourceDirectories in Compile).value),
    scalacOptions ++= sourceMappingOptions(sourceMappings.value)
  )

  private def sourceMappingOptions(sourceMappings: Seq[(File, String)]) = for ((file, newPrefix) <- sourceMappings) yield {
    val oldPrefix = file.getCanonicalFile.toURI
    s"-P:scalajs:mapSourceURI:$oldPrefix->$newPrefix/"
  }
}
