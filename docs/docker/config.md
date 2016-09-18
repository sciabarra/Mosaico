# `MosaicoConfigPlugin`: configuration support

Mosaico supports property configuration files that can be used in the builds.

The task `prp` returs a map of the properties read from property files, 
so you can get a named property (a string) in your build with:

`prp.value("property.name")`

Default configuration files are placed the top level folder of your file, 
and are in order:

- `mosaico.dist.properties`
- `mosaico.properties`
- `mosaico.local.properties`

The first one is meant to be included in the distribution providing defaults.
The second is the actual man configuration file.
The third one is meant to be used locally and not to be shared with others.

You can see the whole configuration and configuration files with `prpDump`

# Profile support

You can actually use an additional property file named:

- `mosaico.${profile}.properties`

where `${profile}` is the value of the System property `profile`,
only when it is available. The profiled property file is loaded last, after other files.
 
You can set the profile from the `sbt` startup command (with for example `sbt -Dprofile=devel`),
or you can dynamicall change the profile with the `profile` command name.

Profile switch will set the `profile` sytem property and reload the property files including the profiled one.

# Adding property files

Where and which property files 
You can change or add your own property files with the setting:

```
prpLookup ++= Seq(file("prp")->"myprp")
```

Default prefix is `mosaico`, if you add your own the system will process 
also your prefixes, including a profile if there is one. 

So if have a profile `devel`,  it will process also

`demo.dist.properties`
`demo.properties`
`demo.local.properties`
`demo.devel.properties`
