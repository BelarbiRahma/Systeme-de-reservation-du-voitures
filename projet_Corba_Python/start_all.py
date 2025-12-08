import subprocess
import time
import sys
import os


def start_service(name, script, delay=2):
    print(f"🚀 Démarrage de {name}...")

    if sys.platform == "win32":
        process = subprocess.Popen(
            [sys.executable, script],
            creationflags=subprocess.CREATE_NEW_CONSOLE
        )
    else:
        process = subprocess.Popen(
            [sys.executable, script],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )

    time.sleep(delay)
    return process


def main():
    print("=" * 60)
    print("   DÉMARRAGE DES SERVICES PYTHON")
    print("=" * 60)

    # Démarrer Flask
    flask_process = start_service("Flask Microservice", "app_reporting.py")

    print("\n✅ Services démarrés!")
    print("📊 Flask: http://localhost:5000")
    print("\n📋 Endpoints:")
    print("   - GET /api/stats/global")
    print("   - GET /api/stats/ventes")
    print("   - GET /health")

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\n👋 Arrêt des services...")
        flask_process.terminate()


if __name__ == "__main__":
    main()