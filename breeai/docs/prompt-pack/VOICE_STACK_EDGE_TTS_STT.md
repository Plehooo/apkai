# Voice Stack Bree — Edge TTS + STT Gratis/Fallback

Diperiksa: 15 Oktober 2026. Cek ulang dokumentasi sebelum deploy karena layanan, kuota, suara, dan syarat pemakaian bisa berubah.

## Jangan tertukar

- **TTS (Text-to-Speech)**: tulisan jawaban AI dibacakan. Edge TTS cocok untuk kebutuhan ini.
- **STT (Speech-to-Text)**: ucapan pengguna diubah menjadi teks. Edge TTS tidak menyediakan fungsi ini. Gunakan Groq Whisper online atau Android SpeechRecognizer fallback.

## Suara Indonesia Edge TTS

| Pilihan di Settings | Voice ID | Sifat |
|---|---|---|
| Perempuan — Gadis | `id-ID-GadisNeural` | Suara neural Indonesia perempuan |
| Laki-laki — Ardi | `id-ID-ArdiNeural` | Suara neural Indonesia laki-laki |

Edge TTS komunitas `edge-tts` menggunakan layanan TTS online Microsoft Edge tanpa API key provider menurut README project. Tetapi ini **bukan API developer resmi dengan jaminan SLA**. Jangan menjanjikan akan gratis/selalu bekerja selamanya; cek ketentuan layanan sebelum rilis publik/komersial. Dokumentasi: https://github.com/rany2/edge-tts

## Rekomendasi implementasi

Gunakan satu backend Python FastAPI untuk memudahkan integrasi `edge-tts`, Gemini, Groq STT, dan optional OpenRouter. Hosting harus menjalankan Python serta mengizinkan koneksi keluar. Render free saat dokumentasi diperiksa bisa menjalankan web service Python/FastAPI, tetapi service free spin-down setelah 15 menit tanpa trafik dan cold-start sekitar satu menit; ini cocok untuk percobaan/hobi, tidak untuk jaminan always-on. Sumber: https://render.com/docs/free dan https://render.com/docs/your-first-deploy

Endpoint yang disarankan:

- `GET /health` — status aman tanpa menampilkan secrets.
- `GET /v1/status` — provider yang terkonfigurasi + capability flags + status umum, tanpa key/token.
- `POST /v1/chat` — teks chat dengan validasi skema dan batas konteks.
- `POST /v1/vision` — gambar yang secara eksplisit dipilih/diizinkan user.
- `POST /v1/stt` — multipart audio ke Groq Whisper; provider hanya server-side.
- `POST /v1/tts` — teks dan voice ID allowlisted, hasil MP3.
- `POST /v1/research` — optional grounding web/search, hanya jika tersedia dan diaktifkan.

### TTS request contract

Request JSON:

```json
{
  "text": "Halo, aku Bree. Mau bahas apa hari ini?",
  "voice": "id-ID-GadisNeural",
  "rate": "+0%",
  "pitch": "+0Hz"
}
```

Validasi `text` tidak kosong dan batasi panjang (contoh 1.500 karakter per chunk; tentukan angka dari uji). Voice hanya boleh salah satu dari allowlist yang telah dikonfigurasi. Rate/pitch/volume harus range-checked. Hasil berupa `audio/mpeg`; gunakan timeout, cancellation, dan jangan log body. Untuk teks panjang, potong pada batas kalimat dan putar berurutan. Jangan menaruh MP3 hasil suara di storage permanen tanpa pilihan pengguna.

### STT request contract

`POST /v1/stt` menerima audio upload dengan max size dan durasi yang dibatasi backend. Groq endpoint: `https://api.groq.com/openai/v1/audio/transcriptions`; model konfigurasi `whisper-large-v3-turbo` atau `whisper-large-v3`; kirim `language=id` bila didukung. Rujukan: https://console.groq.com/docs/speech-to-text dan https://console.groq.com/docs/rate-limits

STT flow:
1. Pengguna tekan mic; minta izin mikrofon.
2. Rekam hanya selama sesi yang terlihat dan dapat dihentikan.
3. Upload audio setelah pengguna memulai aksi; berikan indikator bahwa pemrosesan online mengirim audio ke server/provider.
4. Tampilkan transkrip yang dapat diedit sebelum chat dikirim.
5. Hapus file sementara/audio buffer sesudah selesai atau batal.
6. Jika key/provider/kuota tidak tersedia, fallback ke Android SpeechRecognizer setelah menjelaskan pilihan. Jangan mengirim ganda secara diam-diam.

## Config environment backend

`.env.example` (placeholder saja; jangan commit `.env` aktual):

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

`EDGE_TTS_*` bukan rahasia. API key yang sesungguhnya hanya dimasukkan di dashboard secret/environment host. APK tidak memerlukan provider keys. Jangan menaruh upstream secrets di `BuildConfig`, XML, assets, keystore, Git, atau `.github/workflows` yang commit hardcoded.

## Android playback

- Gunakan `MediaPlayer` atau player dependency yang sudah dipilih; pastikan audio focus, lifecycle, stop, network error, buffer, dan release resource ditangani.
- UI state: IDLE → LISTENING → TRANSCRIBING → THINKING → SPEAKING → IDLE; setiap langkah bisa gagal/cancel dengan jalur bersih.
- Di Settings: perempuan/laki-laki, preview suara, voice speed, toggle Edge TTS, fallback Android TTS, pilihan Groq/Android STT, indikator provider aktif.
- Jangan berjanji suara Edge offline. Jangan klaim transkripsi Edge.

## Test minimum

- Suara Gadis dan Ardi sukses / voice tidak tersedia.
- Text kosong, terlalu panjang, karakter khusus, chunk panjang, timeouts, response non-MP3.
- TTS dipotong di tengah; pindah activity; telepon masuk/audio focus.
- Permission mic denied; STT Bahasa Indonesia; no speech/no match; audio terlalu besar; network error; Groq 401/429/5xx; Android fallback.
- Tidak ada audio/text sensitif di log dan tidak ada API key yang dapat ditemukan di APK.
