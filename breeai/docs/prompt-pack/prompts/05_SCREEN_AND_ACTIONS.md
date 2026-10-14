# Tahap 5 — Konteks aplikasi dan kontrol Android secara aman

Baca batas keamanan pada master prompt. Tambahkan mode sadar konteks yang opt-in menggunakan mekanisme Android yang tepat. Gunakan AccessibilityService hanya setelah user enable manual dan menerima penjelasan yang spesifik. Filter password/OTP/secure-window serta aplikasi yang dikecualikan. Jangan mengumpulkan isi layar atau mengirim ke AI diam-diam.

Buat ActionController dengan schema tindakan terbatas, validasi lokal, approval gate, dan hasil eksekusi. Jangan menjalankan instruksi model sebagai kode. Implementasikan aksi yang didukung seperti membuka aplikasi, memasukkan draft ke field aktif, klik/scroll/back bila node tersedia. Tangani target tidak ditemukan, service disable/revoke, app UI berubah, dan aksi dibatalkan.

Untuk WhatsApp/chat, default adalah generate draft, preview penerima/isi bila tersedia, lalu masukkan draft ke kolom pesan setelah pengguna menyetujui. Pengguna menekan tombol kirim sendiri. Bila fitur eksplisit untuk send disediakan, wajib ada konfirmasi final yang jelas sebelum send. Jangan membuat spam/bulk send. Uji prompt injection dari teks layar dan pastikan ia tidak dapat mengubah policy.
