import sys
import os
from omniORB import CORBA, PortableServer
import CosNaming
import assurance_idl


class ExtraService_i(assurance_idl.ExtraServicePOA):
    def calcularTaxe(self, montant):
        return montant * 0.05

    def calcularCoutAssurance(self, typeAssurance, valeurVehicule):
        if typeAssurance.lower() == "complet":
            taux = 0.07
        elif typeAssurance.lower() == "intermediaire":
            taux = 0.05
        else:
            taux = 0.03

        return (valeurVehicule * taux) + 50.0

    def getInfoService(self):
        return "Service CORBA Assurance Plus v1.0"

    def estDisponible(self):
        return True


def main():
    orb = CORBA.ORB_init(sys.argv, CORBA.ORB_ID)
    poa = orb.resolve_initial_references("RootPOA")
    servant = ExtraService_i()
    poa.activate_object(servant)

    # Service de noms
    obj = orb.resolve_initial_references("NameService")
    rootContext = obj._narrow(CosNaming.NamingContext)

    name = [CosNaming.NameComponent("AssuranceService", "")]
    rootContext.rebind(name, servant._this())

    print("✅ Serveur CORBA démarré")
    print("📡 Service: AssuranceService")
    orb.run()


if __name__ == '__main__':
    main()