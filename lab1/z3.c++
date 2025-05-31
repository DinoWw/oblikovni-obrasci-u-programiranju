#include <stdio.h>

#pragma pack(push, 1)
class CoolClass{
public:
  virtual void set(int x){x_=x;};
  virtual int get(){return x_;};
private:
  int x_;
};
#pragma pack(pop)
#pragma pack(push, 1)
class PlainOldClass{
public:
  void set(int x){x_=x;};
  int get(){return x_;};
private:
  int x_;
};
#pragma pack(pop)

int main(){
  printf(
    "sizeof(CoolClass) == %ld\n\
sizeof(PlainOldClass) == %ld\n",
     sizeof(CoolClass), sizeof(PlainOldClass));
}  

