#include "myfactory.h"

#include <dlfcn.h>
#include <stdio.h>
#include <stdlib.h>

#include <string.h>

void* myfactory(char const* libname, char const* ctorarg){
    void *handle;

    static size_t len = strlen(".so");
    char *path = malloc( (strlen("./") + strlen(libname) + len + 1) * sizeof(char) ) ;
    sprintf(path, "./%s.so", libname);

    handle = dlopen(path, RTLD_LAZY);
    if (!handle) {
        return NULL;
    }


    typeof(void* (char const*)) *create;

    create = (typeof(void* (char const*)) *) dlsym(handle, "create");

    return create(ctorarg);


}