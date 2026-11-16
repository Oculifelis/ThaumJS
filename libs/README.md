Jars this project compiles against. They are not committed. Copy them here:

- thaumaturge-26.1.2-NeoForge-1.1.0.jar
- kubejs-neoforge-26.1.2-8.0.7.jar
- rhino-2101.2.8-build.91.jar
- better-advanced-tooltips-2601.1.0-build.11.jar (run time only)
- curios-neoforge-15.0.0+26.1.2.jar (run time only)
- lithostitched-1.8.1-neoforge-26.1.jar (run time only)

`gradlew build runGameTestServer` boots a server with `src/autotest/kubejs/server_scripts/thaumjs_test.js` copied into
`run/gametest/kubejs/server_scripts`; its results are the THAUMJS-TEST lines in `run/gametest/logs/kubejs/server.log`.
