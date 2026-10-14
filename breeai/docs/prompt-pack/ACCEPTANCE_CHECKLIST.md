# Checklist penerimaan / Definition of Done

Gunakan status `LULUS`, `GAGAL`, atau `BELUM DIUJI`; tulis versi OS/perangkat dan bukti yang relevan. Jangan mengubah `BELUM DIUJI` menjadi `LULUS` tanpa benar-benar menjalankan tes.

## Proyek dan build
- [ ] Source Android native Java tersimpan di project.
- [ ] Gradle Wrapper tersedia dan executable di Linux.
- [ ] `assembleDebug` benar-benar berhasil; lokasi APK dilaporkan.
- [ ] Dependency, AGP, Gradle, JDK, compileSdk kompatibel.
- [ ] Unit tests/lint yang disediakan benar-benar dijalankan.
- [ ] GitHub Actions valid, tidak memakai paket SDK usang yang diketahui gagal, dan mengunggah APK hanya bila build sukses.
- [ ] Termux script mengecek prerequisites dan memberi error yang jelas.

## UI dan karakter
- [ ] UI bisa dibuka tanpa backend.
- [ ] Karakter Bree memiliki fallback asset lokal.
- [ ] State idle/listening/thinking/speaking/error tampak jelas.
- [ ] Drag, tap, minimize, hide/show bekerja ketika izin overlay ada.
- [ ] Overlay permission ditolak tidak membuat crash; chat in-app tetap jalan.
- [ ] Rotasi, screen size, service restart, dan permission revoke ditangani.

## Chat dan AI
- [ ] Chat mengirim request ke backend nyata setelah konfigurasi.
- [ ] API key provider tidak ditemukan di APK/source client.
- [ ] Konteks percakapan tersambung dan punya batas ukuran.
- [ ] Loading, cancel, retry, offline, timeout, 401/403, 429, provider error diuji.
- [ ] Tidak ada jawaban palsu/hard-coded yang diklaim sebagai AI live.
- [ ] Provider free quota habis tidak menyebabkan charge/overflow tersembunyi.

## Persona dan memori
- [ ] Bree menjawab dengan bahasa natural sesuai `PERSONA_BREE.md`.
- [ ] Tugas mendapat penjelasan yang cukup dan tidak mengarang.
- [ ] Riwayat bisa dihapus.
- [ ] Memori jangka panjang opt-in, dapat dilihat/diedit/dihapus.
- [ ] Tidak menyimpan password, OTP, token, atau data sensitif sebagai memori.

## Suara
- [ ] Microphone permission diminta saat diperlukan.
- [ ] Push-to-talk start/stop bekerja.
- [ ] Transkrip terlihat dan dapat dikoreksi.
- [ ] No-match, timeout, engine unavailable, permission denial ditangani.
- [ ] TTS membacakan jawaban, bisa dihentikan, dan fallback ditangani.
- [ ] Tidak ada rekaman latar belakang tanpa tindakan dan indikator pengguna.

## Konteks layar dan aksi
- [ ] Accessibility service opt-in, fungsi dijelaskan, dan aplikasi tetap bisa digunakan tanpa service.
- [ ] Foreground app/context tidak diklaim sebagai isi layar lengkap.
- [ ] Password/OTP/secure screen diabaikan.
- [ ] Konten layar tidak diunggah tanpa permintaan dan indikator yang jelas.
- [ ] Membuka app/ketik/klik/scroll memakai daftar aksi yang tervalidasi.
- [ ] Hasil aksi diverifikasi jika memungkinkan; tidak mengklaim sukses sebelum ada hasil.
- [ ] Chat WhatsApp default hanya membuat draft/memasukkan teks; kirim harus dikonfirmasi.
- [ ] Prompt injection dari teks layar tidak dapat mengesampingkan kebijakan aplikasi.

## Kamera dan vision
- [ ] Photo Picker berfungsi tanpa izin storage luas.
- [ ] Kamera meminta izin hanya saat digunakan.
- [ ] Preview terlihat sebelum upload.
- [ ] Gambar besar/invalid/error provider ditangani.
- [ ] Consent untuk mengirim ke AI online dijelaskan.
- [ ] Screenshot memakai MediaProjection consent; capture berhenti ketika diminta.
- [ ] Tidak ada bypass secure-window protections.

## Privasi, backend, rilis
- [ ] Tidak ada secrets di Git, APK, log, atau artefak.
- [ ] Rate limit, payload limits, timeout, input validation, redacted logs tersedia.
- [ ] Backend bukan open proxy dan tidak menerima host arbitrer.
- [ ] Clear history, forget memory, stop audio, hide companion berfungsi.
- [ ] README memuat setup, deploy, build, permission, troubleshooting, dan keterbatasan.
- [ ] Pengujian perangkat nyata dilakukan atau gap-nya disebutkan jujur.
- [ ] Audit kebijakan Google Play untuk AccessibilityService dilakukan sebelum publikasi jika akan dipasang di Play Store.


## Edge TTS, STT, provider fallback dan riset web tambahan
- [ ] Settings memiliki pilihan suara Indonesia perempuan (`id-ID-GadisNeural`) / laki-laki (`id-ID-ArdiNeural`) dan preview.
- [ ] Edge TTS dijalankan dari backend Python, menghasilkan audio nyata; tidak diklaim sebagai STT atau offline.
- [ ] Bila Edge TTS error/offline, Android TextToSpeech fallback dan status fallback ditampilkan.
- [ ] STT online Groq Whisper hanya dipakai bila key tersedia; audio hanya dikirim setelah tindakan user dan transkrip dapat diedit.
- [ ] STT Android fallback tersedia; provider aktif dijelaskan dan tidak ada audio upload diam-diam ke beberapa provider.
- [ ] API keys disimpan hanya sebagai backend environment secrets; tak ada key dalam APK/source/client logs.
- [ ] Gemini/OpenRouter/Groq provider adapters menggunakan model IDs yang diverifikasi; unsupported multimodal route tidak menerima image.
- [ ] Fallback tidak pernah berpindah ke model berbayar tanpa persetujuan eksplisit.
- [ ] Kuota free/rate limit dan kebijakan data disampaikan; 401/403/429/timeout ditangani jelas.
- [ ] Riset web hanya mengklaim menggunakan web bila grounding/search benar-benar berhasil; citation metadata tervalidasi, mode bisa dimatikan, quota checked.
- [ ] Prompt injection dari hasil web tidak mengubah system rules, permission, atau action policy.
- [ ] Hosting free/cold start dibahas dan tidak dijanjikan selalu online.
