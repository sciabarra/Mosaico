# `MosaicoDockerPlugin`: Docker Support

Enable them with `enablePlugins(MosaicoDockerPlugin)`

## input `alpBuild` {APKFILE} {APKFILE}

Execute a build of am Alpine package file. `{APKFILE}` must be a valid APKFILE.

Ouput is stored under `target` with the name `{APKFILE}`

Build is executed with the image `sciabarra/alpine-abuild`

## input `download` {url} [{file}] [{header}...]

Download the file pointed from the {url}, saving it in `{file}` (optional).

Default filename is the filename in the url.

You can also specify additional headers to the url request,  useful to add passwords, cookies, etc.

