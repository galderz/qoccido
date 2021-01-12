# Cocido QCC

# Requirements

Build [QCC](https://github.com/quarkuscc/qcc):

```bash
git clone git@github.com:quarkuscc/qcc.git
cd qcc
mvn clean install -DskipTests
```

Build [QCC class library](https://github.com/quarkuscc/qcc-class-library):

```bash
git clone --recurse-submodules git@github.com:quarkuscc/qcc-class-library.git
cd qcc-class-library
mvn clean install -DskipTests
cd /opt
ln -s ${HOME}/.../qcc-class-library/java.base/target qccrt
```

Download [JBang](https://github.com/jbangdev/jbang).

Makefiles require Make 4 or higher.

# Run

```bash
make clean && make
```

# Tips and Tricks

See 
[here](https://www.usna.edu/Users/cs/wcbrown/courses/F19SI413/lec/l19/lec.html)
for ways to tweak the LLVM IR.a