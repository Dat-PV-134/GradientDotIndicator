## Preview
![alt text](https://github.com/Dat-PV-134GradientDotIndicator/blob/main/preview_1.jpg)
![alt text](https://github.com/Dat-PV-134GradientDotIndicator/blob/main/preview_2.jpg)

## Implementation:
Project setting gradle:

```sh
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
}
```

Build.gradle (Module app):

```sh
    implementation("com.github.Dat-PV-134:GradientDotIndicator:1.0.1")
```
