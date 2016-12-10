#!/bin/sh
cd /home
exec /usr/bin/uwsgi --socket  $(hostname -i):8000 \
 --wsgi-file $DJANGO_APP/wsgi.py\
 --master --processes $UWSGI_PROCESSES --threads $UWSGI_THREADS\
 --stats $(hostname -i):8001
