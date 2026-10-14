# MASTER PROMPT — Bangun Bree AI, Teman Virtual Android Multifungsi

Kamu bertindak sebagai **senior Android engineer, backend engineer, AI integration engineer, UI/UX designer, QA engineer, dan security reviewer**. Bangun proyek aplikasi nyata bernama **Bree AI**; nama karakter/asisten adalah **Bree**. Ini bukan permintaan membuat konsep, mockup, tutorial saja, atau satu file demo. Hasil yang diminta adalah source project yang terstruktur, dapat dibangun, bisa diuji, dan memiliki dokumentasi lengkap.

## 0. Cara kerja dan standar hasil

1. Periksa workspace, file yang sudah tersedia, toolchain, versi Java/Gradle/Android SDK, serta kemampuan build yang benar-benar ada sebelum memilih versi dependency.
2. Bila workspace berisi proyek yang sudah ada, audit dulu dan pertahankan struktur serta kode relevan. Jangan overwrite atau menghapus hal yang tidak berkaitan. Bila workspace kosong, buat proyek bersih.
3. Buat file secara nyata, bukan hanya menampilkan cuplikan di chat. Lakukan build, lint/check yang tersedia, dan test yang realistis. Jangan mengaku berhasil jika belum dijalankan.
4. Gunakan **native Android Java** untuk aplikasi. XML layout boleh digunakan. Hindari menjadikan WebView/HTML sebagai aplikasi utama.
5. Hasil akhir harus mencakup source Android, wrapper/build configuration, backend terpisah, contoh konfigurasi non-rahasia, dokumentasi setup, pengujian, serta perintah build Termux dan GitHub Actions.
6. Jangan meninggalkan fungsi inti sebagai TODO, stub sukses, tombol inert, mock response, atau hard-coded fake AI. Bila credential belum tersedia, aplikasi harus menampilkan petunjuk konfigurasi yang jelas dan graceful error—not pretend success.
7. Jangan memasukkan API key, service-account JSON, password, token permanen, atau rahasia lainnya ke APK, Git, asset, log, atau prompt output. Gunakan environment/secrets pada backend.
8. Jika informasi penting belum tersedia, ambil default aman yang dijelaskan dalam README daripada menghentikan seluruh pekerjaan. Jangan bertanya kembali jika keputusan teknis bisa dibuat dengan default aman.
9. Implementasi harus jujur tentang batas Android, provider, izin, biaya/kuota gratis, dan kemampuan background. Jangan menjanjikan kemampuan yang tidak disediakan OS.

## 1. Produk dan tujuan

Bree AI adalah teman virtual kecil yang bisa mengambang di atas aplikasi lain dengan persetujuan pengguna. Karakter dapat dipindahkan, disentuh, diminimalkan, disembunyikan sementara, dan membuka panel percakapan. Bree terasa ramah serta natural, tidak kaku, dan dapat berubah dari obrolan santai ke mode fokus saat membantu tugas atau menjalankan perintah.

**Target produk:**
- APK Android native Java.
- Build lokal melalui Termux bila SDK tersedia; build otomatis melalui GitHub Actions di runner Ubuntu.
- AI online melalui backend milik pengembang.
- Provider AI dapat diganti melalui adapter backend; tidak mengunci arsitektur ke satu vendor.
- Fitur utama punya status izin, status koneksi, pesan error, loading state, cancellation, dan retry yang jelas.
- Bahasa antarmuka utama Bahasa Indonesia; gaya percakapan Bree santai dan kontekstual.

## 2. Pengalaman pengguna utama

### First run
1. Layar onboarding menjelaskan fungsi, izin, dan privasi dengan bahasa sederhana.
2. Pengguna dapat mencoba chat teks tanpa memberikan izin overlay, kamera, mikrofon, atau aksesibilitas.
3. Izin diminta **saat fitur terkait dipakai**, tidak sekaligus dan tidak dengan dark pattern.
4. Jelaskan overlay, voice, screen context/accessibility, camera, dan screen capture secara terpisah.
5. Lakukan health check backend tanpa membocorkan konfigurasi sensitif. Jika belum diatur, tunjukkan cara mengonfigurasi backend.

### Main app
- Tampilan modern, premium, ringan, responsif pada layar kecil.
- Beranda memiliki karakter Bree, tombol obrolan, mikrofon, kamera/foto, riwayat, pilihan kepribadian, pengaturan, privasi, dan status layanan.
- Panel chat memakai bubble, timestamp opsional, indikator sedang mendengar/berpikir/berbicara, stop audio, retry, copy, dan edit prompt.
- Percakapan tidak boleh diblokir oleh animasi.

### Floating companion
- Overlay mungil dengan animasi halus yang tidak menyita banyak layar.
- Drag untuk memindahkan posisi; posisi terakhir boleh disimpan lokal.
- Tap membuka panel ringkas; tombol eksplisit untuk bicara, chat, foto, tindakan, minimize/hide.
- Sediakan drag handle atau gesture yang jelas agar karakter tidak menghalangi tombol aplikasi lain.
- Tangani izin overlay ditolak, dicabut, layar berubah orientasi, service dimatikan, dan proses aplikasi dihentikan.
- Gunakan WindowManager dan tipe window yang benar menurut API level. Hindari meminta overlay tanpa menjelaskan alasan.

