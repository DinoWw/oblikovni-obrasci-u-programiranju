#include "myfactory.h"

#include <stdio.h>
#include <stdlib.h>

typedef char const* (*PTRFUN)(...);

struct Animal{
  PTRFUN* _vptr;
  // vtable entries:
  // 0: char const* name(void* this);
  // 1: char const* greet();
  // 2: char const* menu();
};

// parrots and tigers defined in respective dynamic libraries

// animalPrintGreeting and animalPrintMenu similar as in lab 1

void animalPrintGreeting(struct Animal *animal) {
	printf("%s pozdravlja: %s\n", animal->_vptr[0](animal), (animal->_vptr[1])());
}
void animalPrintMenu(struct Animal *animal) {
	printf("%s voli %s\n", animal->_vptr[0](animal), (animal->_vptr[2])());
}

int main(int argc, char *argv[]){
  for (int i=0; i<argc/2; ++i){
    struct Animal* p=(struct Animal*)myfactory(argv[1+2*i], argv[1+2*i+1]);
    if (!p){
      printf("Creation of plug-in object %s failed.\n", argv[1+2*i]);
      continue;
    }

    animalPrintGreeting(p);
    animalPrintMenu(p);
    free(p); 
  }
}
