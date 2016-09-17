#!/bin/bash
sudo chown packager:packager /home/packager/packages
cp /home/abuild/$1 /home/packager/APKBUILD
cd /home/packager
abuild checksum
abuild -R
mv packages/home/*/*.apk packages/$2


