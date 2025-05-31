from abc import ABC, abstractmethod # Abstract Base Class
from numpy import random

class DistributionTester :
    generator: 'DistributionGenerator'
    seeker: 'DistributionSeeker'
    numbers: list[int]

    def __init__(self, generator, seeker):
        self.generator = generator
        self.seeker = seeker
        self.numbers = self.generator.generate()

    def print10(self):
        for p in range(0, 100, 10):
            print( f"percentile {p}: { self.seeker.seek(self.numbers, p) }" )

    def printP(self, p):
        print( f"percentile {p}: { self.seeker.seek(self.numbers, p) }" )

class DistributionGenerator(ABC) :
    @abstractmethod
    def generate(self) -> list[int]:
        pass

class UniformDistributionGenerator(DistributionGenerator): 
    def __init__(self, lb, ub, step):
        self.lb = lb
        self.ub = ub
        self.step = step
        
    def generate(self) -> list[int]:
        return list( range(self.lb, self.ub, self.step) )

class NormalDistributionGenerator(DistributionGenerator): 
    def __init__(self, e, sigma, count):
        self.e = e 
        self.sigma = sigma
        self.count = count

    def generate(self) -> list[int]:
        return random.normal(self.e, self.sigma, self.count)

class FibonacciDistributionGenerator(DistributionGenerator): 
    def __init__(self, count):
        self.count = count

    def generate(self) -> list[int]:
        fib = [1, 1] # will not work for count < 2
        for i in range(2, self.count):
            fib.append(fib[i-1] + fib[i-2])
        return fib
    
class DistributionSeeker(ABC):
    @abstractmethod
    def seek(self, arr, p):
        pass

class RoundingDistributionSeeker(DistributionSeeker):
    def seek(self, arr, p):
        return sorted(arr)[ round( len(arr) * p/100 ) ]

class InrepolatedDistributionSeeker(DistributionSeeker):
    def seek(self, arr, p):
        srt = sorted(arr)

        if(p <= 0):
            return srt[0]
        if(p >= 100):
            return srt[-1]

        N = len(arr)
        for i in range(len(arr) -1):
            prvi = 100 * ( (i+1) - 0.5 ) / N
            drugi = 100 * ( (i+2) - 0.5 ) / N
            if( p > prvi and p < drugi ): 
                return srt[i] + N * (p-prvi) * (srt[i+1] - srt[i]) / 100

def main():
    generator: DistributionGenerator = UniformDistributionGenerator(0, 100, 5)
    seeker: DistributionSeeker = RoundingDistributionSeeker()
    tester: DistributionTester = DistributionTester(generator, seeker)

    print("uniform 0, 100, 10; rounding")
    tester.print10()
    tester.printP(14)
    tester.printP(16)
    print()

    generator: DistributionGenerator = NormalDistributionGenerator(100, 40, 100)
    seeker: DistributionSeeker = InrepolatedDistributionSeeker()
    tester: DistributionTester = DistributionTester(generator, seeker)
    print("normal 100, 40, 100; interpolated")
    tester.print10()
    tester.printP(14)
    tester.printP(16)
    print()

    generator: DistributionGenerator = FibonacciDistributionGenerator(30)
    seeker: DistributionSeeker = InrepolatedDistributionSeeker()
    tester: DistributionTester = DistributionTester(generator, seeker)
    print("fibonacci 30; interpolated")
    tester.print10()
    tester.printP(14)
    tester.printP(16)

if __name__ == "__main__" :
    main()



