#!/bin/sh
tail -f /var/log/nginx/access.log &
exec /usr/sbin/nginx
