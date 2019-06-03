val ReleaseCommand = Command.command("release") {
  state =>
    "clean" :: "coverage" :: "test" :: "coverageReport" :: "coverageAggregate" :: "assembly" :: "doc" :: state
}

commands += ReleaseCommand