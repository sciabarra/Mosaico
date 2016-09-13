# Conventions for the Docker Images

# Conventions

The images uses the following path:

- Everything runs with root
- Uses `daemontools` for starting processes,
- `/home/<app>` for app code
- `/var/<app>` for app data
- `/service/<app>` for run script

# Build

From the sbt shell, use:


```
docker
```

to build all the images.

Use:

```
image/docker 
```

to build a single image (and its dependencies).
