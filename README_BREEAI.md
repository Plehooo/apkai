# Bree AI — Bootstrap Native Android + Backend

Bundle ini mengubah spesifikasi `BreeAI_Prompt_Pack_Master_Edge_Free_AI.zip` menjadi fondasi proyek yang bisa diuji dan dibangun. Source ditempatkan di folder `breeai/` agar dapat disuntikkan ke repository yang sudah ada melalui branch baru tanpa menimpa `main`.

## Yang benar-benar disediakan

- Aplikasi Android native Java, UI chat lokal, pengaturan URL backend, pengecekan `/health`, percakapan via `POST /v1/chat`, riwayat sesi sementara, hapus chat, dan opsional text-to-speech Android.
- Backend FastAPI dengan `/health`, chat Gemini sungguhan, Edge TTS (MP3) dan Groq Whisper STT endpoint opsional.
- Validasi ukuran/format input, timeouts, rate limit in-memory dasar, secrets hanya melalui environment, serta tes backend.
- GitHub Actions untuk mengetes backend dan membangun APK debug. Artifact diunggah hanya setelah build berhasil.
- Script Termux untuk menyuntikkan folder `breeai/` dan workflow khusus ke branch baru GitHub.
- Prompt pack asli tetap tersedia di `breeai/docs/prompt-pack/`.

## Batas versi bootstrap

Belum diimplementasikan/diuji: floating overlay, AccessibilityService, kamera/Photo Picker, screenshot MediaProjection, aksi aplikasi, memori persisten, konfigurasi provider di UI, dan pemutaran Edge-TTS dari aplikasi Android. Jangan menyatakan fitur tersebut selesai.

## Bangun lokal

Backend:

```bash
cd breeai/backend
python -m venv .venv
. .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
uvicorn app.main:app --host 127.0.0.1 --port 8000
```

Isi `.env` secara lokal dengan `GEMINI_API_KEY` yang valid sebelum mencoba chat. Jangan commit `.env`. Default model `gemini-3.8-flash` dikonfigurasi melalui `GEMINI_MODEL` dan bisa diganti sesuai model yang tersedia di akun. Endpoint tidak melakukan fallback ke model/vendor lain; kuota habis ditampilkan sebagai error. Perhatikan bahwa penggunaan gratis/berbayar tetap bergantung pada konfigurasi billing dan kuota project di dashboard provider.

Android membutuhkan JDK 17+, Android SDK platform 36 dan Gradle 9.6.0. Jika toolchain lokal tidak tersedia, gunakan GitHub Actions; workflow melakukan build di runner Linux.

## GitHub Actions

Workflow ada di `.github/workflows/breeai-ci.yml`. Pada push ke branch mana pun, dua job berjalan paralel: tes backend dan build Android. APK debug diunggah sebagai artifact hanya bila build sukses. Workflow tidak memerlukan API key untuk build APK; untuk chat live, masukkan `GEMINI_API_KEY` di environment backend hosting, bukan di repository atau APK.

## Mengirim dari Termux

Lihat `TERMUX_PUSH.md`, lalu jalankan `bash breeai/scripts/push-to-github.sh https://github.com/USERNAME/REPO.git`. Script membuat branch `breeai-bootstrap-*`, mendorong branch tersebut, dan tidak mengubah `main` secara langsung. GitHub Actions harus aktif di repository.

## Keamanan

- Jangan masukkan token GitHub atau API keys ke source, command yang tersimpan di shell history, issues, maupun zip.
- Utamakan SSH remote atau `gh auth setup-git` untuk autentikasi push.
- Backend MVP menggunakan rate limit in-memory; untuk deployment publik serius, tambahkan shared rate limiter, autentikasi klien, monitoring, dan kebijakan retensi sesuai kebutuhan.
- Edge TTS adalah integrasi komunitas online dan tidak dijamin tersedia tanpa batas. Tidak ada fallback otomatis ke provider berbayar.
