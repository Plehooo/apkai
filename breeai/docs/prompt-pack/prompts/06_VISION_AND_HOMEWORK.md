# Tahap 6 — Kamera, gambar, screen assistance, tugas

Implementasikan Android Photo Picker, kamera dengan permission runtime, image preview, resize/compress, batas file/MIME, dan upload ke backend multimodal provider yang nyata. Tampilkan persetujuan yang menjelaskan gambar dikirim ke server AI online. Jangan mempertahankan gambar secara permanen tanpa pilihan user.

Buat alur bantuan tugas: transkripsi gambar jika provider mendukung, ringkasan, penjelasan langkah, salin jawaban, dan ketidakpastian jika gambar buram/tidak lengkap. Jangan mengarang tulisan yang tak terbaca.

Jika menambah analisis layar, gunakan MediaProjection consent resmi dengan indikator dan foreground service type yang sesuai, hentikan saat user stop, dan jangan bypass secure-window. Sediakan manual screenshot/photo fallback. Tangani permission ditolak, kamera gagal, format tidak didukung, upload timeout, provider tidak mendukung vision, dan quota limit.
