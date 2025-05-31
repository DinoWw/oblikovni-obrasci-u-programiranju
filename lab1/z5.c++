#include <stdio.h>

class B{
public:
  virtual int prva()=0;
  virtual int druga(int)=0;
};

class D: public B{
public:
  virtual int prva(){return 42;}
  virtual int druga(int x){return prva()+x;}
};

typedef int (*PRVAFUN)(B*);
typedef int (*DRUGAFUN)(B*, int);

void printPrvaDruga(B *pb) {
    printf("%u\n", pb);
    PRVAFUN prva = ( (PRVAFUN*) pb ) [0];
    printf("%u\n", prva);
    
    printf("prva: %d\n", prva(pb));
}

int main() {
    // D *d = new D();
    D d;
    printPrvaDruga(&d);
}
