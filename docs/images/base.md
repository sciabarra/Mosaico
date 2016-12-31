# Base Image
This is an alpine based base image

It includes `daemontools`, `serf` and `augeas`

## daemontools

Automatically start services putting a run script in /services/<service>

## augeas

Augtool makes easy to edit configuration scripts

## serf

A service discovery kit. The start script is in /usr/bin/serf.sh,

To enable serf:

- copy `serf.sh` in `/services/serf/run`,
- set the `SERF` env variable to point to some well know serf host (defautls to localohst)
