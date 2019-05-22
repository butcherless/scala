val ReleaseCommand = Command.command("release") {
    state =>
      "clean" :: "coverage" :: "assembly" :: "coverageReport" :: "coverageAggregate" :: "doc" :: state
  }
  
commands += ReleaseCommand