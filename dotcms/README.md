# dotCMS with StreamX

By completing the steps below, you will configure and validate an integration between dotCMS and StreamX.

# Prerequisites

* TODO: Add link to public "Tutorial: Getting Started with StreamX Command Line Interface (CLI)" tutorial later
* [Docker Compose](https://docs.docker.com/compose/install/)
* Locally cloned git repository 

# Setup

Follow the instructions under the next sections to configure a local StreamX demo environment with dotCMS as a data
source.

> Unless otherwise stated, all terminal commands and paths are relative to the `<repo_checkout>/dotcms` folder

## dotCMS setup

dotCMS and related services can be started by executing the following command in a terminal

```shell
docker compose up -d
```

> The `docker-compose.yml` used for the demo is identical to the official one offered by dotCMS, except
> * a custom dotCMS starter package is used to provide demo Content Types, Assets and Contentlets
> * an extra container network is added for connectivity to StreamX

> See more information about dotCMS and Docker on the official
>
documentation [Getting Started with dotCMS in Docker Containers ](https://www.dotcms.com/docs/latest/getting-started-with-dotcms-in-docker-containers).

## StreamX setup

The StreamX Mesh can be started by executing the following command in a terminal

```shell
streamx run
```

## Network configuration

To enable network communication between dotCMS and the StreamX ingestion service, execute the following command

```shell
docker network connect dotcms-net rest-ingestion
```

Also, depending on your operating system, complete one of the following steps to allow using `streamx.local` domain name
as an alias for `localhost`

* Linux/macOS

  Execute the following command in a terminal

  ```shell
  sudo sh -c 'echo "127.0.0.1 streamx.local" >> /etc/hosts'
  ```

* Windows

  Start any text editor in `Administration` mode, open the `C:\Windows\System32\drivers\etc\hosts` file and add
  the following entry at the end of the file

  ```
  127.0.0.1 streamx.local
  ```

## Connector installation

Follow the next steps to install the demo StreamX connector as a dotCMS plugin

* Navigate to `demo-connector` folder in your terminal and execute one of the following commands, depending on your
  operating system

    * Linux/macOS
      ```shell
      ./mvnw clean install
      ```

    * Windows
      ```shell
      .\mvnw.cmd clean install
      ```
* Log in into [dotCMS](https://local.dotcms.site:8443/dotAdmin) with the default credentials `admin@dotcms.com`/`admin`
* Navigate to [dotCMS - Developers - Plugins](https://local.dotcms.site:8443/dotAdmin/#/c/dynamic-plugins) in your web
  browser and upload the dotCMS plugin `demo-connector/target/streamx-connector-dotcms-1.0.0.jar` built in the previous
  step

> Make sure that the `dev.streamx.streamx-connector-dotcms` plugin is in `Active` state.
> If not, you might use the `Restart` button in the top-right corner to make it started.

> As per default configuration, the connector expects the StreamX ingestion API to be accessible
> through `http://rest-ingestion:8080`.
> This can be changed by configuring a new value for `streamx.host` within
> the `demo-connector/src/main/resources/plugin.properties` before re-building and re-installing the plugin.

# Publish content

The StreamX connector will take care of pushing content to StreamX when you complete the following publication actions

* On [dotCMS - Site Manager - Browser](https://local.dotcms.site:8443/dotAdmin/#/c/site-browser), navigate
  to `application/css` and publish `output.css`
* On [dotCMS - Digital Assets - Images](https://local.dotcms.site:8443/dotAdmin/#/c/c_Images), publish all images
* On [dotCMS - Content - Blog Article](https://local.dotcms.site:8443/dotAdmin/#/c/c_Blog-Article_list), publish the
  blog article
* On [dotCMS - Content - Product](https://local.dotcms.site:8443/dotAdmin/#/c/c_Product_list), publish the product
* On  [dotCMS - Site Manager - Pages](https://local.dotcms.site:8443/dotAdmin/#/pages), publish the following pages
    * Home (/index.html)
    * Blog (/blog.html)
    * Products (/products.html)

# Validation

At this point StreamX should have all the dotCMS demo data processed which can be validated by

* Opening http://streamx.local/index.html in you web browser and navigate through the links to check blog and product
  landing pages, or the sample blog article and product pages
* Opening http://streamx.local/sitemap.xml in your web browser to check the content of the auto-generated sitemap.xml
  file
* Opening http://streamx.local/search?query=Tee in your web browser to validate that hits for the search term `Tee`

## Versions used for testing

Instructions and commands above were tested on:

* `macOS 14.5 (23F79)`
* `Docker Engine 26.1.4` with `containerd 1.6.33` and `runc 1.1.12`
* `Docker Compose version v2.27.1-desktop.1`
* `StreamX CLI 0.3.4` on `OpenJDK Runtime Environment Temurin-17.0.10+7 (build 17.0.10+7)`
