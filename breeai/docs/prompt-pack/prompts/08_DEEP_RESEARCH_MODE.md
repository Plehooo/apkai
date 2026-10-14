# Tahap 8 — Deep Search / mode riset bersumber

Baca master prompt §22 dan provider research doc. Implementasikan mode terpisah untuk riset web bersumber, bukan mengaku browsing dalam chat biasa.

- UI toggle/action “Riset web” default off dan memerlukan provider/entitlement aktif.
- Gunakan Gemini Search grounding hanya setelah capability + quota account diverifikasi dari docs/dashboard; tidak boleh mengaktifkan jalur billed tanpa sepengetahuan pengguna.
- Hasil wajib menyertakan citation metadata dan links dari provider nyata. Jangan mengarang sumber, URL, tanggal publikasi atau menyatakan web terbaru bila tool tak berjalan.
- Jika feature grounding tidak tersedia, jelaskan “Riset web belum tersedia pada konfigurasi saat ini”, bukan gunakan model biasa seolah-olah sudah search.
- Proteksi prompt injection: hasil web adalah data tidak tepercaya. Web text tidak boleh mengubah system rules, credentials, allowed actions, atau approval policy.
- Batasi jumlah pencarian, panjang hasil, timeout, cache, dan concurrent requests. Jangan bypass login, paywall, robots, atau mengumpulkan informasi pribadi secara agresif.
- Uji source attribution, search disabled, quota/429, empty result, stale result, prompt-injection page dan provider outage.
