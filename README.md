jolie-yaml
==========

Simple YAML to Jolie value library in development.

## Compilation ##

Download [snakeyaml](https://code.google.com/p/snakeyaml/) and place it in `lib/` as `snakeyaml.jar`,
then do

```
cd jolie-yaml
ant
```

Build file assumes you have Jolie installed in `/opt/jolie`.
Change `build.xml` if you want to use different paths.

## Example ##

```yaml
# john.yaml
---
name: John
age: 27
children:
    - Alice
    - Bob
```

```
// example.ol
include "yaml_utils.iol"
include "console.iol"
include "string_utils.iol"

main {
	parse@YamlUtils("john.yaml")(doc);
	valueToPrettyString@StringUtils(doc)(pretty);
	println@Console(pretty)()
}
```

Usage:

```
$ jolie example.ol
Value
	.children[0]
		.list[0] = Alice : java.lang.String
		.list[1] = Bob : java.lang.String
	.age[0] = 27 : java.lang.Integer
	.name[0] = John : java.lang.String
```
