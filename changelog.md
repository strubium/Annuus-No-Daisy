# 1.0.15
* Removed 2 unused Mixins classes. 
* Made the InformationCompressorRegistry use names. allows the config to be more code driven
  * Ex: A addon mod adds some obscure compression algorithms, now they can be directly selected in the `annuus.json` config
* Fixed Lz4Compressor using the length of the compressed message, not the length of the original method like it should
* New compressor option, [Snappy](https://github.com/xerial/snappy-java)


# 1.0.14
* Removed all the unused Apricot/Lilium/Viburnum stuff 
* Replaced ApricotCollectionFactor with direct object creation
* NeoForge: Removed Client and Server mods, everything is done inside the Neoannuus container
* Replaced Fabric/NeoForge loggers with a unified one
* No longer keep track of the "loadingPlatform" using a variable 
* Removed "Catheter" and "Sinuatum"
  * Jar size reduction; 3 MiB → 2.8 MiB

# 1.0.13
First Version

* Removed GitHubPackages maven and replaced with Jitpack
* Updated "Catheter" and "Sinuatum" by the original mod dev to 1.0.46 and 1.0.17 respectively (from 1.0.42 and 1.0.15)
  * Plan it to fully replace these with a more widely known dependency where needed
* Removed `ordinal` from `onPlayerConnect` mixin to make it less brittle  