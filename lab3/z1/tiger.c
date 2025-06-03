 
#include <stdlib.h>
#include <string.h>



typedef char const* (*PTRFUN)();


typedef struct Parrot{
  PTRFUN* _vptr;
  // vtable entries:
  // 0: char const* name(void* this);
  // 1: char const* greet();
  // 2: char const* menu();
  char* name;
} Parrot;




char const* greet(){
	return "rawr!";
}
char const* menu(){
	return "zebre";
}

char const* name(Parrot *p){
	return p->name;
}

PTRFUN vtbl[3] = {name, greet, (char const* (*) ()) menu};

void* create(char const* name) {
    Parrot *p = malloc(sizeof(Parrot));
    p->_vptr = vtbl;
    p->name = malloc( ( strlen(name) + 1 )* sizeof(char));
    strcpy(p->name, name);
    return p;
}