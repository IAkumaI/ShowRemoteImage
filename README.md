## ShowRemoteImage

An easy way to show remote images in android's ImageView.

For example, in your Activity:

```java
ImageView image = (ImageView) findViewById(R.id.my_cool_image);
String url = "http://mysite.ur/path/to/image.png";
new ShowRemoteImage(image, url);
```

Image will be cached in the memory by LruCache, so you need 12 SDK version at least.

This is all you need except import this library to your project.