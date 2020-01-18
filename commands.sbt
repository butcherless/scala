val CoverageCommand = Command.command("xcoverage") { state =>
  "clean" :: "coverage" :: "test" :: "coverageReport" :: "coverageAggregate" :: state
}

val ReleaseCommand = Command.command("xrelease") { state =>
  "clean" :: "coverage" :: "test" :: "coverageReport" :: "coverageAggregate" :: "assembly" :: "doc" :: state
}

val ReloadCompileCommand = Command.command("xrecompile") { state =>
  "clean" :: "reload" :: "compile" :: state
}

val ReloadTestCommand = Command.command("xretest") { state =>
  "clean" :: "reload" :: "test" :: state
}

val DependencyUpdatesCommand = Command.command("xdup") { state =>
  "dependencyUpdates" :: state
}

commands ++= Seq(
  CoverageCommand,
  DependencyUpdatesCommand,
  ReleaseCommand,
  ReloadCompileCommand,
  ReloadTestCommand
)
