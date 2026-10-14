package com.breeai.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String PREFS = "bree_settings";
    private static final String BACKEND_URL = "backend_url";
    private static final int BG = Color.rgb(15, 19, 30);
    private static final int PANEL = Color.rgb(25, 31, 46);
    private static final int CYAN = Color.rgb(112, 221, 235);
    private static final int WHITE = Color.rgb(238, 243, 250);
    private static final int MUTED = Color.rgb(157, 170, 190);

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final ArrayList<JSONObject> history = new ArrayList<>();
    private SharedPreferences prefs;
    private LinearLayout chatColumn;
    private ScrollView chatScroll;
    private EditText input;
    private Button sendButton;
    private Button voiceButton;
    private TextView connectionStatus;
    private boolean sending = false;
    private boolean voiceEnabled = false;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        Window w = getWindow();
        w.setStatusBarColor(BG);
        w.setNavigationBarColor(BG);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("id", "ID"));
                ttsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED;
            }
        });
        buildUi();
        addBubble("Bree", "Hai bre! Aku sudah berjalan di aplikasi ini. Untuk chat AI sungguhan, tekan Server lalu masukkan URL backend Bree AI. Aku tidak akan berpura-pura tersambung bila server belum aktif.", false);
        if (!backendUrl().isEmpty()) checkBackend(false);
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(10), dp(16), dp(10));
        root.setBackgroundColor(BG);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);
        TextView mascot = new TextView(this);
        mascot.setText("✦"); mascot.setTextColor(CYAN); mascot.setTextSize(30); mascot.setGravity(Gravity.CENTER);
        mascot.setBackground(round(PANEL, 18));
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(dp(48), dp(48));
        header.addView(mascot, mp);

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(12), 0, 0, 0);
        TextView title = new TextView(this); title.setText("BREE AI"); title.setTextColor(WHITE); title.setTextSize(18); title.setTypeface(null, Typeface.BOLD);
        connectionStatus = new TextView(this); connectionStatus.setText("Backend belum dicek"); connectionStatus.setTextColor(MUTED); connectionStatus.setTextSize(12);
        titles.addView(title); titles.addView(connectionStatus);
        header.addView(titles, new LinearLayout.LayoutParams(0, -2, 1));
        Button settings = smallButton("Server"); settings.setOnClickListener(v -> showBackendDialog()); header.addView(settings);
        root.addView(header);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(0, dp(12), 0, dp(10));
        voiceButton = smallButton("Suara: OFF");
        voiceButton.setOnClickListener(v -> {
            voiceEnabled = !voiceEnabled;
            voiceButton.setText(voiceEnabled ? "Suara: ON" : "Suara: OFF");
            if (voiceEnabled && !ttsReady) Toast.makeText(this, "Engine suara Indonesia tidak tersedia di HP ini.", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, voiceEnabled ? "Balasan akan dibacakan dengan TTS Android." : "Suara dimatikan.", Toast.LENGTH_SHORT).show();
        });
        Button clear = smallButton("Hapus chat");
        clear.setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Hapus percakapan?").setMessage("Riwayat sesi yang tersimpan sementara akan dihapus.").setNegativeButton("Batal", null).setPositiveButton("Hapus", (d, which) -> {
            history.clear(); chatColumn.removeAllViews(); addBubble("Bree", "Chat sudah dibersihkan, bre.\nRiwayat percakapan tidak disimpan setelah aplikasi ditutup.", false);
        }).show());
        actions.addView(voiceButton);
        LinearLayout.LayoutParams clearParams = new LinearLayout.LayoutParams(-2, dp(38));
        clearParams.leftMargin = dp(8);
        actions.addView(clear, clearParams);
        root.addView(actions);

        chatScroll = new ScrollView(this);
        chatScroll.setFillViewport(false);
        chatColumn = new LinearLayout(this);
        chatColumn.setOrientation(LinearLayout.VERTICAL);
        chatColumn.setPadding(0, dp(4), 0, dp(8));
        chatScroll.addView(chatColumn, new ScrollView.LayoutParams(-1, -2));
        root.addView(chatScroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout composer = new LinearLayout(this);
        composer.setOrientation(LinearLayout.HORIZONTAL);
        composer.setGravity(Gravity.CENTER_VERTICAL);
        composer.setPadding(dp(8), dp(6), dp(6), dp(6));
        composer.setBackground(round(PANEL, 18));
        input = new EditText(this);
        input.setTextColor(WHITE); input.setHintTextColor(MUTED); input.setHint("Tulis pesan ke Bree...");
        input.setTextSize(15); input.setSingleLine(false); input.setMaxLines(4);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setBackgroundColor(Color.TRANSPARENT); input.setPadding(dp(8), dp(8), dp(8), dp(8));
        composer.addView(input, new LinearLayout.LayoutParams(0, -2, 1));
        sendButton = new Button(this); sendButton.setText("Kirim"); sendButton.setTextColor(BG); sendButton.setAllCaps(false); sendButton.setTypeface(null, Typeface.BOLD); sendButton.setBackground(round(CYAN, 14));
        composer.addView(sendButton, new LinearLayout.LayoutParams(dp(82), dp(44)));
        sendButton.setOnClickListener(v -> sendMessage());
        root.addView(composer, new LinearLayout.LayoutParams(-1, -2));

        TextView privacy = new TextView(this);
        privacy.setText("Chat dikirim ke backend yang kamu atur. Jangan kirim password, OTP, atau data sensitif.");
        privacy.setTextSize(10); privacy.setTextColor(MUTED); privacy.setPadding(dp(2), dp(7), dp(2), 0);
        root.addView(privacy);
        setContentView(root);
    }

    private void sendMessage() {
        if (sending) return;
        String message = input.getText().toString().trim();
        if (message.isEmpty()) return;
        if (backendUrl().isEmpty()) {
            new AlertDialog.Builder(this).setTitle("Atur backend dulu").setMessage("Bree butuh URL HTTPS backend agar bisa memberikan jawaban AI sungguhan.").setNegativeButton("Nanti", null).setPositiveButton("Atur server", (d, w) -> showBackendDialog()).show();
            return;
        }
        input.setText("");
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(input.getWindowToken(), 0);
        addBubble("Kamu", message, true);
        try {
            JSONObject item = new JSONObject(); item.put("role", "user"); item.put("content", message); history.add(item);
        } catch (Exception ignored) { }
        trimHistory();
        JSONArray payloadMessages = new JSONArray();
        for (JSONObject item : history) payloadMessages.put(item);
        JSONObject payload = new JSONObject();
        try { payload.put("messages", payloadMessages); } catch (Exception ignored) { }
        sending = true; sendButton.setEnabled(false); sendButton.setText("...");
        TextView waiting = addBubble("Bree", "Sedang menghubungi backend...", false);
        io.execute(() -> {
            String answer = null; String failure = null;
            try {
                JSONObject response = postJson(backendUrl() + "/v1/chat", payload);
                answer = response.optString("reply", "").trim();
                if (answer.isEmpty()) throw new Exception("Backend tidak mengembalikan teks jawaban.");
            } catch (Exception e) { failure = humanError(e); }
            final String finalAnswer = answer; final String finalFailure = failure;
            runOnUiThread(() -> {
                if (waiting.getParent() instanceof View) {
                    View waitingWrapper = (View) waiting.getParent();
                    if (waitingWrapper.getParent() == chatColumn) chatColumn.removeView(waitingWrapper);
                }
                sending = false; sendButton.setEnabled(true); sendButton.setText("Kirim");
                if (finalAnswer != null) {
                    try { JSONObject item = new JSONObject(); item.put("role", "assistant"); item.put("content", finalAnswer); history.add(item); } catch (Exception ignored) { }
                    addBubble("Bree", finalAnswer, false);
                    if (voiceEnabled && ttsReady && tts != null) tts.speak(finalAnswer, TextToSpeech.QUEUE_FLUSH, null, "bree-reply");
                } else {
                    if (!history.isEmpty() && "user".equals(history.get(history.size() - 1).optString("role"))) {
                        history.remove(history.size() - 1);
                    }
                    addBubble("Koneksi bermasalah", finalFailure + "\nPeriksa URL backend, koneksi internet, dan konfigurasi API key pada server.", false);
                }
            });
        });
    }

    private void trimHistory() {
        int chars = 0;
        for (JSONObject item : history) chars += item.optString("content", "").length();
        while ((history.size() > 20 || chars > 12000) && history.size() > 2) {
            JSONObject first = history.remove(0);
            chars -= first.optString("content", "").length();
            // Remove the matching assistant reply too, so retained history starts on a user turn.
            if (!history.isEmpty() && "assistant".equals(history.get(0).optString("role"))) {
                JSONObject second = history.remove(0);
                chars -= second.optString("content", "").length();
            }
        }
    }

    private JSONObject postJson(String endpoint, JSONObject payload) throws Exception {
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL(endpoint).openConnection();
            c.setRequestMethod("POST"); c.setConnectTimeout(12000); c.setReadTimeout(45000); c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            byte[] bytes = payload.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = c.getOutputStream()) { os.write(bytes); }
            int code = c.getResponseCode();
            InputStream stream = code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream();
            String body = readBody(stream);
            if (code < 200 || code >= 300) {
                String detail = body;
                try { detail = new JSONObject(body).optString("detail", body); } catch (Exception ignored) { }
                throw new Exception("HTTP " + code + ": " + detail);
            }
            return new JSONObject(body);
        } finally { if (c != null) c.disconnect(); }
    }

    private String readBody(InputStream inputStream) throws Exception {
        if (inputStream == null) return "";
        StringBuilder b = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line; while ((line = r.readLine()) != null) b.append(line);
        }
        return b.toString();
    }

    private void showBackendDialog() {
        EditText field = new EditText(this);
        field.setSingleLine(true); field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        field.setHint("https://backend-kamu.example.com"); field.setText(backendUrl()); field.setSelectAllOnFocus(true);
        LinearLayout box = new LinearLayout(this); box.setPadding(dp(20), dp(4), dp(20), 0); box.addView(field, new LinearLayout.LayoutParams(-1, -2));
        new AlertDialog.Builder(this).setTitle("URL backend Bree AI")
            .setMessage("Masukkan base URL backend FastAPI, tanpa /v1/chat. Gunakan HTTPS untuk server publik.")
            .setView(box).setNegativeButton("Batal", null).setNeutralButton("Hapus", (d, which) -> { prefs.edit().remove(BACKEND_URL).apply(); connectionStatus.setText("Backend belum diatur"); })
            .setPositiveButton("Simpan & cek", (d, which) -> {
                String url = field.getText().toString().trim().replaceAll("/+$", "");
                if (!url.startsWith("https://") || url.contains(" ")) {
                    Toast.makeText(this, "Gunakan URL HTTPS agar koneksi ke backend terenkripsi.", Toast.LENGTH_LONG).show(); return;
                }
                prefs.edit().putString(BACKEND_URL, url).apply(); checkBackend(true);
            }).show();
    }

    private void checkBackend(boolean toast) {
        String base = backendUrl(); if (base.isEmpty()) return;
        connectionStatus.setText("Memeriksa backend...");
        io.execute(() -> {
            String status = "Backend tidak terjangkau"; boolean okay = false;
            HttpURLConnection c = null;
            try {
                c = (HttpURLConnection)new URL(base + "/health").openConnection(); c.setConnectTimeout(8000); c.setReadTimeout(8000); c.setRequestMethod("GET");
                int code = c.getResponseCode();
                if (code >= 200 && code < 300) { JSONObject j = new JSONObject(readBody(c.getInputStream())); okay = true; status = j.optString("status", "ok").equals("ok") ? "Backend online" : "Backend: " + j.optString("status"); }
                else status = "Health check HTTP " + code;
            } catch (Exception e) { status = humanError(e); }
            finally { if (c != null) c.disconnect(); }
            final String label = status; final boolean connected = okay;
            runOnUiThread(() -> { connectionStatus.setText(label); if (toast) Toast.makeText(this, connected ? "Backend merespons." : "Belum terhubung: " + label, Toast.LENGTH_LONG).show(); });
        });
    }

    private String backendUrl() { return prefs.getString(BACKEND_URL, "").trim().replaceAll("/+$", ""); }

    private TextView addBubble(String speaker, String text, boolean user) {
        LinearLayout wrapper = new LinearLayout(this); wrapper.setOrientation(LinearLayout.VERTICAL); wrapper.setPadding(0, dp(5), 0, dp(5));
        wrapper.setGravity(user ? Gravity.RIGHT : Gravity.LEFT);
        TextView label = new TextView(this); label.setText(speaker); label.setTextColor(user ? CYAN : MUTED); label.setTextSize(11); label.setPadding(dp(5), 0, dp(5), dp(4));
        TextView body = new TextView(this); body.setText(text); body.setTextColor(WHITE); body.setTextSize(15); body.setLineSpacing(dp(2), 1.03f); body.setPadding(dp(13), dp(10), dp(13), dp(10)); body.setBackground(round(user ? Color.rgb(33, 58, 77) : PANEL, 16));
        wrapper.addView(label);
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(-2, -2);
        bodyParams.width = Math.min(getResources().getDisplayMetrics().widthPixels - dp(70), (int)(getResources().getDisplayMetrics().widthPixels * 0.84f));
        wrapper.addView(body, bodyParams);
        chatColumn.addView(wrapper, new LinearLayout.LayoutParams(-1, -2));
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
        return body;
    }

    private Button smallButton(String text) {
        Button b = new Button(this); b.setText(text); b.setAllCaps(false); b.setTextSize(12); b.setTextColor(WHITE); b.setPadding(dp(10), 0, dp(10), 0); b.setBackground(round(PANEL, 12));
        return b;
    }
    private GradientDrawable round(int color, int radius) { GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d; }
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
    private String humanError(Exception e) {
        String m = e.getMessage(); if (m == null || m.trim().isEmpty()) return "Permintaan gagal.";
        if (m.contains("UnknownHostException")) return "Host backend tidak ditemukan. Periksa URL dan internet.";
        if (m.contains("timeout") || m.contains("timed out")) return "Backend timeout. Coba lagi setelah server siap.";
        return m;
    }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        io.shutdownNow();
        super.onDestroy();
    }
}
