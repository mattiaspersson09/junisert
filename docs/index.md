---
title: Overview
layout: home
---

# Junisert overview
{: .no_toc }

## Table of Contents
{: .no_toc .text-delta }

1. Supported Java LTS
2. Modules
3. Getting started using Junisert
{:toc}

This documentation offers both technical information and a user guide on how to start using Junisert.

## Supported Java LTS

The project is currently built for **Java 8** and higher, but developed using **Java 21** for convenience, to be able to
use newer Gradle- and plugin versions. The project is compiled by version 21 using the `--release 8` option
and force version 8 bytecode. The reason support is starting at version 8 is to have broad support for teams 
not being equally up-to-date with the LTS yet. The framework can still be used for testing with newer LTS versions.

## Modules

### Junisert API

Module [junisert-api](https://github.com/mattiaspersson09/junisert/tree/main/junisert-api) is the main API 
of the project, the other modules implements this module.
Unlike how other projects might be set up, this project uses the API as it's contract and an abstraction of the other 
modules rather than be used on its own, **junisert-core** is a direct implementation and brings life to the API.

The API are designed as a *fluent API* in mind and if you are familiar with the AssertJ library, you might recognize this 
style of testing. Where the assertions are more expressive, and you can chain call and keep asserting from the very first to last operation.

Because of support for Java 8, and it's lack of modularity, the internal API is visible under package `internal` 
but will be considered indefinitely unstable for direct usage by external users. It can be changed, moved or removed 
and might do so without proper notice. A disclaimer ([DISCLAIMER.md](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/DISCLAIMER.md))
is added to the module and all public classes are documented with this information.

### Junisert Core

Module [junisert-core](https://github.com/mattiaspersson09/junisert/tree/main/junisert-core) implements the API and 
provides value support from **junisert-values**, when looking up values to inject in to `fields`, `methods` and `constructors`.
This module provides entry point for assertions using the main class `Junisert`, both for convenience and familiarity,
all assertions will be set up from this class.
`Junisert` abstracts out the implementation of the inner workings of the framework, setting up and injecting default resources 
when needed during assertions. When testing, only specifying this module is needed, the API and value support is aggregated 
as dependency.

### Junisert Values

Module [junisert-values](https://github.com/mattiaspersson09/junisert/tree/main/junisert-values) implements the value 
API and offers value support when testing. This module offers support for internal **Java types from version 8** out of the box,
but also other useful values like: `interface` proxies, `arrays`, `enums`, `instances` from constructors, `primitives`
and `wrapper primitive` types. All values except primitives from this module are lazily constructed, and will only be constructed 
when they are needed.

This module can be used on its own together with the API if that's only what you want,
however, the project will only focus on supporting what the whole framework needs.
See [ValueGenerator](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/src/main/java/io/github/mattiaspersson09/junisert/api/value/ValueGenerator.java)
and
[Value](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/src/main/java/io/github/mattiaspersson09/junisert/api/value/Value.java)
since they are the back-bone when testing.

### Junisert Common

Module [junisert-common](https://github.com/mattiaspersson09/junisert/tree/main/junisert-common) is only internally supported 
and is only used to share common solutions in the project (which does not belong in the API). This is 
usually utilities and logging capabilities that all modules might need.

## Getting started using Junisert

### Maven

```xml
<dependency>
    <groupId>io.github.mattiaspersson09</groupId>
    <artifactId>junisert-core</artifactId>
    <version>0.1.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```groovy
testImplementation("io.github.mattiaspersson09:junisert-core:0.1.0")
```