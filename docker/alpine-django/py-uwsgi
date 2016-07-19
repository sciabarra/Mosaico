pkgname=py-uwsgi
_pkgname=uwsgi
pkgver=2.0.13.1
pkgrel=1
pkgdesc="uWsgi for Python"
url="https://uwsgi-docs.readthedocs.io/en/latest/"
arch="all"
license="MIT"
depends="python"
depends_dev=""
makedepends="python-dev py-setuptools"
install=""
subpackages=""
source="https://pypi.python.org/packages/83/22/47b6ff871a5f01b9f660121cf61ba1eccbf7886b5cbe24caacccd0d00d07/uwsgi-2.0.13.1.tar.gz"

_builddir="$srcdir"/$_pkgname-$pkgver
prepare() {
	local i
	cd "$_builddir"
	chmod 644 python2/${_pkgname}.egg-info/*
	for i in $source; do
		case $i in
		*.patch) msg $i; patch -p1 -i "$srcdir"/$i || return 1;;
		esac
	done
}

build() {
	cd "$_builddir"
	python setup.py build || return 1
}

package() {
	cd "$_builddir"
	python setup.py install --prefix=/usr --root="$pkgdir" || return 1
}
