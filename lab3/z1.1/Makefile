

all: main parrot tiger

main: main.c myfactory.c
	gcc main.c myfactory.c -ldl

parrot: parrot.c
	gcc -shared -fPIC parrot.c -o parrot.so

tiger: tiger.c
	gcc -shared -fPIC tiger.c -o tiger.so

clean: 
	rm a.out *.so