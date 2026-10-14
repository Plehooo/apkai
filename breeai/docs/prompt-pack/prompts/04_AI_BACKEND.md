# Tahap 4 — Backend FastAPI, model AI, secrets, Edge TTS dan Groq STT

Buat backend nyata. Pilihan yang direkomendasikan untuk prototipe all-in-one adalah Python FastAPI agar Edge TTS community package dapat dijalankan langsung. Hosting harus support Python dan koneksi keluar; bila memakai Cloudflare Workers JS, Edge TTS perlu service Python terpisah. Catat cold-start/idle limit hosting free.

Implementasikan:
- `GET /health` dan `GET /v1/status` (tidak mengembalikan secrets).
- `POST /v1/chat` dengan Gemini API sebagai primary, model lewat `GEMINI_MODEL`.
- `POST /v1/vision` hanya pada provider yang benar-benar support image input.
- `POST /v1/tts` dengan `edge-tts`, voice allowlist `id-ID-GadisNeural` dan `id-ID-ArdiNeural`, rate/pitch limits, response MP3, no payload logging.
- `POST /v1/stt` menerima audio bounded dan memakai Groq Whisper hanya bila `GROQ_API_KEY` tersedia.
- Optional OpenRouter adapter untuk text fallback hanya pada free model/route yang telah diverifikasi. No auto paid fallback.
- Optional research endpoint memakai Gemini Google Search grounding hanya bila entitlement, quota, and current pricing telah diverifikasi; otherwise disabled with explicit status.

Pakai environment/secret manager (`GEMINI_API_KEY`, `GROQ_API_KEY`, `OPENROUTER_API_KEY` optional). Sediakan `.env.example` placeholder only, no secrets in Git/APK. Validate schema, limits, content types, requests/timeouts, rate limits, cancellation, clean temp media, normalize errors and redact logs. Build real provider integration, not mocked success. Test invalid credentials, rate limit/quota, provider timeout, invalid model, too-large audio/image and fallback.
