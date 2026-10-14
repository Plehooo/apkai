# Tahap 9 — QA, audit keamanan dan release

Jalankan seluruh `ACCEPTANCE_CHECKLIST.md`; laporkan setiap item sebagai LULUS/GAGAL/BELUM DIUJI dengan bukti. Jalankan clean Android build, unit tests, lint, backend tests, API contract tests dan workflow validation bila tersedia. Jangan menyembunyikan test gagal.

Audit: secrets, logging, HTTPS, open proxy/SSRF, payload size, rate limit, permission flow, Accessibility policy, screen/media consent, password/OTP redaction, draft-vs-send confirmation, lifecycle, memory deletion, voice service cleanup, Edge TTS fallback, Groq STT upload consent, quota handling, OpenRouter modality and no paid fallback, live search citation correctness and prompt injection from web content.

Pastikan README berisi Termux/GitHub Actions build, deploy backend, `.env.example`, link dashboard resmi untuk membuat key, setting secret di host, voice IDs perempuan/laki-laki, STT providers, privacy and free quota caveats, output APK yang benar-benar dihasilkan, fitur yang belum diuji. Untuk release, jelaskan signing melalui secrets tanpa memasukkan keystore ke repo. Jangan mengklaim siap publish hanya karena compile.
