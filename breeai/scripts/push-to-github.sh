#!/usr/bin/env bash
set -Eeuo pipefail

REPO_URL="${1:-}"
if [[ -z "$REPO_URL" ]]; then
  echo "Pemakaian: bash breeai/scripts/push-to-github.sh https://github.com/USERNAME/REPO.git"
  exit 2
fi
if ! command -v git >/dev/null 2>&1; then echo "ERROR: git belum terpasang. Jalankan: pkg install git"; exit 2; fi
if [[ "$REPO_URL" == https://*@github.com/* ]]; then
  echo "ERROR: jangan menaruh username/token di URL. Gunakan URL GitHub biasa atau SSH."; exit 2
elif [[ "$REPO_URL" == *"@"*:* ]]; then :
elif [[ "$REPO_URL" != https://github.com/* && "$REPO_URL" != ssh://git@github.com/* ]]; then
  echo "ERROR: gunakan URL HTTPS atau SSH repository GitHub yang kamu kontrol."; exit 2
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUNDLE_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
[[ -d "$BUNDLE_ROOT/breeai/android" && -d "$BUNDLE_ROOT/breeai/backend" ]] || { echo "ERROR: struktur bundle tidak lengkap: $BUNDLE_ROOT"; exit 2; }

STAMP="$(date +%Y%m%d-%H%M%S)"
BRANCH="breeai-bootstrap-$STAMP"
TMP_PARENT="${TMPDIR:-$PREFIX/tmp}"
mkdir -p "$TMP_PARENT"
WORK_DIR="$(mktemp -d "$TMP_PARENT/breeai-push.XXXXXX")"
cleanup() { rm -rf "$WORK_DIR"; }
trap cleanup EXIT

if command -v gh >/dev/null 2>&1 && gh auth status >/dev/null 2>&1; then
  gh auth setup-git >/dev/null 2>&1 || true
fi

echo "[1/6] Clone repository tujuan..."
if ! git clone "$REPO_URL" "$WORK_DIR/repo"; then
  echo "ERROR: clone gagal. Cek URL, koneksi, permission, dan autentikasi GitHub."; exit 1
fi
cd "$WORK_DIR/repo"
git checkout -b "$BRANCH"
if [[ -e breeai ]]; then
  echo "ERROR: repository sudah memiliki folder 'breeai/'. Aman dibatalkan agar tidak menimpa isi lama."
  echo "Pilih branch/repo lain atau gabungkan bundle secara manual setelah audit."; exit 3
fi

echo "[2/6] Salin source tanpa secrets..."
cp -a "$BUNDLE_ROOT/breeai" ./breeai
mkdir -p .github/workflows
if [[ -e .github/workflows/breeai-ci.yml ]]; then
  echo "ERROR: .github/workflows/breeai-ci.yml sudah ada; batal agar tidak overwrite."; exit 3
fi
cp -a "$BUNDLE_ROOT/.github/workflows/breeai-ci.yml" .github/workflows/
if [[ ! -e README_BREEAI.md ]]; then cp -a "$BUNDLE_ROOT/README_BREEAI.md" ./README_BREEAI.md; fi

echo "[3/6] Validasi struktur minimum..."
test -f breeai/android/app/build.gradle
test -f breeai/backend/app/main.py
test -f .github/workflows/breeai-ci.yml

echo "[4/6] Commit perubahan..."
git add -f breeai .github/workflows/breeai-ci.yml README_BREEAI.md
git -c user.name='BreeAI Bootstrap' -c user.email='breeai-bootstrap@users.noreply.github.com' commit -m "Add Bree AI Android and backend bootstrap"

echo "[5/6] Push branch $BRANCH ..."
if ! git push -u origin "$BRANCH"; then
  echo "ERROR: push gagal. Source lokal sementara dibersihkan; login/auth remote lalu jalankan ulang."
  echo "Jika repo remote memakai SSH, gunakan URL seperti git@github.com:USERNAME/REPO.git"
  exit 1
fi

echo "[6/6] Selesai: branch telah dikirim."
echo "Branch: $BRANCH"
echo "Berikutnya: buka tab Actions di repository dan cek workflow BreeAI CI."
echo "APK baru bisa diunduh sebagai artifact jika job build Android berhasil."
