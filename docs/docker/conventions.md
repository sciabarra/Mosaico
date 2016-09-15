# Conventions for the Docker Images

The images use the following conventins and paths:

Everything runs as root inside the container

Images `daemontools` for starting processes, and keeping them running.
Basically they never die because if one dies it is automatically restarted.

Standard for paths inside the images:

- `/home/<app>` for app code
- `/var/<app>` for app data
- `/service/<app>` for run script

