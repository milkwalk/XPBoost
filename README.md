# XPBoost
Updated version of XPBoost spigot plugin.

Download avaliable on https://www.spigotmc.org/resources/xp-boost.13537/


## Maven
You will need to have Dubcat public repository added to your `pom.xml` under `<repositories>`

```xml
<!-- Dubcat Repository -->
<repository>
  <id>dubcat-repo</id>
  <url>http://maven.apps.dubcat.cz/repository/dubcat-public/</url>
</repository>
```

Add dependency to the `<dependencies>` list

```xml
<!--XPBoost-->
<dependency>
  <groupId>cz.dubcat.plugins</groupId>
  <artifactId>xpboost</artifactId>
  <version>VERSION</version>
  <scope>provided</scope>
</dependency>
```