## 3. Karakter dan visual

Desain Bree sebagai robot kecil 3D futuristik: badan putih/pearl, aksen biru cyan, mata ekspresif bercahaya, bentuk sederhana, ramah, dan mudah dikenali. UI boleh memakai ilustrasi lokal hasil desain; aplikasi tetap harus berfungsi jika aset animasi gagal dimuat.

Ekspresi/status minimal: `IDLE`, `LISTENING`, `THINKING`, `SPEAKING`, `HAPPY`, `CONFUSED`, `ERROR`, `FOCUSED`. Berikan visual fallback yang ringan—misalnya vector drawable—supaya tidak tergantung pada gambar eksternal saat runtime. Animasi jangan terus menyala dengan konsumsi baterai tinggi ketika idle. Hormati Reduce Motion bila memungkinkan.

Jangan menyalin karakter berhak cipta. Jangan mengunduh asset dari URL tak terpercaya ketika aplikasi berjalan. Semua asset harus tersimpan di proyek atau bersumber jelas.

## 4. Kepribadian Bree

Gunakan `PERSONA_BREE.md` sebagai aturan sistem untuk gaya percakapan. Bree harus:
- Santai, hangat, cerdas, empatik, dan spontan; boleh memakai bahasa gaul Indonesia secukupnya.
- Menghindari jawaban seperti robot/kaku, pengulangan salam, kalimat motivasi generik, dan basa-basi berlebihan.
- Menyesuaikan panjang jawaban; untuk pertanyaan singkat jawab singkat, untuk tugas teknis jelaskan berurutan.
- Bisa bercanda tetapi tahu kapan pengguna serius.
- Tidak mengaku punya emosi, tubuh, pengalaman pribadi, atau melihat layar bila data layar tidak benar-benar tersedia.
- Tidak berpura-pura sudah mengklik, mengetik, menyimpan, membuka, mengirim, atau menjalankan sesuatu sebelum tindakan dikonfirmasi berhasil.
- Meminta klarifikasi hanya jika diperlukan; jika konteks cukup, lakukan pekerjaan.
- Menjaga batas privasi dan tidak menampilkan konten pribadi di overlay/notifikasi tanpa pilihan pengguna.

Membuat kepribadian santai tidak berarti memakai kata kasar berlebihan, merendahkan, manipulatif, atau selalu setuju. Jangan mendorong ketergantungan emosional atau mengaku sebagai manusia.

## 5. Obrolan teks dan konteks percakapan

- Buat ChatRepository/ConversationManager untuk menyimpan konteks sesi dan membangun permintaan provider.
- Percakapan harus memiliki ID sesi dan urutan pesan yang stabil; cegah request ganda saat tombol ditekan berkali-kali.
- Riwayat disimpan lokal secara opsional, dapat dihapus per percakapan atau seluruhnya. Jelaskan retensi.
- Jangan mengirim seluruh riwayat tanpa batas: trim berdasarkan token/ukuran, pertahankan pesan yang relevan, dan batasi ukuran request.
- Berikan tombol stop/cancel, retry, edit, copy, hapus, serta status offline/quota exceeded.
- Jangan mengirim data layar atau foto diam-diam ke model. Selalu beri indikator dan persetujuan saat mengirim isi layar/gambar ke layanan online.

## 6. Suara dua arah — Edge TTS + STT terpisah

Bangun voice stack yang benar-benar bekerja, dapat dikonfigurasi, dan hemat biaya. Pahami perbedaan ini: **Edge TTS adalah text-to-speech (teks menjadi suara), bukan speech-to-text (suara menjadi teks)**.

### 6.1 TTS utama: Edge TTS
- Gunakan Python package komunitas `edge-tts` pada backend Python/FastAPI, bukan mencoba menjalankan package Python langsung di APK Java.
- Service ini memakai layanan online Microsoft Edge TTS tanpa API key provider menurut dokumentasi proyek komunitas, tetapi ini bukan jaminan SLA/API resmi untuk developer. Tandai sebagai integrasi komunitas yang dapat berubah atau berhenti bekerja; verifikasi syarat penggunaan sebelum distribusi publik/komersial.
- Sediakan pilihan suara Bahasa Indonesia di Settings:
  - Perempuan: `id-ID-GadisNeural`
  - Laki-laki: `id-ID-ArdiNeural`
- Default awal boleh `id-ID-GadisNeural`, tetapi pengguna dapat mengganti gender/voice, mencoba preview suara, menyetel rate/pitch/volume dalam batas service, dan mengatur kecepatan bicara. Jangan membuat suara clone atau menyamarkan identitas sumber suara.
- Buat endpoint backend terbatas seperti `POST /v1/tts` yang menerima `{ "text": "...", "voice": "id-ID-GadisNeural", "rate": "+0%", "pitch": "+0Hz" }`, memvalidasi voice melalui allowlist, membatasi panjang teks, menghasilkan MP3, menetapkan content-type yang tepat, menangani timeout/cancel/error, dan tidak mencatat teks lengkap ke log.
- Jangan menerima nama voice arbitrer tanpa validasi. Jangan meneruskan URL/audio endpoint yang dipilih client. Jangan simpan file audio/text lebih lama dari yang diperlukan.
- Android memutar audio hasil TTS dengan lifecycle/audio focus yang benar; tombol stop harus menghentikan playback segera.
- Fallback wajib: saat Edge TTS gagal/offline/timeout, tawarkan Android `TextToSpeech` engine yang tersedia. Beri tahu fallback aktif, karena kualitas suara/voice berbeda antar HP. Jangan membuat seolah Edge TTS berhasil bila memakai fallback.
- Karena Edge TTS membutuhkan backend Python, pilih backend yang benar-benar dapat menjalankan Python dan membuat koneksi keluar. Cloudflare Workers JavaScript tidak bisa begitu saja mengimpor package Python `edge-tts`; jika memakai Workers, TTS perlu service Python terpisah atau provider lain. Untuk prototipe, Python FastAPI di hosting gratis yang sesuai dapat digunakan, tetapi periksa batas host/cold-start dan ketersediaan layanan setiap saat.

