# syntax=docker/dockerfile:1.4

FROM --platform=$BUILDPLATFORM node:lts AS build

WORKDIR /code
COPY package.json /code/package.json
COPY package-lock.json /code/package-lock.json

RUN npm ci
COPY . /code

RUN ["npm", "run", "build"]

FROM nginx:1.27.2-alpine

COPY nginx.conf /etc/nginx/nginx.conf

RUN rm -rf /usr/share/nginx/html/*

COPY --from=build /code/dist /usr/share/nginx/html

CMD ["nginx","-g","daemon off;"]