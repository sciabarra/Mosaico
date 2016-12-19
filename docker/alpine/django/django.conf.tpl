daemon off;

worker_processes 1;

events {
  worker_connections 1024;
}

error_log stderr ;


http {
  include       mime.types;
  default_type  application/octet-stream;
  sendfile on;
  access_log /var/log/nginx/access.log;

  upstream django {
    server %DJANGO_HOST%:8000;
  }

  server {
    listen 80;
    server_name %NGINX_HOST%;
    charset utf-8;
    client_max_body_size 75M;

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
      root html;
    }

    location /static {
      root /home;
    }

    location /media {
      root /home;
    }

    location / {
      uwsgi_pass django;
      include /etc/nginx/uwsgi_params;
    }
  }
}
