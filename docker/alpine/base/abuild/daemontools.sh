# Maintainer: Natanael Copa <ncopa@alpinelinux.org>
# Fixed by Michele Sciabarra <michele@sciabarra.com>
pkgname=daemontools
pkgver=0.76
pkgrel=1
pkgdesc="Collection of tools for managing UNIX services"
url="http://cr.yp.to/daemontools.html"
arch="all"
license="public-domain"
depends=
# The makefile need GNU cat for 'cat -v'
makedepends="coreutils"

source="http://cr.yp.to/daemontools/$pkgname-$pkgver.tar.gz"

_builddir="$srcdir"/admin/$pkgname-$pkgver/src

prepare() {
	cd "$_builddir"
	sed -i -e 's/extern int errno;/#include \<errno.h\>/' error.h
	patch -p1 <<EOF
Fixup misc warnings

Patch by RiverRat

http://bugs.gentoo.org/124487

--- src/chkshsgr.c
+++ src/chkshsgr.c
@@ -1,10 +1,13 @@
 /* Public domain. */

+#include <sys/types.h>
+#include <stdlib.h>
+#include <grp.h>
 #include <unistd.h>

 int main()
 {
-  short x[4];
+  gid_t x[4];

   x[0] = x[1] = 0;
   if (getgroups(1,x) == 0) if (setgroups(1,x) == -1) _exit(1);
--- src/matchtest.c
+++ src/matchtest.c
@@ -1,3 +1,4 @@
+#include <unistd.h>
 #include "match.h"
 #include "buffer.h"
 #include "str.h"
--- src/multilog.c
+++ src/multilog.c
@@ -1,3 +1,4 @@
+#include <stdio.h>
 #include <unistd.h>
 #include <sys/types.h>
 #include <sys/stat.h>
--- src/prot.c
+++ src/prot.c
@@ -1,5 +1,8 @@
 /* Public domain. */

+#include <sys/types.h>
+#include <unistd.h>
+#include <grp.h>
 #include "hasshsgr.h"
 #include "prot.h"

--- src/seek_set.c
+++ src/seek_set.c
@@ -1,6 +1,7 @@
 /* Public domain. */

 #include <sys/types.h>
+#include <unistd.h>
 #include "seek.h"

 #define SET 0 /* sigh */
--- src/supervise.c
+++ src/supervise.c
@@ -1,3 +1,4 @@
+#include <stdio.h>
 #include <unistd.h>
 #include <sys/types.h>
 #include <sys/stat.h>
--- src/pathexec_run.c
+++ src/pathexec_run.c
@@ -1,5 +1,6 @@
 /* Public domain. */

+#include <unistd.h>
 #include "error.h"
 #include "stralloc.h"
 #include "str.h"
EOF
}

build() {
	cd "$_builddir"
	echo "${CC:-"gcc"} ${CFLAGS}" > conf-cc
    echo "${CC:-"gcc"} ${LDFLAGS}" > conf-ld
    echo >home
	make PATH="/usr/bin:/bin" || return 1
}

package() {
	local f
	cd "$_builddir"
	mkdir -p "$pkgdir"/usr/bin "$pkgdir"/service
	for f in $(cat ../package/commands); do
		cp $f "$pkgdir"/usr/bin/$f
	done
	# launched directly
	#install -Dm755 "$srcdir"/svscan.initd "$pkgdir"/etc/init.d/svscan
}

md5sums="1871af2453d6e464034968a0fbcb2bfc  daemontools-0.76.tar.gz"