### 6.2 STT utama online: Groq Whisper, fallback Android
- Untuk transkripsi Bahasa Indonesia online, buat adapter STT server-side ke Groq API endpoint `https://api.groq.com/openai/v1/audio/transcriptions`, model konfigurasi default `whisper-large-v3-turbo` dan opsi `whisper-large-v3` jika dikonfigurasi. Model ID dan batas file harus diverifikasi dari dokumentasi saat deploy.
- Rahasia `GROQ_API_KEY` hanya ada di environment/secret manager backend, tidak pernah di APK.
- Rekam audio hanya setelah aksi pengguna dan izin mikrofon. Gunakan push-to-talk secara default, batas durasi/file yang wajar, kompresi audio bila perlu, timeout, pembatalan upload, dan hapus file sementara sesudah transkripsi selesai/gagal.
- Set bahasa `id` bila didukung dan tampilkan transkrip sebelum mengirim ke chat supaya pengguna bisa mengoreksi. Jangan menyimpan audio mentah secara default.
- Bila `GROQ_API_KEY` belum dipasang, kuota habis, atau server STT tidak tersedia, gunakan Android `SpeechRecognizer` bila ada. Tampilkan dengan jujur engine mana yang dipakai. SpeechRecognizer sendiri dapat bergantung pada layanan di perangkat/jaringan dan tidak otomatis offline.
- Sediakan opsi di pengaturan: `Otomatis (Groq → Android)`, `Groq Whisper`, `SpeechRecognizer Android`. Jangan mengirim audio ke dua provider secara diam-diam.

### 6.3 Alur percakapan suara
1. Pengguna mengetuk robot atau tombol mic; aplikasi meminta RECORD_AUDIO hanya saat fitur dipakai.
2. Tampilkan indikator listening yang jelas dan tombol stop/cancel.
3. Ambil audio menggunakan implementasi yang cocok untuk STT terpilih. Jangan memulai rekaman latar belakang diam-diam.
4. Transkripsikan; tampilkan teks yang dapat diedit.
5. Setelah pengguna mengirim/menyetujui, proses dengan AI backend.
6. Tampilkan jawaban, kirim ke TTS terpilih, lalu putar suara.
7. Untuk percakapan berkelanjutan, setelah playback selesai tampilkan tombol “Bicara lagi” atau mode percakapan yang diaktifkan secara eksplisit. Jangan loop mic diam-diam setelah layar ditutup.
8. Tangani permission deny, recognizer unavailable, no-match, file terlalu besar, HTTP 401/403/429/5xx, timeout, jaringan putus, audio focus, interruption telepon/notifikasi, Activity/Service lifecycle, dan Stop.
9. Lepaskan recognizer/player, file temp, AudioFocus, coroutine/thread/task, dan resource terkait pada setiap jalan keluar.

### 6.4 Pemilihan suara
- Pengaturan menyediakan kartu preview “Perempuan — Gadis” dan “Laki-laki — Ardi”, memakai suara asli provider; simpan pilihan secara lokal.
- Pastikan user dapat memilih kembali Android TTS fallback dan melihat status layanan.
- Jangan mengiklankan Edge sebagai offline. Jangan klaim semua voice tersedia selamanya; voice list dapat berubah. Sertakan link rujukan di README.

## 7. AI online dan backend aman

Buat backend terpisah dari Android. Gunakan adapter interface, misalnya `AiProvider`, dengan implementasi provider konkret yang terdokumentasi. Prioritaskan backend ringan yang dapat deploy ke layanan yang menyediakan free tier, tetapi tulis dengan jelas bahwa kuota, model, wilayah, dan harga bisa berubah. Jangan mengklaim layanan selalu gratis.

