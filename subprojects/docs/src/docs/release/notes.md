The Gradle team is excited to announce Gradle @version@.

This release features [1](), [2](), ... [n](), and more.

We would like to thank the following community contributors to this release of Gradle:
<!-- 
Include only their name, impactful features should be called out separately below.
 [Some person](https://github.com/some-person)
-->

[Mark Nordhoff](https://github.com/MarkNordhoff)

<!-- 
## 1

details of 1

## 2

details of 2

## n
-->

## Upgrade Instructions

Switch your build to use Gradle @version@ by updating your wrapper:

`./gradlew wrapper --gradle-version=@version@`

See the [Gradle 6.x upgrade guide](userguide/upgrading_version_6.html#changes_@baseVersion@) to learn about deprecations, breaking changes and other considerations when upgrading to Gradle @version@. 

For Java, Groovy, Kotlin and Android compatibility, see the [full compatibility notes](userguide/compatibility.html).

<!-- Do not add breaking changes or deprecations here! Add them to the upgrade guide instead. --> 

## Promoted features
Promoted features are features that were incubating in previous versions of Gradle but are now supported and subject to backwards compatibility.
See the User Manual section on the “[Feature Lifecycle](userguide/feature_lifecycle.html)” for more information.

The following are the features that have been promoted in this Gradle release.

### Gradle Dependency Cache can be relocated

With this release, the Gradle Dependency cache, that is the content under `$GRADLE_HOME/caches/modules-2`, can be relocated, for data cached by Gradle version 6.1 and later.
This enables its copy from host to host, allowing to fully leverage all the cached information: artifacts downloaded and metadata parsed.

Note that priming the cache and consuming it needs to use the same Gradle version for maximum effect.
See [the documentation](userguide/dependency_resolution.html#sub:cache_copy) for details on this.

This is one step in helping out ephemeral CI setups where host images can be seeded with dependency cache content, reducing the amout of downloads during the build.

<!--
### Example promoted
-->

## Fixed issues

## Known issues

Known issues are problems that were discovered post release that are directly related to changes made in this release.

## External contributions

We love getting contributions from the Gradle community. For information on contributing, please see [gradle.org/contribute](https://gradle.org/contribute).

## Reporting Problems

If you find a problem with this release, please file a bug on [GitHub Issues](https://github.com/gradle/gradle/issues) adhering to our issue guidelines. 
If you're not sure you're encountering a bug, please use the [forum](https://discuss.gradle.org/c/help-discuss).

We hope you will build happiness with Gradle, and we look forward to your feedback via [Twitter](https://twitter.com/gradle) or on [GitHub](https://github.com/gradle).
