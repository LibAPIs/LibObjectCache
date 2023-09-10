# LibObjectCache

A simple java helper library for managing a cache of objects.

## Maven Dependency

Include the library in your project by adding the following dependency to your pom.xml

```
<dependency>
	<groupId>com.mclarkdev.tools</groupId>
	<artifactId>libobjectcache</artifactId>
	<version>1.5.1</version>
</dependency>
```

## Example

To create custom Cached Objects, simply extend the LibObjectCacheCachedObject class.

```
// Add object to the cache
LibObjectCache.getCache("objects").put("obj-1", new MyCachedObject());

// Recall from cache later
MyCachedObject o = LibObjectCache.getCache("objects").get("obj-1");

// Object details
o.isExpired();
o.getTouchCount();
o.getTimeCreated();
o.getTimeExpires();
```

# License

Open source & free for all. ❤
