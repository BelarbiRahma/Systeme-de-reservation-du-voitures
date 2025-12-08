# Module assurance_idl généré manuellement à partir de assurance.idl

class AssuranceService:
    def __init__(self):
        self.clients = {}
    
    def getAssuranceInfo(self, clientId):
        return f"Info assurance pour client {clientId}"
    
    def checkCoverage(self, clientId, riskType):
        return True
    
    def calculatePremium(self, clientId, amount):
        return amount * 0.05

class GestionAssurance(AssuranceService):
    def ajouterClient(self, nom, prenom):
        client_id = len(self.clients) + 1
        self.clients[client_id] = {'nom': nom, 'prenom': prenom}
        return client_id
    
    def modifierCouverture(self, idClient, nouvelleCouverture):
        if idClient in self.clients:
            self.clients[idClient]['couverture'] = nouvelleCouverture
            return True
        return False
    
    def genererRapport(self, idClient):
        if idClient in self.clients:
            return f"Rapport pour client {idClient}: OK"
        return "Client non trouvé"

print('Module assurance_idl chargé (manuel)')
