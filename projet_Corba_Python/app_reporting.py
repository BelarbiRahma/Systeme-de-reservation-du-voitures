from flask import Flask, jsonify, request
from flask_cors import CORS
from datetime import datetime, timedelta
import random

app = Flask(__name__)
CORS(app)


def get_simulated_stats():
    return {
        "revenuTotalMois": 15500.75,
        "revenuTotalAnnee": 189500.25,
        "reservationsMois": 47,
        "tauxOccupation": 78.5,
        "topMarques": [
            {"marque": "Hyundai", "reservations": 45, "revenu": 12450.50},
            {"marque": "Renault", "reservations": 32, "revenu": 8760.25},
            {"marque": "Kia", "reservations": 28, "revenu": 9450.00}
        ],
        "clientsFrequents": [
            {"client": "Jean Dupont", "reservations": 12, "montantTotal": 4200.00},
            {"client": "Marie Martin", "reservations": 8, "montantTotal": 3100.50},
            {"client": "Pierre Durand", "reservations": 6, "montantTotal": 2450.75}
        ],
        "statutVoitures": {
            "disponibles": 12,
            "reservees": 8,
            "enMaintenance": 3
        }
    }


@app.route('/api/stats/global', methods=['GET'])
def get_global_stats():
    return jsonify(get_simulated_stats())


@app.route('/api/stats/ventes', methods=['GET'])
def get_sales_stats():
    months = ["Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
              "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"]

    ventes = []
    for month in months:
        ventes.append({
            "mois": month,
            "chiffreAffaire": random.randint(10000, 20000),
            "reservations": random.randint(30, 60)
        })

    return jsonify({"ventesParMois": ventes})


@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({
        "status": "healthy",
        "service": "reporting-microservice",
        "timestamp": datetime.now().isoformat()
    })


if __name__ == '__main__':
    print(" Microservice Flask démarré sur http://localhost:5000")
    app.run(host='0.0.0.0', port=5000, debug=True)