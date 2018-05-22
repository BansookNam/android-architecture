# todo-svc-kotlin

This version of the app is called todo-svc-kotlin, and provides a foundation for other kotlin samples in this project. The sample aims to:

* Provide a basic [Screen-Views-ControlTower](https://medium.com/@bansooknam/svc-the-better-pattern-against-mvp-66e6d342a23f) (SVC) with svc library dependency.
* Act as a reference point for comparing and contrasting the other kotlin samples in this project.
* Leverage idiomatic kotlin to improve readability and limit verbosity, as compared to the TODO-MVP kotlin project.

# Dependencies
* Kotlin stdlib

* kotlin-android plugin

* SVC library. 

  ```groovy
  implementation "com.naver.android.svc:svc:$0.0.1-alpha5"
  ```

  

## Features

### Complexity - understandability

Medium: You need to [learn](http://kotlinlang.org/docs/reference/) the kotlin language. 

And you need to read what is SVC. Read this [article](https://medium.com/@bansooknam/svc-the-better-pattern-against-mvp-66e6d342a23f)



### Testability

#### Unit testing

Will be updated after finish this project

#### Integration testing

Will be updated after finish this project

### Code metrics

Will be updated after finish this project

The below chart is from (kotlin MVP)

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Kotlin                          48            958           1539           3103 (3901 in MVP java)
XML                             34             95            338            816
-------------------------------------------------------------------------------
SUM:                            82           1053           1877           3919
-------------------------------------------------------------------------------
```
### Maintainability

#### Ease of amending or adding a feature

Really easy.

#### Learning cost

Medium, if you are unfamiliar with kotlin.
