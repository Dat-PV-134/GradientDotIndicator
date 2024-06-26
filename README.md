## Preview

![alt text](https://github.com/Dat-PV-134/GradientDotIndicator/blob/main/preview_2.jpg)

![alt text](https://github.com/Dat-PV-134/GradientDotIndicator/blob/main/preview_1.jpg)

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

## Usage:

```sh
    <com.rekoj134.gradientdotindicator.indicator.GradientDotIndicator
        android:id="@+id/dotsIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:dotsColor="#ACACAC"
        app:dotsCornerRadius="7dp"
        app:dotsSize="7dp"
        app:dotsSpacing="3dp"
        app:dotsWidthFactor="3"
        android:layout_marginStart="12dp"
        app:layout_constraintStart_toStartOf="parent"
        app:progressMode="true"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:selectedDotColorStart="#45BA7B"
        app:selectedDotColorEnd="#48B7DA" />
```

```sh
    dotIndicator.attachTo(viewPager)
```

Circle type: 
```sh
    app:isTypeCircleDot="true"
```

Custom solid color of each dot:
- Number of color must same as number of page in view pager
```sh
     dotIndicator.setListColor(listOf(
            Color.parseColor("#F31414"),
            Color.parseColor("#16DC96"),
            Color.parseColor("#6A12C3")
            )
        )
```

        


