from abc import ABC, abstractmethod # Abstract Base Class
import datetime
import statistics
from time import sleep

class SlijedBrojeva:
    def __init__(self, source: 'Izvor'):
        self.source = source
        self.subscribers = []
        self.sljed = []

    def add_subscriber(self, subscriber: 'Akcija'):
        self.subscribers.append(subscriber)

    def kreni(self):
        n = self.source.get()
        while(n != -1):
            self.sljed.append(n)
            for sub in self.subscribers:
                sub.process(self.sljed)
            sleep(1)
            n = self.source.get()

class Izvor(ABC):
    @abstractmethod
    def get(self) -> int:
        pass

class TipkovnickiIzvor(Izvor):
    def get(self) -> int:
        return int(input("Unesi broj: "))

class DatotecniIzvor(Izvor):
    def __init__(self, filename):
        self.file = open(filename, "r")

    def get(self) -> int:
        l = self.file.readline().strip()
        if(l == ""):
            return -1
        return int(l)

class Akcija(ABC):
    @abstractmethod
    def process(self, arr: list[int]):
        pass

class FileWriter(Akcija):
    def __init__(self, filename):
        self.file = open(filename, "w")

    def process(self, arr):
        for n in arr:
            self.file.write(str(n) + "\n")
        self.file.write( str( datetime.datetime.now() ) + "\n" )


class SumPrinter(Akcija):
    def process(self, arr):
        print(f"Suma: {sum(arr)}")

class AvgPrinter(Akcija):
    def process(self, arr):
        print(f"Avg: {statistics.mean(arr)}")

class MedianPrinter(Akcija):
    def process(self, arr):
        print(f"Median: {statistics.median(arr)}")


def main():
    izvorT: Izvor = TipkovnickiIzvor()
    izvorF: Izvor = DatotecniIzvor("zadatak5.in")

    sum = SumPrinter()
    avg = AvgPrinter()
    med = MedianPrinter()
    file = FileWriter("zadatak5.out")

    slijed: SlijedBrojeva = SlijedBrojeva(izvorF)
    slijed.add_subscriber(sum)
    slijed.add_subscriber(avg)
    slijed.add_subscriber(med)
    slijed.add_subscriber(file)
    slijed.kreni()

    slijed: SlijedBrojeva = SlijedBrojeva(izvorT)
    slijed.add_subscriber(sum)
    slijed.add_subscriber(avg)
    slijed.add_subscriber(med)
    slijed.add_subscriber(file)
    slijed.kreni()

    



if __name__ == "__main__":
    main()
