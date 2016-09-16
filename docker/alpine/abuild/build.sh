#!/bin/bash
cd /home/packager
sudo chown packager:packager /home/packager/packages
cp $1 APKBUILD
abuild checksum
abuild -R
mv packages/home/*/*.apk packages/$2


