#!/bin/bash
#!/bin/bash
./gradlew html:dist
cp -a html/build/dist release-builds/html-dist/
