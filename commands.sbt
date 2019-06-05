val ReleaseCommand = Command.command("release") {
  state =>
    "clean" :: "coverage" :: "test" :: "coverageReport" :: "coverageAggregate" :: "assembly" :: "doc" :: state
}

val ReloadCompileCommand = Command.command("reload-compile") {
  state =>
    "reload" :: "clean" :: "compile" :: state
}

val ReloadTestCommand = Command.command("reload-test") {
  state =>
    "reload" :: "clean" :: "test" :: state
}

commands ++= Seq(
  ReleaseCommand,
  ReloadCompileCommand,
  ReloadTestCommand
)