# Junisert

[![CI](https://github.com/mattiaspersson09/junisert/actions/workflows/ci.yaml/badge.svg)](https://github.com/mattiaspersson09/junisert/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/mattiaspersson09/junisert/branch/main/graph/badge.svg?token=SEE3IT4F86)](https://codecov.io/gh/mattiaspersson09/junisert)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A scalable and flexible framework used as complement to standard Java unit assertion for different kind of units.

**Currently supported Java LTS:** 8+

---

**Currently supported unit assertions:**
- [x] POJO's ("Plain Old Java Object"), flexible units without a clear convention
- [x] Java Bean compliance of a unit

**Planned future support:**
- [ ] Ability to register own values to inject into constructors, fields and methods during assertion
- [ ] Ability to register blocks for types you or your team has decided to avoid, for convention or security reasons
- [ ] Asserting immutability
- [ ] Asserting Mapper classes
- [ ] Asserting Builder classes

*Do you have an idea for features or support that sounds in-line with this project? 
[Create an issue](https://github.com/mattiaspersson09/junisert/issues/new), and it will be 
reviewed and considered for future versions.*

## Project information

This project were created from a need and desire to test structural boilerplate code with or without behavior, with 
continuous support where users should have ability to register their own support without needing to wait for 
new support for Java types.

This project **starts with supporting Java from version 8** but **should** be able to be used 
with newer LTS, to accommodate different teams which are not equally up to date with the current LTS.

**This project is not created to replace well established Java unit testing frameworks and libraries**
(i.e. *JUnit*, *TestNG*, *AssertJ*), but rather give a helping hand
with testing boilerplate code in units to ensure structural integrity and behavior.

*Credit goes to [OpenPojo](https://github.com/OpenPojo/openpojo) and [pojo-tester](https://github.com/sta-szek/pojo-tester)
for laying groundwork and inspiration that sparked the birth of this project.*

## Goals

- Be free from external code dependencies to not clutter users code base when it's not needed.
- Be a complement to users already favorite testing frameworks.
- Provide broad support for values during testing.
- Able to test units regardless of field- or method-conventions, different teams might have their own conventions.
- Give the user ability to register their own prioritized dependency values when predefined support isn't enough,
to avoid getting stuck when testing.
- Give the user ability to block value types during assertion.
- Guarantee 100% code coverage when asserting on boilerplate code.



