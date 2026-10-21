# KFSM-VIZ upgrade plan.

This project is written in kotlin and provides a library to be used by a Gradle plugin to generate visualisations of FSMs the will be define by the kfsm project. It uses kotlin-spec and grammar-tools to parse the code so that the FSM actions and predicates in diagram
acturately reflects the code. 
`kotlin-spec` has not been updated after kotlin 1.9 and `grammar-tools` are further behind. I want to change this project to be 2 libraries. one is a kotlin compiler plugin that will detect annotations like:

## Annotation
```kotlin
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class KfsmViz(
val name: String,
val includeActions: Boolean = true,
val includeGuards: Boolean = true,
)
```

## Annotation Usage

```kotlin
class SecureTurnstileFSM(secureTurnstile: SecureTurnstile) {
    companion object {
        @KfsmViz(name = "SecureTurnstile")
        val definition = stateMachine(...) { ... }.build()
    }
}
```

## Modules

### Kotlin Compiler Plugin

This compiler plugin will visit declared annotations and then create a VisualStateMachineDefinition. This definition will be externalized as a JSON file in the same location as where the compiler is writing resources with extension .kfsm
Generate an error/exception when state machine definition is not unique.

### Gradle Plugin

The kfsm-viz gradle plugin which will locate the .kfsm files and then invoke diagram generator providing file names.  

### Diagram Generator

The diagram generator is currently in visualization.kt and doesn't need to change apart from the ability to load a state machine definition from a .kfsm file

### Schema

create a json schema representing the state machine definition and include a formatVersion 


## Design updates

Provide for adding file name to  definition and line/column numbers with elements like transitions, actions and guard expressions so that the visualisation can be used to generate links to the code.


