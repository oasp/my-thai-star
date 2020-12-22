# 1. Build
FROM blairguk/node-sass-alpine:8.9.4 AS build
WORKDIR /app
COPY . /app
RUN npm i -g @angular/cli
RUN apk update && apk add yarn
RUN yarn
RUN ng build --environment=docker --prod --build-optimizer

# 2. Deploy
FROM nginx:latest
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    apache2-utils \
    && rm -rf /var/lib/apt/lists/*
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/. /usr/share/nginx/html
EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]
