# alpine-djago

Run a django app with wsgi.

App should be placed under /home and need an <app>/wsgi file.

Variables

DJANGO_APP: name of the app (default: hello)
UWSGI_PROCESSES: number of processes (default 4)
UWSGI_THREADS: number of processes (default 2)
