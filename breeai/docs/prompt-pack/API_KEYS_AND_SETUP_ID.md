# API key dan setup Bree AI (panduan praktis)

**Aturan utama:** APK Bree tidak menyimpan key Gemini/Groq/OpenRouter. Semua key provider dimasukkan sebagai environment secret di backend. Edge TTS package sendiri tidak memerlukan API key, tetapi tetap membutuhkan koneksi online dan dapat berubah.

## Yang perlu dibuat (tidak harus semuanya)

### Minimal untuk mencoba chat + foto
1. Google AI Studio: https://aistudio.google.com/api-keys
2. Buat Gemini API key dan simpan pribadi.
3. Di dashboard host backend, buat secret `GEMINI_API_KEY` dan isi nilai asli.
4. Set variable biasa `GEMINI_MODEL=gemini-3.8-flash` jika model tersebut tersedia pada akun kamu.
5. Deploy backend; masukkan URL backend ke konfigurasi app (URL bukan key upstream).

### Agar suara masuk ditranskrip oleh server (STT)
1. Daftar/login di Groq Console: https://console.groq.com/keys
2. Buat `GROQ_API_KEY`.
3. Masukkan key sebagai secret di backend, bukan ke APK.
4. Set `GROQ_STT_MODEL=whisper-large-v3-turbo` jika model tersedia.
5. Jika belum mau membuat key Groq, Android SpeechRecognizer bisa dipakai sebagai fallback, tergantung kemampuan HP/engine/network.

### Fallback chat text opsional
1. Buka https://openrouter.ai/settings/keys
2. Buat `OPENROUTER_API_KEY` jika diperlukan.
3. Tambahkan secret di backend dan pilih route/model yang benar-benar gratis pada waktu dipakai. Cek modality dan kebijakan datanya. Jangan mengaktifkan paid fallback otomatis.

## Contoh environment backend

```dotenv
AI_PROVIDER=gemini
GEMINI_API_KEY=replace_me
GEMINI_MODEL=gemini-3.8-flash
GROQ_API_KEY=replace_me_optional
GROQ_STT_MODEL=whisper-large-v3-turbo
OPENROUTER_API_KEY=replace_me_optional
OPENROUTER_TEXT_MODEL=openrouter/free
EDGE_TTS_ENABLED=true
EDGE_TTS_DEFAULT_VOICE=id-ID-GadisNeural
EDGE_TTS_FEMALE_VOICE=id-ID-GadisNeural
EDGE_TTS_MALE_VOICE=id-ID-ArdiNeural
STT_PROVIDER=auto
RESEARCH_ENABLED=false
```

Jangan mengirim file `.env` ke GitHub. `.env.example` dengan kata `replace_me` boleh masuk Git. Jangan berbagi key ke chat, issues publik, screenshot, log, atau zip. Jika key bocor, revoke/regenerate di dashboard provider lalu update server secret.

## Secret manager vs app Settings

- Benar: host backend → Settings/Environment/Secrets → `GEMINI_API_KEY` → value API key pribadi.
- Salah: `app/src/main/java/...`, `BuildConfig`, `strings.xml`, `assets/config.json`, `local.properties`, commit Git, atau string literal dalam apk.
- Pengguna APK tidak perlu API key upstream. Settings app menampilkan provider/model status dan pilihan suara, bukan key.
- Untuk satu APK pribadi dengan backend pribadi, bila backend butuh access control tambahan, gunakan login/token berumur pendek/akses yang dikonfigurasi user serta rate limiting. Jangan menanam master secret bersama ke semua instalasi.

## Test setelah deploy

1. `GET /health` harus mengembalikan status tanpa secrets.
2. `POST /v1/chat` dengan payload pendek harus mengembalikan jawaban nyata dari provider.
3. Foto dikirim hanya ke endpoint vision setelah consent; test gambar buram/terlalu besar.
4. `POST /v1/stt` harus menghasilkan transkrip Bahasa Indonesia; transkrip ditampilkan untuk koreksi.
5. `POST /v1/tts` untuk voice Gadis dan Ardi harus menghasilkan MP3 dan dapat diputar; bila gagal, app laporkan fallback Android TTS.
6. Simulasikan no key, 401/403, 429, timeout, offline, model id salah, dan backend sleep/cold start.
7. Search/research diuji hanya jika flag diaktifkan serta kuota/tier bisa menggunakan fitur grounding. Kalau tidak, nyatakan belum dikonfigurasi.

## Catatan kuota/data

Semua free tiers memiliki limit dan syarat. Jangan mempublikasikan klaim “unlimited gratis”. Periksa pricing resmi, kebijakan penggunaan data, negara/region, dan entitlements akun. Hindari mengirim kata sandi, OTP, chat privat, screenshot aplikasi bank, atau gambar sensitif ke provider AI online.
