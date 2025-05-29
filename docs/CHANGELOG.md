---
title: CHANGELOG
layout: default
nav_order: 4
---

# Changelog

This is an automatic changelog based on notable changes to this project, check [release notes](https://github.com/mattiaspersson09/junisert/releases) for additional information.

## 0.1.0 *(2025-05-25)*
**Release notes:** https://github.com/mattiaspersson09/junisert/releases/tag/v0.1.0


### Features

- Support for java internal java.time package [#86](https://github.com/mattiaspersson09/junisert/pull/86) ([b4ff7b](https://github.com/mattiaspersson09/junisert/commit/b4ff7bd2f5a3f634fa1ec8152b0170e897975c93)) 
- Handling argument constructors with configurable dependency depth [#63](https://github.com/mattiaspersson09/junisert/pull/63) ([59c073](https://github.com/mattiaspersson09/junisert/commit/59c0730a04dc9cb1430aa90e2fc1b185e6a41750)) 
- Make reflective operations throw unchecked exception [#53](https://github.com/mattiaspersson09/junisert/pull/53) ([e1fd9f](https://github.com/mattiaspersson09/junisert/commit/e1fd9f5963889587110151c210fdff1643647710)) 
- Making proxies from InterfaceValueGenerator comparable [#51](https://github.com/mattiaspersson09/junisert/pull/51) ([eb1567](https://github.com/mattiaspersson09/junisert/commit/eb1567b4d5c1fbcbc12bff0711186c79f2977ca1)) 
- PlainObjectAssertion and needed refactor and tests [#50](https://github.com/mattiaspersson09/junisert/pull/50) ([f0f18e](https://github.com/mattiaspersson09/junisert/commit/f0f18ef96adcdd5f34d234ca65818103800d7d68)) 
- Prevent field-method collisions when booleans are involved [#49](https://github.com/mattiaspersson09/junisert/pull/49) ([8c4c7d](https://github.com/mattiaspersson09/junisert/commit/8c4c7dc34779f3a8ffb871fdebc49e362f269895)) 
- ImplementsToString unit test [#44](https://github.com/mattiaspersson09/junisert/pull/44) ([b40e3c](https://github.com/mattiaspersson09/junisert/commit/b40e3c66f79993f21ff8bc594dab03f360990ad7)) 
- ImplementsHashCode unit test [#37](https://github.com/mattiaspersson09/junisert/pull/37) ([bb26bd](https://github.com/mattiaspersson09/junisert/commit/bb26bd65689cd75481513f4b7ce8acf2a48e7437)) 
- *(values)* ValueGenerator supporting array values ([450bc4](https://github.com/mattiaspersson09/junisert/commit/450bc458da6bd10897d1cf4177d86438288ed875)) 
- *(values)* ValueGenerator supporting enum values ([292ea7](https://github.com/mattiaspersson09/junisert/commit/292ea745f92de371e0b6772ba0541213633fd2a1)) 
- *(values)* Ability to handle recursive object constructors ([8d2609](https://github.com/mattiaspersson09/junisert/commit/8d26092093afad77b2a42b49e4cc937b489b6757)) 
- *(values)* ValueGenerator generating from parameter constructors ([d3339d](https://github.com/mattiaspersson09/junisert/commit/d3339da153f4cf984152f9ccf71cd433288faf11)) 
- More reflection tests to ensure functionality ([4ae536](https://github.com/mattiaspersson09/junisert/commit/4ae536dfd745ba725898fdb0985ae9afba08d917)) 
- ImplementsEquals unit test to check logic of equals method ([069bf6](https://github.com/mattiaspersson09/junisert/commit/069bf6c27759337f8566a9632bec459dbdd909d2)) 
- HasGetters unit test ([399e1b](https://github.com/mattiaspersson09/junisert/commit/399e1b2d939e05a065ef15c95274bcebcbd5c49a)) 
- HasSetters test for asserting working setters ([cfdb48](https://github.com/mattiaspersson09/junisert/commit/cfdb48184d0eaec42265143de4d3818a8df1d5e8)) 

### Fixed

- Setting null for recursive and cyclic parameters again ([dab82f](https://github.com/mattiaspersson09/junisert/commit/dab82fa0efc61b653fb231e67e7305c7c4f27ef4)) 
- Field -> ReflectionException message ([8f2138](https://github.com/mattiaspersson09/junisert/commit/8f2138b86fdf37324d3268f53359cbd626491423)) 
- Error-prone tests when caching unit instances [#38](https://github.com/mattiaspersson09/junisert/pull/38) ([33b241](https://github.com/mattiaspersson09/junisert/commit/33b241b392255116ffb4219329ee262dc49cdfc3)) 
- HasGetters had unnecessary extra loop ([7789a8](https://github.com/mattiaspersson09/junisert/commit/7789a8d853240ca263198bd02289780a7b97c581)) 
- Handle both nullable and non nullable objects in recursion ([72dcf7](https://github.com/mattiaspersson09/junisert/commit/72dcf75d29443fb5b2380d173f5f2093aab1061f)) 
- Support recursive parameter regardless of helping generator ([a0066f](https://github.com/mattiaspersson09/junisert/commit/a0066fbb2c6d87f2e26b9f2e8ee233d8f48cf6bf)) 

### Refactor

- Reviewing and finishing modules for first publish [#92](https://github.com/mattiaspersson09/junisert/pull/92) ([01bb69](https://github.com/mattiaspersson09/junisert/commit/01bb695b07e9cce14dc06a40a9f206fa19e8cc96)) 
- To accommodate performance and performance testing with JMH [#65](https://github.com/mattiaspersson09/junisert/pull/65) ([8dde68](https://github.com/mattiaspersson09/junisert/commit/8dde684c03d283b047951414ee0a1e9de6d66ee1)) 
- Clean up ([6ec630](https://github.com/mattiaspersson09/junisert/commit/6ec63033723b1909aa970f81a70340e556b5e0f8)) 
- Members have owning unit's name in toString() already ([f02dd1](https://github.com/mattiaspersson09/junisert/commit/f02dd100a29bbf7fca209b3cb8f1c695597875e2)) 
- ImplementsEquals unit test [#54](https://github.com/mattiaspersson09/junisert/pull/54) ([13c688](https://github.com/mattiaspersson09/junisert/commit/13c688c39cbb354f3349847e26e59e50bc312ae0)) 
- Reflection classes and setup, being more functional ([99dd83](https://github.com/mattiaspersson09/junisert/commit/99dd83f743f822f6c4cad789909d4fa8ce7f3494)) 
- Method reflection and tests ([02456d](https://github.com/mattiaspersson09/junisert/commit/02456d60b20eefaaef49422a42374fc2d797b900)) 
- Field reflection with tests ([3b3d40](https://github.com/mattiaspersson09/junisert/commit/3b3d4055b9c95e33c3ff7b651acc42eb75c4e871)) 
- *(api)* Exception -> error on blocking types ([5dbacc](https://github.com/mattiaspersson09/junisert/commit/5dbacc6676b2e5462c3804cb32e49c74212d2f82)) 
- *(api and value)* Api unsupported exception -> error and value module update ([a3940a](https://github.com/mattiaspersson09/junisert/commit/a3940ace540dcc5a721904e2cf14ebf33429317c))
<!-- generated by git-cliff -->
