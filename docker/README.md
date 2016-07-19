# Conventions

The images uses the following path:

- Everything run with root
- Uses s6 for managing Processes,
- `/home/<app>` for app code
- `/var/<app>` for app data
- `/etc/s6/<app>` for run script

# build
Use:

```
../bin/build.sh
```

to build all the images

Use:

```
../bin/build.sh <image-dir>
```

to build a single image (and its dependencies).

# Tests

Use docker-compose -f <file>.yml

to test containers.
