# OpenNBT
OpenNBT is a library for reading and writing NBT files, with some extra custom tags added to allow the storage of more data types.

This fork contains various improvements and changes specifically made for use in Via plugins, including but not limited to the following list:
* Most notably, move the tag name out the of tags themselves
* Add primitive getter methods to number types
* Don't wrap values given in Tag#setValue / Tag constructors
* Abstract NumberTag class for easier number handling
* Always read/write CompoundTags in NBTIO
* Don't use reflection when creating tag instances
* Directly use value in clone()
* Implement tag specific equals() methods
* Update to Java 8

## Building the Source
OpenNBT uses Maven to manage dependencies. Simply run 'mvn clean install' in the source's directory.

## License
OpenNBT is licensed under the **[MIT license](http://www.opensource.org/licenses/mit-license.html)**.