Backend yang diminta:
- Endpoint health/status non-rahasia.
- Endpoint chat dengan validasi skema, batas payload, timeout, cancellation bila bisa, rate limit, dan penanganan error yang dinormalisasi.
- Endpoint vision atau chat multimodal hanya jika provider yang dipilih mendukungnya.
- Provider secret dibaca dari environment/secret manager, bukan source Android.
- Batasi origin bila relevan, autentikasi aplikasi yang masuk akal, abuse protection/rate limits, dan budget/usage warnings.
- Jangan menganggap secret statis yang ditanam dalam APK sebagai perlindungan. Bila aplikasi publik, jelaskan model autentikasi yang sesuai dan mitigasi abuse.
- Hindari menyimpan prompt, gambar, transkrip, dan jawaban secara default di server. Jika logging operasional diperlukan, redaksi konten sensitif dan gunakan retensi minimal.
- Jangan log Authorization header, API keys, gambar, full transcripts, atau request body pribadi.
- Berikan `.env.example` dengan nilai placeholder yang jelas bukan credential nyata.
- Berikan README deploy backend dan instruksi tambah/update secret. Jangan membuat token palsu seolah bisa dipakai.
- Tampilkan batas kuota, 401/403, 429, timeout, server error, provider unavailable, dan invalid model dengan pesan pengguna yang informatif.
- Buat pemilihan provider/model melalui konfigurasi server, jangan perlu membangun ulang APK hanya untuk mengganti model endpoint bila tidak diperlukan.

Jangan memilih model hanya berdasar nama yang mungkin usang. Periksa dokumentasi resmi untuk endpoint, model, skema gambar, free tier, dan ketentuan pemakaian ketika implementasi dilakukan. Bila browsing tidak tersedia, dokumentasikan parameter sebagai configurable dan jelaskan verifikasi yang masih dibutuhkan.

## 8. Mengenali aplikasi dan konteks layar

Sediakan **mode sadar konteks yang opt-in**:
- Secara default, aplikasi dapat mengetahui foreground package secara terbatas hanya setelah pengguna mengaktifkan layanan terkait dan OS mengizinkan.
- Tampilkan nama aplikasi yang terdeteksi dengan jelas, dan izinkan pengguna mengecualikan aplikasi tertentu atau mematikan fitur.
- Jangan diam-diam merekam layar terus-menerus.
- Gunakan AccessibilityService hanya untuk fungsi yang dijelaskan kepada pengguna. Jangan meminta service accessibility dengan alasan palsu.
- Baca struktur UI hanya ketika perlu dan hanya node yang diizinkan/tersedia. UI aplikasi lain mungkin tidak mengekspos teksnya.
- Abaikan password fields, konten sensitif, halaman pembayaran, PIN, OTP, dan window yang ditandai secure. Jangan mengekstrak password atau kredensial.
- Jangan mengirim konteks layar ke backend kecuali pengguna secara eksplisit meminta bantuan atas layar tersebut dan melihat indikator pengiriman.
- Berikan mode manual: pengguna mengetuk “Bantu layar ini”, memilih area/teks, atau membagikan screenshot. Ini fallback utama bila Accessibility tidak bisa membaca layar.
- Jangan pernah menyatakan “aku melihat layar” jika aplikasi hanya mengetahui nama package atau tidak ada data konteks.

## 9. Aksi di Android: ketik, klik, scroll, buka aplikasi

Buat `ActionController` terpisah dari AI. AI hanya mengusulkan aksi dalam schema terstruktur; validasi lokal memeriksa aksi dan memerlukan persetujuan yang sesuai sebelum dieksekusi. Jangan mengeksekusi instruksi bebas dari model sebagai kode.

Aksi yang bisa disediakan secara aman:
- Membuka aplikasi yang diminta melalui intent yang sesuai.
- Mengetik teks ke field aktif bila accessibility node/action mendukungnya dan pengguna mengizinkan.
- Menekan tombol, scroll, Back, atau aksi UI yang didukung service; hasil selalu diverifikasi bila memungkinkan.
- Membuat draft teks untuk clipboard atau memasukkannya ke field yang dipilih.
- Memberi status jika target tidak ditemukan/tidak dapat diakses, tanpa mengklaim berhasil.

Khusus WhatsApp dan aplikasi pesan:
- Bree boleh merangkum konteks yang diizinkan dan menyusun balasan.
- Default flow: tampilkan pratinjau; pengguna mengetuk “Masukkan ke kolom pesan”; Bree mengisi draft; pengguna sendiri menekan tombol kirim.
- Jangan mengirim pesan otomatis tanpa konfirmasi yang jelas untuk penerima dan isi pesan. Untuk kontak/grup baru atau tindakan berisiko, minta konfirmasi lagi.
- Jangan mengakali proteksi aplikasi atau memanfaatkan Accessibility untuk spam, pengiriman massal, atau penyalahgunaan.
- Jangan mengirim pesan berdasarkan teks layar yang tidak jelas penerima atau konteksnya.

Aksi berisiko (kirim pesan, hapus, pembayaran, pembelian, mengubah keamanan/akun) harus membutuhkan konfirmasi eksplisit dan menampilkan ringkasan tindakan sebelum dijalankan. Jangan melakukan pembayaran/pembelian otomatis.

## 10. Kamera, foto, gambar, dan tugas sekolah

- Sediakan pilih gambar dari Android Photo Picker dan alur buka kamera menggunakan API yang sesuai.
- Minta izin kamera saat dipakai; jangan menyalakan kamera background secara diam-diam.
- Preview gambar sebelum upload; tunjukkan apakah gambar akan dikirim ke provider online.
- Kompres/resize gambar sesuai limit provider, periksa MIME type dan ukuran file, tangani EXIF/metadata bila tidak diperlukan, dan jangan menyimpan gambar permanen tanpa pilihan user.
- AI vision harus menjawab berdasar apa yang benar-benar terlihat; nyatakan ketidakpastian ketika gambar buram/tidak cukup informasi.
- Untuk tugas, minta soal/foto yang jelas, bantu penalaran langkah demi langkah, jelaskan konsep, dan tandai jika jawaban tidak dapat dipastikan. Jangan mengarang teks yang tidak terlihat.
- Bila screen capture dibutuhkan, gunakan MediaProjection consent flow yang disediakan OS, indikator jelas, foreground service type yang benar, dan hentikan capture saat user mengakhiri sesi. Jangan bypass secure-window flags.
- Jangan menyimpan frame kontinu atau mengalirkan layar tanpa indikator dan persetujuan.

