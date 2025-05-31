#include <stdlib.h>
#include <stdio.h>

typedef char const* (*PTRFUN)();

struct Animal {
	const char *name;
	PTRFUN *_vptr;	
};

char const* dogGreet(void){
	return "vau!";
}
char const* dogMenu(void){
	return "kuhanu govedinu";
}
char const* catGreet(void){
	return "mijau!";
}
char const* catMenu(void){
	return "konzerviranu tunjevinu";
}

PTRFUN dogVtable[2] = {dogGreet, dogMenu};
PTRFUN catVtable[2] = {catGreet, catMenu};

void constructDog(struct Animal *mem, const char *name) {
	mem->name = name;
	mem->_vptr = dogVtable;
}
void constructCat(struct Animal *mem, const char *name) {
	mem->name = name;
	mem->_vptr = catVtable;
}

void animalPrintGreeting(struct Animal *animal) {
	printf("%s pozdravlja: %s\n", animal->name, (animal->_vptr[0])());
}
void animalPrintMenu(struct Animal *animal) {
	printf("%s voli %s\n", animal->name, (animal->_vptr[1])());
}

struct Animal *createDog(const char *name){
	struct Animal *animal = malloc(sizeof(struct Animal));
	constructDog(animal, name); 
	return animal;
}
struct Animal *createCat(const char *name){
	struct Animal *animal = malloc(sizeof(struct Animal));
	constructCat(animal, name);
	return animal;
}

struct Animal* createDogs(int n, const char *name[]) {
	struct Animal *animals = malloc( n * sizeof(struct Animal) );

	for(int i = 0; i < n; i++) {
		constructDog(&animals[i], name[i]);
	}

	return animals;
}

void testAnimals(void){

	struct Animal* p1=createDog("Hamlet");
	struct Animal* p2=createCat("Ofelija");
	struct Animal* p3=createDog("Polonije");

	animalPrintGreeting(p1);
	animalPrintGreeting(p2);
	animalPrintGreeting(p3);

	animalPrintMenu(p1);
	animalPrintMenu(p2);
	animalPrintMenu(p3);

	free(p1); free(p2); free(p3);

	struct Animal p;
	constructCat(&p, "macka na stacku");
	animalPrintGreeting(&p);


	const char *names[] = {"sled_dog_1", "sled_dog_2", "sled_dog_3", "sled_dog_4"};
	struct Animal *dogs = createDogs(4, names);

	for(int i = 0; i < 4; i++) {
		animalPrintGreeting(&dogs[i]);
	}

}

int main(int argc, char *argv[]) {
	testAnimals();
}