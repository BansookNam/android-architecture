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
  implementation "com.naver.android.svc:svc-generic:1.0.0-android-28-beta07"
  ```


## Features

### Complexity - understandability

Medium: You need to [learn](http://kotlinlang.org/docs/reference/) the kotlin language. 

And you need to read what is SVC. 

1. Read this [article](https://medium.com/@bansooknam/svc-the-better-pattern-against-mvp-66e6d342a23f)
2. Read "readme.md" from [SVC repository](https://github.com/naver/svc)

### Testability

#### Unit testing

Same as TODO-MVP.
You can change lifeCycle of Screen menually for test (see: [TasksFragmentTest.kt](https://github.com/BansookNam/android-architecture/blob/todo-svc-kotlin/todoapp/app/src/test/java/com/example/android/architecture/blueprints/todoapp/tasks/TasksFragmentTest.kt))

#### Integration testing

Will be updated after finish this project

### Code metrics

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Kotlin                          37            397           553           1452 (1538 in todo-mvp-kotlin)
XML                             31             85            291            556 (608 in todo-mvp-kotlin)
-------------------------------------------------------------------------------
SUM:                            68           482           844           2008
-------------------------------------------------------------------------------
```
### Maintainability

#### Ease of amending or adding a feature

Pretty much Same or less then TODO-MVP. (Do not need interface for presenter or view, instead need interface for "viewsAction")

#### Learning cost

Medium, if you are unfamiliar with kotlin.
