import com.typesafe.sbt.SbtScalariform.autoImport.scalariformPreferences
import scalariform.formatter.preferences.{AlignSingleLineCaseStatements, IndentWithTabs}

object Scalariform {

  scalariformPreferences := scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(IndentWithTabs, false)
}
