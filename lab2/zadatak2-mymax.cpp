#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <vector>
#include <string>

typedef int (*cmpf)(const void *, const void *);

const void* mymax(
    const void *base, size_t nmemb, size_t size,
    int (*compar)(const void *, const void *)
) {
    const int8_t* bp = (int8_t*) base;
    const int8_t* max =  bp;
    
    for(size_t offset = 0; offset < nmemb * size; offset += size) {
        if(compar(bp + offset, max)){ 
            max = bp + offset;
        }
    }

    return max;
    
}

int gt_int(const int *e1, const int *e2) {
    return *e1 > *e2;
}
int gt_char(const char *e1, const char *e2) {
    return *e1 > *e2;
}
int gt_str(const char *e1, const char *e2) {
    return strcmp(e1, e2) > 0;
}




int main() {
    int arr_int[] = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    char arr_char[]="Suncana strana ulice";
    const char* arr_str[] = {
       "Gle", "malu", "vocku", "poslije", "kise",
       "Puna", "je", "kapi", "pa", "ih", "njise"
    };
    std::vector<int> vec_int = { 1, 3, 5, 7, 4, 6, 9, 2, 0 };
    std::string str_char = "Suncana strana ulice";

    int j = 0;
    int d = 2;

    printf("max int:    %d\n", *(int*) mymax(arr_int, sizeof(arr_int)/sizeof(arr_int[0]), sizeof(arr_int[0]), (cmpf)gt_int));
    printf("max char:   %c\n", *(char*) mymax(arr_char, sizeof(arr_char)/sizeof(arr_char[0]), sizeof(arr_char[0]), (cmpf)gt_char));
    printf("max string: %s\n", *(char**) mymax(arr_str, sizeof(arr_str)/sizeof(arr_str[0]), sizeof(arr_str[0]), (cmpf)gt_str));
    printf("max vec:    %d\n", *(int*) mymax(&vec_int[0], vec_int.size(), sizeof(vec_int[0]), (cmpf)gt_int));
    printf("max string char: %c\n", *(char*) mymax(&str_char[0], str_char.length(), sizeof(str_char[0]), (cmpf)gt_char));
}