## Internal API disclaimer

Classes inside the *internal* package will be considered indefinitely unstable. They should NOT be directly
used by an external user of this API since they can be changed without proper notice.
Disclaimer is also added to javadoc for every publicly visible class of the internal package:
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