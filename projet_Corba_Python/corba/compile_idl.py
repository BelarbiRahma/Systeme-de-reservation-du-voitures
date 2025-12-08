import sys
import os
from omniORB import CORBA
from omniORB import CosNaming
from omniORB import assurance_idl


class CORBAClient:
    def __init__(self):
        self.orb = CORBA.ORB_init(sys.argv, CORBA.ORB_ID)
        obj = self.orb.resolve_initial_references("NameService")
        rootContext = obj._narrow(CosNaming.NamingContext)
        name = [CosNaming.NameComponent("AssuranceService", "")]
        self.service = rootContext.resolve(name)._narrow(assurance_idl.ExtraService)

    def test(self):
        print("🧪 Test du service CORBA:")
        print(f"   Info: {self.service.getInfoService()}")
        print(f"   Taxe sur 100€: {self.service.calcularTaxe(100.0)}€")
        print(f"   Assurance basique 15000€: {self.service.calcularCoutAssurance('basique', 15000.0)}€")


if __name__ == '__main__':
    client = CORBAClient()
    client.test()