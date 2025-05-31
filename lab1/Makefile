

all: z1 z3 z4full z5
z1: z1.c
	gcc -Wall -o z1.exe z1.c
z3: z3.c++
	g++ -Wall -o z3.exe z3.c++

z4: z4.c++
	g++ -S z4.c++
z4full: z4.c++
	g++ -fno-asynchronous-unwind-tables -fno-exceptions -fno-rtti -fverbose-asm -Wall -Wextra z4.c++ -O0 -masm=att -S 

z5: z5.c++
	g++ -Wall -o z5.exe z5.c++
	
clean:
	rm z1 z2 z3 *.exe