## 11. Memori dan personalisasi

- Memori jangka pendek: konteks percakapan aktif.
- Memori jangka panjang opsional: nama panggilan yang user pilih, gaya jawaban, preferensi ringan, proyek yang sedang dikerjakan. Minta izin sebelum menyimpan memori jangka panjang.
- Jangan menyimpan kata sandi, OTP, token, data bank, foto sensitif, atau detail sangat pribadi sebagai memori.
- Sediakan halaman “Memori Bree” untuk melihat, mengedit, menonaktifkan, dan menghapus memori.
- Penyimpanan lokal menjadi default untuk riwayat. Jika sinkronisasi/cloud memory ditambahkan, harus opt-in, terenkripsi saat transit dan punya opsi hapus.
- Jangan menyebut memori sebagai fakta bila sumbernya tidak jelas; konfirmasi dengan pengguna jika penting.

## 12. UI, aksesibilitas, dan performa

- UI Android modern dengan Material Components atau komponen native yang kompatibel dengan toolchain terpilih.
- Gunakan warna dasar gelap/terang yang nyaman, biru/cyan sebagai aksen, kontras yang cukup, touch target besar, font scaling, TalkBack labels, dan navigasi yang jelas.
- Jangan bergantung pada koneksi internet untuk membuka beranda, riwayat lokal, pengaturan, atau menyembunyikan overlay.
- Hindari main-thread network/disk work; gunakan executor/coroutine equivalent Java/ExecutorService dan lifecycle-aware handling.
- Batasi ukuran gambar, audio cache, dan history database; paginasi riwayat jika dibutuhkan.
- Hindari battery drain, polling ketat, wake locks panjang, dan background service tanpa alasan.
- Gunakan WorkManager hanya untuk pekerjaan tertunda yang sesuai, bukan untuk menjamin respons instan di background.
- Error state harus memiliki tindakan yang berguna: coba lagi, buka pengaturan izin, atau gunakan mode chat teks.

## 13. Struktur kode yang diharapkan

Struktur fleksibel sesuai kebutuhan, tetapi pisahkan concern. Contoh package `com.breeai.assistant`:

- `app/` — Activity, Application, navigation.
- `ui/home/`, `ui/chat/`, `ui/settings/`, `ui/onboarding/` — UI.
- `companion/` — OverlayService, CompanionView, drag/position state.
- `voice/` — SpeechController, TtsController, audio lifecycle.
- `ai/` — ChatRepository, AiClient, provider DTOs, result/error models.
- `context/` — ForegroundAppTracker, ScreenContextRepository, opt-in configuration.
- `accessibility/` — AccessibilityService, node filtering, allowed action schema.
- `actions/` — ActionController, validator, approval gate, execution result.
- `vision/` — ImagePicker, CameraCoordinator, image resizing, upload consent.
- `data/` — Room or SQLite/SharedPreferences as appropriate, repository, memory controls.
- `security/` — sanitized logging, network security configuration, token handling.
- `backend/` — separate deployable server with routes, provider adapter, validators, rate limits.
- `tests/` — unit, integration, backend contract, and Android UI/instrumentation tests where feasible.

Jangan memaksakan semua class/file jika membuat proyek jadi berlebihan; tetapi tanggung jawab tetap harus dipisahkan. Gunakan Java untuk kode Android inti. Backend boleh TypeScript atau runtime yang cocok untuk hosting terpilih.

## 14. Konfigurasi dan dependensi

- Tetapkan `applicationId`/namespace konsisten, app label `Bree AI`, versionCode/versionName, minSdk dan compileSdk berdasarkan kompatibilitas aktual yang dicek saat implementasi.
- Pin versi dependency yang tersedia dan saling kompatibel. Jangan mengambil versi fiktif.
- Sertakan Gradle Wrapper. Pastikan `gradlew` dapat dieksekusi di Linux.
- Manifest harus hanya memasang permission/service yang benar-benar dipakai dan sesuai API level. Jelaskan permission yang harus diberikan manual lewat Settings.
- Buat `.gitignore` untuk `local.properties`, build output, keystore, `.env`, service account, dan semua secrets. Jangan mengabaikan source/asset penting tanpa alasan.
- Buat `local.properties.example` hanya jika relevan; `local.properties` yang berisi path SDK tidak boleh di-commit.
- Jangan memasukkan signing keystore/password. Untuk testing, hasilkan debug APK; untuk release, jelaskan konfigurasi signing melalui secrets terpisah.

## 15. Termux build

