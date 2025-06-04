class Tiger:
    def __init__(self, name):
        self.name = name


    def greet(self):
        print(f"{self.name} pozdravlja: Sto mu gromova")
        
    def menu(self):
        print(f"{self.name} voli brazilske orahe.")



def create(name:str):
    return Tiger(name)

