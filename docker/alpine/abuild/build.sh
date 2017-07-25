#!/bin/bash
cp /home/abuild/$1 /home/packager/APKBUILD
cd /home/packager
abuild checksum
abuild -R
cp packages/home/*/*.apk /home/target/$2
