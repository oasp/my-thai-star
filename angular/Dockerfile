# 1. Build
FROM node:lts AS build
WORKDIR /app
COPY . /app
RUN npm i -g yarn
RUN yarn
RUN yarn build --configuration=docker

# 2. Deploy
FROM nginx:alpine-perl

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/. /var/www/
EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]
