# Advanced Docker Build

In this section we discuss first the limitations of the Dockerfile format and what you can do to fix them.

## Limitations of the `Dockefile` format

In the past few years I spent a lot of time creating docker images.

After you have worked on this task some time, you will soon realize as the Dockerfile format is very limited *on purpose*.

The real intent of this format is to be able to create repeatable builds that can be used on public internet services like Docker Hub.

By design, it assumes all you need to build your image either exists in the source directory or it can be downloaded from the internet.

However, it is not so easy in practice. You may have a number of needs that are not satisfied by this format. Here is a list the problems I usually face with `Dockerfile`:

- you need to express dependencies between images. You may have a base container and then a number of derived containers built from a base container. And you need to update the base container before you actually build the derived one, without having to do it manually.

- you need to collect artifacts from different sources, not just your build folder. `Dockerfiles` are adamant in the fact everything should be in the source directory. As a result, you need to copy your stuff in the source directory manually.

- you need to be able to download something from Internet,  but avoid to repeat the download all the time (as `Dockerfile` usually do) while developing your build, to avoid getting old while the stuff is downloaded and downloaded again even if it is still there.

- you may need need to be able to add something in your container compiling it but without having to add a whole new development enviroment in your container then removing it after you have compiled your stuff.

You can collect all those requirements in a simple statement: you need a build tools to be run before you actually build your docker images.  

Luckily, SBT is such a build tool and here I show how you can do advanced builds with SBT.