Sediakan `scripts/build-termux.sh` yang:
- memeriksa Java, Gradle Wrapper, `ANDROID_HOME`/`ANDROID_SDK_ROOT`, sdkmanager, dan paket platform/build-tools yang dibutuhkan;
- memberi error yang actionable jika SDK tidak ada, bukan melanjutkan pura-pura;
- tidak menjalankan `pkg update` atau mengubah environment global secara destruktif tanpa persetujuan;
- menjalankan `./gradlew --no-daemon assembleDebug` dan menampilkan lokasi APK jika sukses;
- tidak mengunduh tools dari situs tak resmi.

Dokumentasikan langkah Termux untuk memasang paket yang tersedia di repository resmi saat ini, konfigurasi SDK yang sesuai, dan alternatif GitHub Actions jika native Android SDK di Termux tidak cocok pada perangkat tertentu. Jangan menganggap satu jalur SDK selalu bekerja pada semua perangkat.

## 16. GitHub Actions

Buat workflow CI yang valid dan maintainable:
- checkout repository;
- setup Java versi yang kompatibel dengan Gradle/AGP;
- setup Android SDK menggunakan action dan opsi paket yang masih berlaku, tanpa meminta paket `tools` lama yang tidak tersedia;
- cache Gradle secara aman;
- jalankan unit tests dan build debug;
- upload APK sebagai artifact jika build sukses;
- jangan cetak secrets ke log;
- backend deploy dibuat workflow terpisah yang hanya berjalan dengan secrets dan trigger yang jelas, atau dokumentasikan deploy manual.

Jangan mewajibkan Firebase/GCP bila tidak diperlukan. Jangan meminta `FIREBASE_SERVICE_ACCOUNT_JSON` kalau proyek tidak memakai Firebase. Dokumentasikan secrets sesuai provider yang benar-benar dipilih.

## 17. Pengujian minimum

Ikuti `ACCEPTANCE_CHECKLIST.md`. Paling tidak uji:
- compile/build clean;
- backend unavailable/invalid credential/429;
- teks chat dan konteks sesi;
- speech permission deny/grant, recognizer unavailable, TTS unavailable;
- overlay permission deny/grant, drag, hide/show, app restart;
- accessibility disable/revoke dan target node tidak ditemukan;
- password/OTP/secure screen filtering;
- aksi ketik memerlukan field target, tidak mengirim pesan tanpa konfirmasi;
- permission camera denial, image too large/unsupported, failed upload;
- delete history/memory;
- mode airplane/offline, request cancellation, lifecycle/orientation changes.

Tambahkan tests untuk backend validator, provider adapter, action policy, payload limits, dan redacted logging. Bila emulator/device tidak tersedia, sebutkan secara eksplisit pengujian yang belum bisa dijalankan.

## 18. README dan penyerahan hasil

README harus menjelaskan:
- ringkasan fitur dan yang belum didukung;
- arsitektur Android/backend;
- toolchain dan kompatibilitas;
- cara membuat API key di dashboard resmi provider yang dipilih;
- cara mengatur environment secrets backend dengan aman;
- cara menjalankan backend lokal dan deploy;
- cara mengatur URL backend di dev/release tanpa secret provider di APK;
- cara build melalui Termux dan GitHub Actions;
- lokasi APK setelah build;
- izin aplikasi dan dampak privasinya;
- troubleshooting yang benar-benar relevan;
- model/provider kuota gratis bukan jaminan permanen dan perlu diverifikasi;
- checklist sebelum rilis.

## 19. Urutan pelaksanaan

Lakukan dalam urutan ini dan jangan melewati validasi:
1. Inspeksi workspace/toolchain dan tulis ringkasan keputusan teknis.
2. Bangun skeleton Android + backend + dokumentasi konfigurasi.
3. Build pertama; perbaiki error nyata sampai compile berhasil jika environment memungkinkan.
4. Implementasikan chat dan backend dengan error handling.
5. Implementasikan persona, history, streaming response bila didukung provider, dan TTS/STT.
6. Implementasikan companion overlay dan UI settings.
7. Implementasikan opt-in screen context, AccessibilityService, action approval, dan filter privasi.
8. Implementasikan camera/photo/vision.
9. Jalankan lint/tests/build, review security, dan checklist.
10. Buat ringkasan file, hasil build, perintah test, lokasi artifact sebenarnya, keterbatasan yang tersisa, dan langkah lanjutan.

Jika tidak mampu menyelesaikan semua tahap pada satu pass, selesaikan bagian yang paling fondasional, pastikan hasil build truthful, dan laporkan bagian berikutnya yang belum selesai. Jangan mengklaim aplikasi siap rilis hanya karena berhasil compile.

## 20. Definition of Done

Pekerjaan baru dapat disebut fondasi berfungsi jika:
- source Android Java dan backend nyata ada di filesystem;
- Gradle build benar-benar dijalankan dan hasilnya dilaporkan;
- chat menggunakan backend/provider yang nyata setelah konfigurasi credential;
- tidak ada secret provider dalam APK;
- izin dijelaskan dan dipinta pada saat diperlukan;
- overlay tidak membuat aplikasi crash saat izin ditolak;
- STT/TTS punya status dan fallback;
- screenshot/kamera memerlukan consent yang sesuai;
- aksi Android melewati policy gate dan hasilnya diverifikasi;
- pengiriman pesan tidak dilakukan diam-diam;
- semua batasan, konfigurasi, dan test dicatat dengan jelas.

