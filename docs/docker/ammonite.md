# `MosaicoAmmonitePlugin`: Ammonite Support

enable them with `enablePlugins(MosaicoAmmonitePlugin)`

## input task `amm` {script} {args}...

Execute ammonite shell or a {script} with optional {args}.

Scripts will be located in the `ammScript` key, defaults to the base directory.

It will execute a sensible prefed to initialize the ammonite shell. 

Predef can be changed with the  `ammPredef` key.

## setting `ammScripts`

Settings of the ammonite scripts, defaults to baseDirectory

## setting `ammPredef`

Default prede

## task `ammClasspath`

Classpath used to launch ammonite



