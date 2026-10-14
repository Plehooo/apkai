# Tahap 3 — Persona, voice chat, Edge TTS dan STT nyata

Baca `PERSONA_BREE.md`, `VOICE_STACK_EDGE_TTS_STT.md`, master prompt, dan checklist. Implementasikan percakapan teks nyata ke backend, konteks sesi, riwayat lokal dengan opsi delete, loading/error/cancel/retry, dan persona sebagai system instruction backend.

**Penting:** Edge TTS adalah TTS (teks ke suara), bukan STT. Implementasikan:
- `edge-tts` package di Python FastAPI backend dan pilihan voice Bahasa Indonesia perempuan `id-ID-GadisNeural` / laki-laki `id-ID-ArdiNeural` di Settings, preview suara, rate/pitch bila tersedia.
- Android TextToSpeech fallback jika Edge TTS gagal; tampilkan status provider voice dan jangan klaim offline.
- STT Groq Whisper online (`whisper-large-v3-turbo` via backend) jika secret disiapkan; fallback Android `SpeechRecognizer`. Tampilkan transkrip Bahasa Indonesia yang dapat diedit.
- Push-to-talk sebagai default, indikator mic/listening, permission runtime, audio consent disclosure, stop/cancel yang benar dan bersihkan audio temporer.
- Untuk percakapan berikutnya, tawarkan tekan-untuk-bicara lagi; always-listening hanya jika user secara eksplisit mengaktifkan fitur yang diizinkan OS.

Tangani permission denied, recognizer/voice unavailable, no-match, HTTP 401/403/429/5xx, timeout, offline, model unavailable, audio focus, lifecycle, backend sleeping/cold start dan text-too-long. Jangan hardcode jawaban palsu. Tes backend/Android yang dapat dijalankan; laporkan perangkat/tes yang belum tersedia.