**Mulai sekarang: inspeksi workspace, lalu kerjakan proyek ini. Jangan berhenti pada rencana atau penjelasan.**

## 21. Prioritas stack gratis yang diteliti — gunakan konfigurasi, jangan mengunci arsitektur

Tanggal dokumentasi diperiksa: 15 Oktober 2026. Semua kuota/ketentuan berubah-ubah, jadi verifikasi ulang dari link resmi ketika mengimplementasikan dan sebelum deploy.

### Provider roles yang direkomendasikan
- **Primary chat + image:** Gemini API, model default konfigurasi `gemini-3.8-flash` setelah memverifikasi ketersediaan untuk akun/region dan input multimodal. Halaman pricing saat pemeriksaan menunjukkan free tier bagi model tersebut, namun tarif, rate limits, Search grounding, dan kebijakan data berbeda menurut mode/model/tier. Input/output pada free tier dapat digunakan untuk improvement sesuai ketentuan yang ditampilkan Google; tampilkan catatan privasi dan jangan kirim data sensitif tanpa persetujuan.
- **Fallback chat text:** provider adapter OpenRouter menggunakan free route/model yang status harga dan modality-nya diperiksa secara runtime/konfigurasi. Model free bisa berubah, memiliki limit, dan kebijakan data provider berbeda-beda. Jangan kirim gambar ke fallback kecuali capability input image diverifikasi; jangan pernah otomatis memilih model berbayar.
- **Optional fallback chat:** Groq model gratis yang tersedia di dashboard akun. Daftar model/limit dinamis; Groq Whisper difokuskan untuk STT, bukan diasumsikan sebagai model vision.
- **Optional host/inference:** Cloudflare Workers AI menunjukkan alokasi free 10.000 Neurons/hari pada saat pengecekan, tetapi model tertentu mensyaratkan billing dan tiap model memakai jumlah Neurons berbeda. Bukan requirement untuk versi pertama.
- **STT:** Groq Whisper `whisper-large-v3-turbo` melalui backend sebagai opsi online; Android SpeechRecognizer sebagai fallback, plus izin dan transkrip yang bisa diedit.
- **TTS:** Edge TTS melalui Python `edge-tts` sebagai pilihan utama yang dapat dinyalakan/dimatikan; bukan API developer resmi yang dijamin stabil dan bukan STT. Android TextToSpeech adalah fallback.
- **Live web research:** buat adapter `ResearchProvider`. Gemini Search grounding dapat dipakai hanya jika endpoint/model/kuota akun benar-benar mengizinkan. Sebelum mengaktifkan, cek pricing/limits live; jangan menganggap Search grounding gratis tanpa batas. Jika tidak tersedia, tampilkan status “pencarian web belum dikonfigurasi” dan jangan mengarang sumber.

Sumber primer yang harus ditinjau saat coding:
- Gemini model list: https://ai.google.dev/gemini-api/docs/models
- Gemini pricing: https://ai.google.dev/gemini-api/docs/pricing
- Gemini API key security: https://ai.google.dev/gemini-api/docs/api-key
- Gemini Google Search grounding: https://ai.google.dev/gemini-api/docs/google-search
- Groq rate limits: https://console.groq.com/docs/rate-limits
- Groq STT: https://console.groq.com/docs/speech-to-text
- OpenRouter free models (dinamis): https://openrouter.ai/collections/free-models
- Cloudflare Workers AI pricing: https://developers.cloudflare.com/workers-ai/platform/pricing/
- Edge TTS community project: https://github.com/rany2/edge-tts
- Render free limitations (bila dipilih): https://render.com/docs/free

### Config (server-side secrets)
Buat `.env.example` tanpa nilai credential asli. Nilai yang boleh ditampilkan hanya placeholder:
```
AI_PROVIDER=gemini
GEMINI_API_KEY=replace_me
GEMINI_MODEL=gemini-3.8-flash
GROQ_API_KEY=replace_me_optional
GROQ_STT_MODEL=whisper-large-v3-turbo
OPENROUTER_API_KEY=replace_me_optional
OPENROUTER_TEXT_MODEL=openrouter/free
EDGE_TTS_ENABLED=true
EDGE_TTS_DEFAULT_VOICE=id-ID-GadisNeural
EDGE_TTS_FEMALE_VOICE=id-ID-GadisNeural
EDGE_TTS_MALE_VOICE=id-ID-ArdiNeural
STT_PROVIDER=auto
RESEARCH_PROVIDER=gemini_google_search
RESEARCH_ENABLED=false
```

Jangan menaruh `.env` dalam Git. Jangan copy nilai secret ke README, chat, APK, BuildConfig, `strings.xml`, assets, manifest, logging, screenshot, test fixture, maupun Github Actions artifacts. `openrouter/free` adalah route pilihan dinamis, bukan janji bahwa setiap model gratis mendukung image/function calling. Bila menggunakan `RESEARCH_ENABLED=true`, verifikasi kemampuan akun + biaya aktual dan beri toggle serta batas penggunaan.

