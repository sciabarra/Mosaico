# `MosaicoDockerPlugin`: Docker Support

Enable them with `enablePlugins(MosaicoDockerPlugin)`

## input task `alpineBuild` {ABUILDIMAGE} {APKFILE} {APKFILE}

Execute a build of am Alpine package file. 

`{APKFILE}` must be a valid APKFILE and be stored in a subfolder named `abuild`

Ouput is stored under `target` with the name `{APKFILE}`

Build is executed with the image `{ABUILDIMAGE}` that should exepect
as first arguent the build file and second argument the output file.

Note that if any argument starts with "@", 
its actual value is looked up in the configured [`prp`](config.md) setting.


## input `download` {url} [{file}] [{header}...]

Download the file pointed from the {url}, saving it in `{file}` (optional).

Default filename is the filename in the url.

You can also specify additional headers to the url request,  
useful to add passwords, cookies, etc.

Note that if any argument starts with "@", 
its actual value is looked up in the configured [`prp`](config.md) setting.

## input `unzip` {source} {outdir} [-{regex} | +{regex}]

Unzip the {source} file in the {outdir}, excluding (-) or including  (+) paths identified by the {regex}

{source} should be a zip file.

## input `untar` {source} {outdir} [-{regex} | +{regex}]

Untar (and gunzip) the {source} file in the {outdir}, excluding (-) or including  (+) paths identified by the {regex}

{source} should be a tar.gz file.