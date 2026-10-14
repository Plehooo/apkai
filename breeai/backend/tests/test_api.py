import pytest
from fastapi.testclient import TestClient
import app.main as main

client = TestClient(main.app)


def test_health_never_returns_api_key(monkeypatch):
    monkeypatch.setenv("GEMINI_API_KEY", "super-secret-test-value")
    response = client.get("/health")
    assert response.status_code == 200
    body = response.json()
    assert body["status"] == "ok"
    assert body["provider_configured"] is True
    assert "super-secret-test-value" not in response.text
    assert "GEMINI_API_KEY" not in response.text


def test_chat_requires_server_side_key(monkeypatch):
    monkeypatch.delenv("GEMINI_API_KEY", raising=False)
    response = client.post("/v1/chat", json={"messages": [{"role": "user", "content": "Halo"}]})
    assert response.status_code == 503
    assert "belum dikonfigurasi" in response.json()["detail"]


def test_chat_requires_latest_message_from_user():
    response = client.post("/v1/chat", json={"messages": [{"role": "assistant", "content": "Halo"}]})
    assert response.status_code == 422


def test_chat_rejects_empty_or_oversized_history():
    empty = client.post("/v1/chat", json={"messages": []})
    long_payload = client.post("/v1/chat", json={"messages": [{"role": "user", "content": "x" * 3001}]})
    assert empty.status_code == 422
    assert long_payload.status_code == 422


def test_chat_adapter_calls_fixed_gemini_endpoint(monkeypatch):
    monkeypatch.setenv("GEMINI_API_KEY", "test-key-not-real")
    monkeypatch.setenv("GEMINI_MODEL", "gemini-test-model")

    class FakeResponse:
        status_code = 200
        def json(self):
            return {"modelVersion": "gemini-test-model", "candidates": [{"content": {"parts": [{"text": "Hai, bre!"}]}}]}

    class FakeAsyncClient:
        def __init__(self, *args, **kwargs):
            self.timeout = kwargs.get("timeout")
        async def __aenter__(self):
            return self
        async def __aexit__(self, exc_type, exc, tb):
            return False
        async def post(self, url, headers, json):
            assert url == main.GEMINI_ENDPOINT.format(model="gemini-test-model")
            assert headers["x-goog-api-key"] == "test-key-not-real"
            assert json["contents"][-1]["parts"][0]["text"] == "Halo Bree"
            assert "systemInstruction" in json
            return FakeResponse()

    monkeypatch.setattr(main.httpx, "AsyncClient", FakeAsyncClient)
    response = client.post("/v1/chat", json={"messages": [{"role": "user", "content": "Halo Bree"}]})
    assert response.status_code == 200
    assert response.json()["reply"] == "Hai, bre!"
    assert "test-key-not-real" not in response.text


def test_tts_rejects_unknown_voice():
    response = client.post("/v1/tts", json={"text": "Hai", "voice": "arbitrary-voice"})
    assert response.status_code == 422


def test_tts_reports_missing_optional_package(monkeypatch):
    monkeypatch.setenv("EDGE_TTS_ENABLED", "true")
    monkeypatch.setattr(main, "edge_tts", None)
    response = client.post("/v1/tts", json={"text": "Hai", "voice": "id-ID-GadisNeural"})
    assert response.status_code == 503
    assert "tidak terpasang" in response.json()["detail"]


def test_stt_requires_key_before_upstream_call(monkeypatch):
    monkeypatch.delenv("GROQ_API_KEY", raising=False)
    response = client.post("/v1/stt", files={"file": ("voice.wav", b"audio", "audio/wav")})
    assert response.status_code == 503
