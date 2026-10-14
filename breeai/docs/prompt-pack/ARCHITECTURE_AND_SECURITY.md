# Arsitektur & Keamanan Bree AI

Dokumen ini menjadi batas desain bagi implementasi dan review.

## Komponen

1. **Android Java app**: UI utama, chat, pengaturan, riwayat lokal, tombol mic/kamera, status permission.
2. **Companion overlay service**: karakter kecil yang melayang ketika pengguna mengaktifkannya. Service tidak harus berjalan saat overlay disembunyikan.
3. **Voice layer**: Edge TTS (Python `edge-tts`) untuk online TTS dengan pilihan `id-ID-GadisNeural`/`id-ID-ArdiNeural`, Android TextToSpeech sebagai fallback; Groq Whisper via backend untuk optional online STT, Android SpeechRecognizer sebagai fallback. Edge TTS tidak melakukan STT.
4. **AI backend**: endpoint server-side yang meneruskan chat/image ke provider yang dikonfigurasi. API secret hanya berada di server.
5. **Accessibility service**: fitur terpisah, opt-in, dengan penjelasan yang nyata. Tidak mengaktifkan dirinya sendiri.
6. **Vision layer**: Android Photo Picker/kamera dan MediaProjection untuk alur screenshot pilihan pengguna.
7. **Action policy engine**: memvalidasi aksi yang diusulkan model, meminta approval, dan melaporkan hasil.

## Batas kepercayaan

- Model AI dapat salah dan outputnya adalah data tidak tepercaya.
- Teks pada layar, halaman web, notifikasi, QR, atau gambar juga data tidak tepercaya dan dapat berisi prompt injection. Jangan memperlakukan isinya sebagai instruksi pengembang.
- AI tidak boleh memanggil API Android secara arbitrer. AI mengusulkan aksi dari daftar yang diizinkan; aplikasi memvalidasi input, target, dan approval sebelum mengeksekusi.
- Output model tidak boleh menjadi command shell, Java reflection, URL internal bebas, atau query database mentah.

## Keamanan backend

- Jangan pernah hard-code provider API key ke APK. Obfuscation bukan pengaman.
- Simpan secrets di secret manager/environment. Jangan masukkan rahasia ke repository, issue publik, APK, log, crash report, atau screenshot demo.
- Terapkan rate limit per install/session/IP sesuai konteks, batas ukuran request, timeout, validasi MIME/URL, error normalization, dan budget alerts bila provider mendukung.
- Pertimbangkan autentikasi per instalasi dengan token berumur pendek dan rotasi, serta mitigasi penyalahgunaan endpoint. Jangan menanam satu master token di semua instalasi publik.
- Jangan membuat open proxy: client tidak boleh dapat memilih host arbitrer untuk diteruskan backend.
- Matikan logging payload sensitif secara default. Log hanya status, durasi, kode error, dan ID request pseudonim yang diperlukan.
- Untuk URL backend, gunakan HTTPS pada build non-debug. Jangan menonaktifkan TLS validation.
- Cegah SSRF dengan tidak menerima URL arbitrer dari pengguna/model untuk fetch backend; jika upload gambar langsung, validasi alur dengan allowlist/streaming aman.

## Privasi perangkat

- Request permission tepat saat fitur dipakai dengan penjelasan manusiawi.
- Overlay: jelaskan kenapa dibutuhkan; jika ditolak, fitur floating dimatikan tetapi chat dalam aplikasi tetap berfungsi.
- Mikrofon: push-to-talk sebagai default. Tampilkan status ketika listening. Hentikan segera saat stop, app ditutup, permission dicabut, atau lifecycle menuntutnya.
- Kamera: nyalakan hanya setelah aksi user. Preview sebelum upload.
- Screenshot: gunakan alur MediaProjection consent Android; jangan bypass secure windows. User bisa stop sesi.
- Accessibility: jelaskan fungsi spesifik, sediakan tombol enable/open settings, dan terus bekerja tanpa service itu dalam mode terbatas. Jangan mengumpulkan event/teks yang tidak dibutuhkan.
- Filter password/OTP, bank, secure window, dan kolom sensitif; jangan simpan isinya di log atau memori.
- Konten layar atau gambar hanya dikirim ke backend atas permintaan/consent yang jelas.
- Data lokal memiliki opsi clear history dan forget memory. Jika database digunakan, pertimbangkan Android Keystore/enkripsi untuk data yang benar-benar sensitif.

