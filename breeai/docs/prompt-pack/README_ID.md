# Bree AI — Prompt Pack Master untuk APK Android Native Java

Paket ini adalah **kumpulan master prompt, spesifikasi teknis, riset provider gratis, setup API keys, dan checklist** untuk meminta AI coding membangun Bree AI. **Bukan APK jadi dan bukan source code aplikasi yang sudah dibangun.**

Target Bree: karakter robot kecil futuristik yang bisa digeser di layar, ngobrol natural, membantu tugas, menganalisis foto, mengetahui konteks aplikasi dengan izin, serta membantu tindakan Android yang divalidasi dan disetujui pengguna.

## Stack yang ditargetkan

- Android app native Java/XML.
- Backend Python FastAPI untuk prototipe, supaya Edge TTS community package dapat berjalan.
- Gemini API sebagai opsi primary chat + image dengan model id configurable.
- Edge TTS online untuk TTS, pilihan Indonesia perempuan `id-ID-GadisNeural` dan laki-laki `id-ID-ArdiNeural`; fallback Android TextToSpeech.
- Groq Whisper sebagai optional online STT; fallback Android SpeechRecognizer.
- OpenRouter free models sebagai text fallback jika dikonfigurasi, setelah memeriksa status free/model modality.
- Optional web research via Google Search grounding hanya saat entitlement/quota akun memang mengizinkan.

**Penting:** Edge TTS bukan STT dan bukan API developer resmi yang menjamin penggunaan tanpa batas. Free-tier AI memiliki kuota dan policy data sendiri. Periksa ulang pricing dan layanan resmi sebelum deploy.

## Struktur file

- `MASTER_PROMPT.md` — spesifikasi utama end-to-end.
- `PERSONA_BREE.md` — kepribadian Bree agar obrolan tidak kaku.
- `ARCHITECTURE_AND_SECURITY.md` — arsitektur, izin Android, action policy, privacy.
- `VOICE_STACK_EDGE_TTS_STT.md` — TTS Edge, voice female/male, STT Groq/Android.
- `FREE_AI_PROVIDER_RESEARCH.md` — riset opsi provider free dan batasnya.
- `API_KEYS_AND_SETUP_ID.md` — cara buat key, pasang secrets, tes endpoint.
- `ACCEPTANCE_CHECKLIST.md` — checklist uji yang wajib dijalankan.
- `prompts/01_PROJECT_SKELETON.md` sampai `prompts/09_TEST_HARDENING_RELEASE.md` — prompt tahap demi tahap.

## Urutan kerja

1. Mulai dengan `MASTER_PROMPT.md` pada AI coding yang dapat benar-benar mengedit banyak file/workspace.
2. Jika modelnya memiliki context limit, jalankan prompt tahap 01 sampai 09 secara berurutan. Beri tahu AI coding agar membaca file spec dan mempertahankan source hasil tahap sebelumnya.
3. Buat key di dashboard resmi sesuai `API_KEYS_AND_SETUP_ID.md`; simpan hanya di backend secret/environment settings.
4. Deploy backend Python. Untuk prototipe gratis, Render mendukung FastAPI tetapi free service dapat sleep setelah 15 menit idle dan cold-start; baca keterbatasannya sebelum diandalkan.
5. Build APK di Termux atau GitHub Actions, lalu uji di HP nyata. Permission overlay, microphone, Accessibility, camera, dan screenshot tidak dapat diasumsikan berhasil tanpa pengujian.

## Prinsip penting

- Jangan hanya hasilkan cuplikan kode; minta file nyata, build nyata, dan laporan test.
- Tidak ada API provider key dalam APK, source client, Git, atau log.
- Fitur yang butuh izin harus opt-in dan menjelaskan tujuan.
- Default voice push-to-talk, tidak always-on tanpa sepengetahuan pengguna.
- WhatsApp dan tindakan berdampak: buat draft; jangan mengirim tanpa konfirmasi jelas.
- Jangan mengarang hasil build atau menandai fitur LULUS jika belum diuji.
- Free tier, quota, model ID dan privacy policy dapat berubah; verifikasi dokumentasi pada saat deploy.

## Sumber resmi yang dicek 15 Oktober 2026

- Gemini model list/pricing/key security: https://ai.google.dev/gemini-api/docs/models ; https://ai.google.dev/gemini-api/docs/pricing ; https://ai.google.dev/gemini-api/docs/api-key
- Gemini Search grounding: https://ai.google.dev/gemini-api/docs/google-search
- Groq STT/rate limits: https://console.groq.com/docs/speech-to-text ; https://console.groq.com/docs/rate-limits
- OpenRouter free models: https://openrouter.ai/collections/free-models
- Cloudflare Workers AI pricing: https://developers.cloudflare.com/workers-ai/platform/pricing/
- Edge TTS community package: https://github.com/rany2/edge-tts
- Render free deployment limitations: https://render.com/docs/free
