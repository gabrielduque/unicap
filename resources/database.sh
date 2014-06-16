#!/bin/bash

adb root &&
adb pull /data/data/com.thm.unicap.app/databases/Unicap.db &&
sqliteman Unicap.db &&
adb shell pm clear com.thm.unicap.app &&
adb push Unicap.db /data/data/com.thm.unicap.app/databases/Unicap.db &&
adb shell am start -n com.thm.unicap.app/com.thm.unicap.app.MainActivity