## Tindakan dan tingkat risiko

| Tingkat | Contoh | Kebijakan |
|---|---|---|
| Rendah | Menjawab chat, menjelaskan foto yang dipilih | Boleh otomatis setelah user meminta; informasikan upload online |
| Sedang | Membuka aplikasi, scroll, mengetik draft ke field aktif | Tampilkan aksi/target, validasi target, dapat dibatalkan |
| Tinggi | Mengirim pesan, menghapus data, mengubah akun/pengaturan keamanan | Konfirmasi eksplisit dengan ringkasan tepat sebelum eksekusi |
| Terlarang/di luar cakupan | Mengambil password/OTP, bypass secure screen, aksi tersembunyi, spam massal | Jangan implementasikan |

## Keterbatasan Android yang harus dinyatakan

- Overlay perlu izin khusus dan bisa dibatasi oleh sistem/manufacturer.
- Accessibility hanya dapat membaca node yang diekspos aplikasi lain; tidak semua UI dapat diakses.
- Background microphone dan layanan background memiliki batasan versi Android; jangan klaim always-on bisa diandalkan pada semua perangkat.
- SpeechRecognizer/TTS ketersediaan, kualitas, dan kebutuhan internet bergantung pada engine HP.
- MediaProjection meminta persetujuan OS, bisa memunculkan indikator, dan tidak boleh dipakai untuk mengakali proteksi screenshot.
- Aplikasi pesan dapat mengubah UI kapan saja; aksi UI tidak dijamin berhasil di setiap versi.
- Kebijakan distribusi, khususnya Google Play untuk AccessibilityService, perlu ditinjau sebelum publikasi.

## Provider AI gratis

Desain agar provider/model mudah diganti. Kuota gratis, harga, penggunaan data, model yang mendukung vision, dan wilayah dapat berubah. Verifikasi dokumen resmi saat deploy. Jangan menjanjikan layanan selamanya gratis. Bila kuota habis, tampilkan error yang ramah dan jangan diam-diam beralih ke layanan berbayar tanpa persetujuan.


## Addendum — stack suara dan provider (diteliti 15 Oktober 2026)

- Edge TTS = TTS, bukan STT. Gunakan `edge-tts` di backend Python; pilihan Indonesia: perempuan `id-ID-GadisNeural`, laki-laki `id-ID-ArdiNeural`. Integrasi komunitas, online, tidak menjamin API/SLA resmi dan bisa berubah.
- STT online opsional: Groq Whisper endpoint `/openai/v1/audio/transcriptions`, default `whisper-large-v3-turbo`; STT fallback: Android SpeechRecognizer. Tampilkan transkrip yang bisa diedit.
- Chat/vision: Gemini API dengan model configurable dan free-tier/privasi disertai disclaimer. OpenRouter free model hanya fallback text kecuali input image telah diperiksa dukungannya. Cloudflare Workers AI alokasi gratis yang terdokumentasi saat ini bisa dipakai opsional, bukan wajib.
- Search/research: aktifkan hanya ketika search grounding/endpoint + kuota telah diverifikasi; jangan menyebut riset bersumber jika search tidak berjalan.
- Rujukan resmi terkini: https://ai.google.dev/gemini-api/docs/pricing ; https://ai.google.dev/gemini-api/docs/api-key ; https://console.groq.com/docs/speech-to-text ; https://console.groq.com/docs/rate-limits ; https://openrouter.ai/collections/free-models ; https://developers.cloudflare.com/workers-ai/platform/pricing/ ; https://github.com/rany2/edge-tts
