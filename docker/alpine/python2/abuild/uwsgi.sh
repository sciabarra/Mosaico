# Contributor:
# Maintainer:
pkgname=uwsgi
pkgver=2.0.15
pkgrel=1
pkgdesc="uWSGI"
url="https://uwsgi-docs.readthedocs.io/en/latest/"
arch="all"
license="MIT"
depends="python"
makedepends="python-dev"
install=""
subpackages=""
source="https://pypi.python.org/packages/bb/0a/45e5aa80dc135889594bb371c082d20fb7ee7303b174874c996888cc8511/uwsgi-2.0.15.tar.gz"
builddir="$srcdir/$pkgname-$pkgver"

build() {
	cd "$builddir/"
	python setup.py build || return 1
}

package() {
	cd "$builddir"
	python setup.py install --prefix=/usr --root="$pkgdir"
}
