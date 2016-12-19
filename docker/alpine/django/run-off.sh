#!/bin/sh
NGINX_HOST=$(hostname -f)
sed \
 -e "s/%DJANGO_HOST%/$DJANGO_HOST/g" \
 -e "s/%NGINX_HOST%/$NGINX_HOST/g" \
 </etc/nginx/django.conf.tpl \
 >/etc/nginx/django.conf
touch /var/log/nginx/access.log
tail -f /var/log/nginx/access.log &
exec /usr/sbin/nginx -c /etc/nginx/django.conf
