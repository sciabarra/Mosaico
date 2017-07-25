#!/bin/bash
cp /home/abuild/$1 /home/packager/APKBUILD
cd /home/packager
abuild checksum
abuild -R
sudo cp packages/home/*/*.apk "/home/target/$2"
sudo chown $(stat -c "%u:%g" /home/target) "/home/target/$2"