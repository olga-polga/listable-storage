# listable-storage
Using Google Cloud Storage

## Run with dev server:  
```
mvn appengine:run -DskipTests=true
```
## Deploy to GCP:
```
mvn appengine:deploy -DskipTests=true
```
## Issues:
- unable to run tests locally
- unable to use blobstore locally outside dev server - confirmed
- unable to get blobkey locally in order to use com.google.appengine.api.images.ImagesService
