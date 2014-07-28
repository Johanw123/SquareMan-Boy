#!/bin/bash
#!/bin/bash
./gradlew android:assembleRelease
cd android/build/apk
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore "johanw123.keystore" -storepass vegas123 -keypass vegas123 "android-release-unsigned.apk" johanw123
./zipalign -f -v 4  android-release-unsigned.apk  SquareMan-Boy.apk
cp SquareMan-Boy.apk ../../../release-builds/SquareMan-Boy.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore "johanw123.keystore" -storepass vegas123 -keypass vegas123 "android-release-unsigned.apk" johanw123
./zipalign -f -v 4  android-release-unsigned.apk  SquareMan-Boy-Ouya.apk
cp SquareMan-Boy-Ouya.apk ../../../release-builds/SquareMan-Boy-Ouya.apk
