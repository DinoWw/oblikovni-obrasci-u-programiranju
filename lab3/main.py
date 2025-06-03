import os
import importlib


def myfactory(moduleName: str):
    # module = importlib.import_module(f"{moduleName}", None)
    module = importlib.import_module(f".{moduleName}", "plugins")
    return getattr(module, "create")
    # return module.__getattr__("create")

def printGreeting(pet):
  pet.greet()

def printMenu(pet):
  pet.menu()


def test():
  pets=[]
  # obiđi svaku datoteku kazala plugins 
  for mymodule in os.listdir('plugins'):
    moduleName, moduleExt = os.path.splitext(mymodule)
    # ako se radi o datoteci s Pythonskim kodom ...
    if moduleExt=='.py':
      # instanciraj ljubimca ...
      ljubimac=myfactory(moduleName)('Ljubimac '+str(len(pets)))
      # ... i dodaj ga u listu ljubimaca
      pets.append(ljubimac)

  # ispiši ljubimce
  for pet in pets:
    printGreeting(pet)
    printMenu(pet)

if(__name__ == "__main__"):
  test()