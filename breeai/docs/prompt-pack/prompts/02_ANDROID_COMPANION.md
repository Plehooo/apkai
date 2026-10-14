# Tahap 2 — UI dan karakter companion mengambang

Baca master prompt dan hasil tahap 1. Buat UI Android Java/XML yang lebih matang: beranda, chat screen, panel izin/privasi, pengaturan, memory controls, dan karakter Bree dengan vector/lokal fallback.

Implementasikan overlay WindowManager/Foreground Service bila diwajibkan oleh versi Android yang dipilih, permission flow yang tepat, drag position, tap-to-open, minimize/hide/show, state animasi, serta cleanup lifecycle. Jangan memulai overlay otomatis sebelum user mengaktifkan. Jika izin ditolak, UI in-app tetap berfungsi.

Uji izin ditolak/diberikan, rotasi, minimize, proses mati, dan service cleanup sejauh environment memungkinkan. Jangan menambahkan animasi eksternal yang dibutuhkan dari internet agar core UI tetap tampil offline. Jangan klaim karakter 3D penuh jika aset yang tersedia hanya vector; sediakan jalur asset 3D/animasi sebagai enhancement yang jelas.
