# Tahap 7 — Free provider stack dan konfigurasi rahasia

Baca `FREE_AI_PROVIDER_RESEARCH.md`, `VOICE_STACK_EDGE_TTS_STT.md`, `API_KEYS_AND_SETUP_ID.md`, dan master prompt. Terapkan adapter provider yang benar-benar dapat dipilih lewat environment, bukan fake dropdown.

Prioritas prototipe:
1. Gemini API untuk chat + gambar, model id `GEMINI_MODEL` configurable dan kunci hanya backend.
2. Edge TTS Python package untuk audio jawaban; voice ID allowlist perempuan `id-ID-GadisNeural`, laki-laki `id-ID-ArdiNeural`.
3. Groq Whisper `whisper-large-v3-turbo` sebagai online STT jika `GROQ_API_KEY` terpasang; Android SpeechRecognizer fallback.
4. OpenRouter free route/model sebagai text-only fallback default kecuali kemampuan multimodal dikonfirmasi. Jangan pernah beralih otomatis ke paid model.
5. Google Search grounding/Research hanya bisa diaktifkan setelah entitlement+harga/quota akun dipastikan.

Buat `.env.example`; tulis langkah membuat key di dashboard resmi; pasang environment secrets di host deploy; uji no-key, invalid-key, rate limit, timeout, provider unavailable. Jangan pernah commit `.env`, key, service account, atau token. Pastikan APK tidak berisi upstream secrets. Tunjukkan status provider aktif tanpa mengungkap nilai key.

Jalankan tests. Jangan menyebut semua komponen gratis tanpa batas; laporkan limit, data-use caveat, serta model/voice yang berhasil diuji.
