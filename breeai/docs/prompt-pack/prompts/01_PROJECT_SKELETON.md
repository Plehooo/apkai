# Tahap 1 — Skeleton project dan build nyata

Baca `MASTER_PROMPT.md`, `ARCHITECTURE_AND_SECURITY.md`, dan `ACCEPTANCE_CHECKLIST.md`. Inspeksi workspace/toolchain. Buat fondasi repo native Android Java dan backend terpisah sesuai spesifikasi.

Hasil wajib:
- Android project yang terstruktur, Gradle Wrapper dan app module.
- Beranda awal Bree AI dengan navigasi dasar, state loading/error, settings placeholder yang tidak mengklaim fitur selesai.
- Backend project terpisah, `/health` yang benar-benar bekerja, config dari environment dan `.env.example` tanpa credential.
- `.gitignore` benar termasuk local.properties, secret, build output, keystore.
- `README_ID.md` berisi dependency/toolchain aktual dan cara menjalankan.
- `scripts/build-termux.sh` dan workflow GitHub Actions build debug.
- Tes minimal backend health/config validation dan app smoke test yang layak.

Jalankan build nyata. Perbaiki error yang keluar. Jangan implementasikan fake AI atau dummy tombol yang tampak aktif. Jika Android SDK tidak tersedia, tetap buat project dan tes backend yang bisa dijalankan, lalu laporkan build Android sebagai BELUM DIUJI—jangan mengklaim sukses.
