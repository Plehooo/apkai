# Riset provider AI gratis untuk Bree

**Snapshot dokumentasi: 15 Oktober 2026.** Ini bukan jaminan kuota/harga akan bertahan. Sebelum deploy, buka link resmi dan periksa page pricing akun kamu. “Gratis” biasanya berarti tier/kuota terbatas, bukan unlimited dan tidak selalu tanpa syarat.

## Rekomendasi fungsi

| Fungsi Bree | Provider awal | Opsi cadangan | Catatan biaya/privasi |
|---|---|---|---|
| Chat + analisis gambar | Gemini API, model configurable `gemini-3.8-flash` | Model OpenRouter gratis dengan image input terverifikasi, jika user mengaktifkan | Gemini pricing menampilkan free tier pada model ini ketika dicek. Free tier dapat memiliki ketentuan penggunaan data berbeda dari tier berbayar; periksa sebelum mengirim data pribadi. |
| STT Indonesia (audio → teks) | Groq Whisper `whisper-large-v3-turbo` | Android `SpeechRecognizer` | Groq mensyaratkan API key dan kuota akun; docs memperlihatkan base free-plan rate limits yang bisa berbeda per akun. Tidak ada Edge STT. |
| TTS Indonesia (teks → audio) | Edge TTS komunitas, female/male selectable | Android `TextToSpeech` | edge-tts menyebut tidak memerlukan API key, tetapi menggunakan layanan online dan bukan developer API dengan jaminan SLA resmi. |
| Chat text fallback | OpenRouter free route/model, hanya bila dipilih & tersedia | Groq text model yang masih memiliki free quota | Model bebas berubah; cek modality dan privacy policy/provider route. Jangan auto-fallback ke model berbayar. |
| Inferensi alternatif | Cloudflare Workers AI jika akun+model cocok | — | Dokumen saat dicek memberi 10.000 Neurons/hari di free allocation; beberapa model butuh billing, usage per model berbeda. |
| Pencarian web / riset | Gemini Google Search grounding bila akses/quota akun mengizinkan | Provider search lain hanya setelah user menambah API key yang sesuai | Tidak boleh mengklaim live search gratis tanpa batas. Harga/kuota Search grounding bergantung model/tier/tool. Sediakan toggle dan periksa runtime. |
| Hosting backend | Python FastAPI host yang cocok, misalnya Render free untuk prototipe | Hosting lain dengan Python+outbound connection | Hosting free bisa sleep/cold-start, membatasi bandwidth, waktu proses, atau berubah paket; kurang cocok untuk always-on. |

## Link resmi / sumber primer

- Gemini models: https://ai.google.dev/gemini-api/docs/models
- Gemini pricing: https://ai.google.dev/gemini-api/docs/pricing
- Gemini API key + security: https://ai.google.dev/gemini-api/docs/api-key
- Gemini Search grounding: https://ai.google.dev/gemini-api/docs/google-search
- Groq speech-to-text: https://console.groq.com/docs/speech-to-text
- Groq rate limits: https://console.groq.com/docs/rate-limits
- OpenRouter free model list: https://openrouter.ai/collections/free-models
- Cloudflare Workers AI pricing: https://developers.cloudflare.com/workers-ai/platform/pricing/
- Edge TTS community package: https://github.com/rany2/edge-tts
- Render free limitations: https://render.com/docs/free
- Render Python/FastAPI deploy: https://render.com/docs/your-first-deploy

## Penilaian mendalam

### Gemini
Bagus sebagai default chat + gambar dan tersedia model dengan free tier pada saat snapshot. Jangan menanam key di aplikasi Android. Model id disetel melalui env (`GEMINI_MODEL`), tidak disebar di banyak file. Perhatikan bahwa dokumen Google menjelaskan free/paid data-use differences, model-specific quotas, dan Google Search grounding dapat mempunyai entitlement/biaya terpisah. Jangan menjadikan fitur grounding sebagai prasyarat semua chat.

### Groq Whisper
API STT yang terdokumentasi, mendukung upload file audio ke endpoint transcriptions. `whisper-large-v3-turbo` cocok untuk kecepatan; `whisper-large-v3` sebagai opsi bila akurasi lebih penting. Limit, file size, supported audio type, dan account eligibility perlu diperiksa dari docs dan dashboard saat kode dibuat. Tampilkan indikator bahwa audio diproses secara online.

### Edge TTS
`edge-tts` adalah Python package open-source yang memakai layanan online Microsoft Edge TTS tanpa API key provider. Suara Indonesia yang biasa digunakan: `id-ID-GadisNeural` (female), `id-ID-ArdiNeural` (male). Ini komunitas wrapper, bukan bukti adanya API resmi Microsoft untuk penggunaan produk tanpa batas. Buat adapter optional, graceful fallback, dan periksa terms sebelum dirilis.

### OpenRouter free
Koleksi free models adalah dinamis. `openrouter/free` bisa memilih model gratis, tetapi tidak menjamin setiap pilihan mendukung image, function calling, atau kebijakan data yang sama. Untuk tugas multimodal, validasi `input_modalities`/fitur aktual dan jangan menurunkan gambar ke model teks. Beberapa free routes dapat memanfaatkan input/output untuk meningkatkan model; jangan kirim data sensitif tanpa persetujuan.

### Cloudflare Workers AI
Alokasi free dilaporkan 10.000 Neurons per hari pada tanggal snapshot. Ini tidak berarti semua model, chat panjang, vision, serta request pengguna unlimited; model tertentu mungkin membutuhkan billing. Gunakan jika konkret dibutuhkan, bukan karena angka kuota kelihatan besar.

### Hosting gratis dan arsitektur
Satu Python FastAPI backend dapat meng-host adapter Edge TTS dan memanggil Gemini/Groq/OpenRouter via server HTTP. Render free mendukung web service Python, tetapi service free sleep setelah 15 menit idle dan perlu cold-start ketika diminta lagi. Cloudflare Workers sangat baik untuk serverless JS/TS, namun tidak menjalankan package Python `edge-tts` secara langsung; jika memilih Workers, Edge TTS perlu service lain atau pilih Android TTS/provider TTS yang kompatibel.

## Policy fallback wajib

1. Hanya gunakan provider fallback yang diaktifkan/configured pengguna.
2. Fallback tidak boleh diam-diam memilih jalur berbayar.
3. Jangan kirim foto/audio/teks layar ke provider lain tanpa consent yang berlaku.
4. Bila provider error 401/403, beri petunjuk konfigurasi; bila 429, hormati retry-after/kuota; jangan retry tanpa batas.
5. Jika fallback tidak mendukung gambar/tools/search, jangan berpura-pura dukungan itu ada.
6. Sampaikan provider/model yang digunakan secara singkat dalam Settings atau detail status.
