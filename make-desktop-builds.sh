#!/bin/bash
#!/bin/bash
./gradlew desktop:dist
cp desktop/build/libs/desktop-1.2.0.jar release-builds/desktop-builds/SquareMan-Boy.jar
cd release-builds/desktop-builds/
java -jar packr.jar SMB-Package-Linux32.json
java -jar packr.jar SMB-Package-Linux64.json
java -jar packr.jar SMB-Package-Win32.json
java -jar packr.jar SMB-Package-Win64.json
java -jar packr.jar SMB-Package-Mac.json
cd out/
zip -r SMB-linux32.zip SMB-linux32
zip -r SMB-linux64.zip SMB-linux64
zip -r SMB-win32.zip SMB-win32
zip -r SMB-win64.zip SMB-win64
zip -r SMB-mac.zip SMB-mac
