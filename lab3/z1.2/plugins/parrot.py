class Parrot:
    def __init__(self, name):
        self.name = name

    def greet(self):
        print(f"{self.name} pozdravlja: mijau!")
        
    def menu(self):
        print(f"{self.name} voli mlako mlijeko.")




def create(name:str):
    return Parrot(name)

