#!/usr/bin/env bash
set -Eeuo pipefail
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")/../android" && pwd)"
cd "$HERE"

if ! command -v java >/dev/null 2>&1; then
  echo "ERROR: Java tidak ditemukan. Pasang JDK 17+ di Termux (mis. pkg install openjdk-17)."; exit 2
fi
java -version 2>&1 | head -n 1
if [[ -z "${ANDROID_HOME:-}${ANDROID_SDK_ROOT:-}" && ! -f local.properties ]]; then
  echo "CATATAN: Android SDK belum terdeteksi lewat ANDROID_HOME/ANDROID_SDK_ROOT atau local.properties."
  echo "Build lokal Termux sering terkendala SDK; jalur paling stabil untuk ZIP ini adalah GitHub Actions."
fi
if [[ -x ./gradlew ]]; then
  ./gradlew --no-daemon clean assembleDebug
elif command -v gradle >/dev/null 2>&1; then
  echo "Membuat Gradle Wrapper 9.6.0 menggunakan Gradle terpasang..."
  gradle wrapper --gradle-version 9.6.0 --distribution-type bin
  ./gradlew --no-daemon clean assembleDebug
else
  echo "ERROR: Gradle tidak ditemukan. Gunakan GitHub Actions, atau pasang Gradle di Termux bila tersedia."
  exit 2
fi
APK="$HERE/app/build/outputs/apk/debug/app-debug.apk"
if [[ -f "$APK" ]]; then echo "BUILD BERHASIL: $APK"; else echo "ERROR: build selesai tetapi APK tidak ditemukan."; exit 1; fi