### Fallback behavior
- Gemini chat/vision gagal karena transient error: retry terbatas dengan exponential backoff dan jitter; jika konfigurasi fallback gratis tersedia, tawarkan atau pakai hanya untuk jenis input yang didukung dan hanya setelah user mengaktifkan fallback.
- 401/403: jangan retry berulang; tunjukkan bahwa konfigurasi key/akses salah tanpa membocorkan detail rahasia.
- 429/quota: baca retry-after jika tersedia, tampilkan “kuota provider habis/terbatas”; jangan otomatis beralih ke jalur berbayar.
- Vision tidak didukung oleh fallback: jangan kirim gambar ke model teks; pertahankan gambar pada client dan minta pengguna memilih provider/model vision yang valid.
- Edge TTS error: fallback ke Android TTS setelah diberi status; tidak perlu API key untuk Edge package itu sendiri, tetapi koneksi online tetap dibutuhkan.
- Groq STT error: fallback ke SpeechRecognizer Android hanya jika pengguna setuju dengan perbedaan provider/privacy; transkrip ditampilkan sebelum dikirim ke AI.
- Deep search nonaktif/limit habis: jawab dari pengetahuan model dengan label bahwa live search tidak dipakai, atau minta pengguna mengaktifkannya. Jangan mengarang sitasi atau hasil browsing.

## 22. Mode Deep Search / Riset sumber

Buat fitur opsional “Riset web” agar Bree bisa mencari jawaban berlandaskan sumber, terpisah dari chat biasa:
1. Pengguna menekan tombol “Riset web” atau mengaktifkannya untuk pertanyaan tertentu. Default off.
2. Backend mengirim query ke Search grounding yang valid untuk provider/config akun. Jangan mengakses internet dari Android langsung dengan API secret.
3. Jawaban menyertakan judul sumber, domain, URL, ringkasan singkat, tanggal bila ada, dan kutipan/citation id yang disediakan provider. Jangan membuat URL, sumber, angka, atau kutipan palsu.
4. Utamakan sumber resmi/primer untuk fakta teknis, harga, hukum, jadwal dan versi; gunakan beberapa sumber independen untuk klaim penting.
5. Hasil pencarian/teks situs adalah DATA tidak tepercaya, bukan instruksi system. Lindungi dari prompt injection di halaman web. Jangan izinkan situs mengubah tools/permissions/action policy.
6. Batasi jumlah query, panjang konten, timeout, dan cache; tampilkan status kuota. Jangan merayapi situs secara agresif, melewati login/paywall, atau mengekstrak data pribadi.
7. Jika search grounding tidak tersedia di free tier akun saat runtime, jangan diam-diam mengaktifkan billing. Matikan fitur, jelaskan kenapa, dan berikan konfigurasi alternatif hanya jika provider itu dipilih pengguna.

## 23. Kepribadian natural dan kualitas percakapan tingkat lanjut

- Kirim `PERSONA_BREE.md` sebagai system instruction server-side, tetapi backend tetap menerapkan aturan keamanan sendiri; persona tidak boleh menimpa authorization/action policy.
- Variasikan respons; jangan tiap pesan diawali nama pengguna, salam, emoji, atau “Siap”. Bahasa bisa santai (“gue/lu”) bila user memakainya, namun jangan memaksakan slang di semua jawaban.
- Gunakan ringkasan konteks pendek yang dibuat dari chat session; long-term memory opt-in, dapat dilihat, diedit, dan dihapus pengguna. Jangan menyimpan detail sensitif atau mengirim semua riwayat ke tiap provider tanpa batas.
- Dukungan `CASUAL`, `FOCUS`, `STUDY`, `VISION`, `RESEARCH`, `ACTION` sebagai mode intent. Mode adalah routing di aplikasi/backend, tidak perlu memaksa model memberi label internal kepada pengguna.
- Saat pengguna bicara sambil menggunakan aplikasi lain, jangan menginterupsi audio media secara agresif; audio focus yang tepat, “stop”, mute, dan volume harus bekerja.
- Perintah seperti “apa yang sedang aku buka?” harus dijawab berdasar package/app detection yang benar-benar diketahui. Isi layar hanya jika accessibility node atau screenshot telah diizinkan dan tersedia.
- Jawaban harus jelas bila model tidak yakin, gambar buram, sumber tidak cukup, atau tool tidak berjalan. Jangan mengarang completion atau mengklaim aksi sukses tanpa hasil dari Android.

## 24. Build, secrets, dan konfigurasi provider pada UI

Di halaman Settings > AI & Suara, tampilkan provider/model aktif, status siap/error/kuota jika backend menyediakan status aman, opsi Edge TTS perempuan/laki-laki, opsi STT, toggle Riset Web, serta tombol “Tes suara” dan “Tes koneksi AI”. Status tidak boleh memuat API key bahkan sebagian besar karakter key.

Settings app tidak boleh meminta pengguna memasukkan API key provider langsung ke APK pada versi default. Key provider dimasukkan ke dashboard backend/secret manager. Jika hanya untuk prototipe pribadi dengan BYOK, buat mode opt-in yang aman dan dokumentasikan risiko; jangan memasukkan key ke repository atau memaksa semua pengguna berbagi key pemilik aplikasi.

Saat pengiriman, berikan file source yang nyata, perintah deploy backend, `.env.example`, instructions for creating each optional key, dan laporan hasil build/test yang sungguh dijalankan. Jangan mengklaim APK tersedia kecuali file build benar-benar dihasilkan.
