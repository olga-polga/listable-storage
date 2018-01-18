# listable-storage
Using Google Cloud Storage for storing images

## Run with dev server:  
```
mvn appengine:run
```
## Deploy to GCP:
```
mvn appengine:deploy
```
## Issues:
- must use war format in local dev server
- must exclude android-json from the spring-boot-starter-test
- unable to use blobstore locally outside dev server - confirmed
- @TODO: unable to get blobkey locally in order to use com.google.appengine.api.images.ImagesService
