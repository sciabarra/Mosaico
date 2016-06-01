# Mosaico

A mosaic of docker components, built with Docker and SBT, for use with Scala/Spark applications

This is a set of reusable Docker images, built with sbt.

---

# Opinions

It is a bit opinionated. Here is our list of opinions.

- Based on Alpine. This means you will have a hard time with DockerToolbox since Alpine based images does not work with shared folders.
- Images target Kubernets AND DockerCompose.  I use compose for development and Kubernetes for deploy on clouds
- Uses s6 for managing Processes
- Processes running as root ?

# Layout:

- `/app` for apps
- `/data` for data


Other?
