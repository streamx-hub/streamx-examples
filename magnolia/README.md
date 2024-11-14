# Magnolia CMS with StreamX

By completing the steps below, you will configure and validate an integration between Magnolia CMS and StreamX.

# Prerequisites

* [StreamX CLI](https://www.streamx.dev/guides/main/streamx-command-line-interface-reference.html)
* Locally cloned git repository
* [nip.io](https://nip.io/) (optional)

# Setup

Follow the instructions under the next sections to configure a local StreamX demo environment with Magnolia CMS as a data source. All of the following commands should be executed from the folder of the cloned repository in which this tutorial is located (./magnolia).

## Magnolia CMS setup

To create our local Magnolia CMS instance, [Magnolia CLI](https://docs.magnolia-cms.com/magnolia-cli/4.x/) will be used.
This has the following prerequisites:
 - at least [Node.js](https://nodejs.org/en) 18.20.4
 - Java 17

 Once everything is set up, Magnolia CLI can be downloaded:

```shell
npm install @magnolia/cli@4.0.12 -g
```

Then we can create the local Magnolia CMS instance:

```shell
mgnl jumpstart -m 6.3.0
```

> The CLI will ask which version should be installed, select the `magnolia-community-webapp` option.

### Install packages

First build the `demo-connector` and the `demo-content` packages:
```shell
mvn clean install -f demo-connector
mvn clean install -f demo-content
```

Add the recently built packages to the Magnolia author instance.
* On Linux:
  ```shell
  cp demo-connector/target/streamx-connector-magnolia-1.0-jar-with-dependencies.jar apache-tomcat/webapps/magnoliaAuthor/WEB-INF/lib/
  cp demo-content/target/demo-content-magnolia-1.0.jar apache-tomcat/webapps/magnoliaAuthor/WEB-INF/lib/
  ```
* On Windows:
  ```bash
  copy "demo-connector\target\streamx-connector-magnolia-1.0-jar-with-dependencies.jar" "apache-tomcat\webapps\magnoliaAuthor\WEB-INF\lib"
  copy "demo-content\target\demo-content-magnolia-1.0.jar" "apache-tomcat\webapps\magnoliaAuthor\WEB-INF\lib\"
  ```

### Change Magnolia CMS port

As StreamX's rest ingestion service is using port 8080, we have to run Magnolia on a different port. To achieve this, edit `apache-tomcat/conf/server.xml` file.

Search for this part in the file:
```shell
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443"
               relaxedQueryChars="[]|" />
```
and change the port value to `8083` or any other port that is not being used on your system.

### Run Mangnolia CMS

Finally Magnolia can be started with the following command:

```bash
mgnl start
```

Once the setup is completed, the Magnolia CMS instance can be accessed at http://localhost:8083 .
Click on `Run the Web update on the author instance`.
The default username and password is `superuser`.

## StreamX setup

Now, the StreamX Mesh can be started. Before executing the following command in a terminal window, also in the root of this repository, make sure that Docker is up and running:

```shell
streamx run
```

# Publish content

The StreamX connector will take care of pushing content to StreamX when you complete the following publication actions:

* In the [JCR Browser](http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:jcr-browser-app:browser), select
  `resources` from the workspace selector dropdown and publish `streamx.css`.
* In the [Assets App](http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:dam:jcrBrowser;::), publish all images.
* In the [Pages App](http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:pages-app:browser;::), publish the
  following pages:
    * Index (/index.html),
    * Blog (/blog.html),
    * Products (/products.html).
* In the [Custom Articles App](http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:articles-app:browser), publish the article.
* In the [Custom Products App](http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:products-app:browser), publish the product.

# Validation

At this point StreamX should have all the Magnolia CMS demo data processed which can be validated by opening the following pages. In the demo we are using `nip.io` to map localhost to a more distinguishable hostname, but using localhost would also work.

* Open http://streamx.127.0.0.1.nip.io/index.html in your web browser and navigate through the links to check blog and product
  landing pages, or the sample blog article and product pages.
* Open http://streamx.127.0.0.1.nip.io/sitemap.xml in your web browser to check the content of the auto-generated sitemap.xml
  file.
* Open http://streamx.127.0.0.1.nip.io/search/byQuery?query=tee in your web browser to validate that hits for the search term `tee`.

## Versions used for testing

Instructions and commands above were tested on:

* `macOS 14.5 (23F79)`,
* `Docker Engine 25.0.3`,
* `Node.js 18.20.4`,
* `Magnolia CLI 4.0.12`,
* `StreamX CLI 0.3.9` and `Magnolia CMS 6.3.0` on `OpenJDK Runtime Environment Temurin-17.0.11+9 (build 17.0.11+9)`.

## Troubleshooting

If you encounter 404 error on magnolia pages like:
http://localhost:8083/magnoliaAuthor/.magnolia/admincentral#app:jcr-browser-app:browser
you need to:
* stop magnolia
* manually delete folders: apache-tomcat, downloads, light-modules
* redo all actions from this readme
