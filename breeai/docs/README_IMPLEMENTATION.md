# Implementasi bootstrap

## API contract
- `GET /health`: status dan flags konfigurasi, tidak mengembalikan secrets.
- `POST /v1/chat`: `{ "messages": [{"role":"user|assistant", "content":"..."}] }`, response `{ "reply":"...", "model":"..." }`.
- `POST /v1/tts`: `{ "text":"...", "voice":"id-ID-GadisNeural|id-ID-ArdiNeural", "rate":"+0%", "pitch":"+0Hz" }`, response audio MPEG/MP3.
- `POST /v1/stt`: multipart `file`, response `{ "text":"...", "model":"..." }`.

## Security notes

Provider upstreams are fixed in server code; client cannot choose an arbitrary upstream host. Gemini key is sent in `x-goog-api-key`, and Groq key only in server-side Authorization header. Input limits and generic upstream errors reduce accidental data leakage. Do not log incoming chat/audio or API keys. The process-local rate limit is an MVP guardrail, not a substitute for a shared production rate limiter.

## Known gaps

- Android client presently wires text chat, backend health check, clear-session button and local Android TTS. The backend's `/v1/tts` and `/v1/stt` endpoints have not been wired into Android UI yet.
- No floating overlay, accessibility, camera, screen capture, or action engine is implemented in this bootstrap.
- Test suite mocks by configuration/no-key paths; live provider API calls and real device UX still require manual testing with valid secrets and a physical device.
