## Internal API disclaimer

Classes will be considered indefinitely unstable and should NOT be directly used by an external user
of this API because they can be changed without proper notice.
Disclaimer should be added to javadoc for every publicly visible class of 
[internal package](src/main/java/io/github/mattiaspersson09/junisert/api/internal):
```java
/**
 * <strong>INTERNAL DISCLAIMER:</strong>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API, 
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity 
 * and when support is dropping for version 8 this will lose visibility.
 * </p>
*/
```