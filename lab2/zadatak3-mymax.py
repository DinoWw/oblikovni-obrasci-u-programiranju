def mymax(iterable, key=lambda x:x):
  # incijaliziraj maksimalni element i maksimalni ključ
  max_x=max_key=None

  # obiđi sve elemente
  for x in iterable:
    # ako je key(x) najveći -> ažuriraj max_x i max_key
    if( max_key is None or key(x) > max_key ):
      max_key = key(x)
      max_x = x

  # vrati rezultat
  return max_x


def main():
    str_list = ["element 1", "el 2", "clan 3", "stavka 4", "string 5"]
    print("najdulji: ", mymax(str_list, lambda x: len(x)) )

    maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
    maxchar = mymax("Suncana strana ulice")
    maxstring = mymax([
    "Gle", "malu", "vocku", "poslije", "kise",
    "Puna", "je", "kapi", "pa", "ih", "njise"
    ])
    print("int", maxint)
    print("char", maxchar)
    print("string", maxstring)

    ## radi jer `for in` iterira kroz kljuceve koje onda prosljedjuje D.get funkciji kao D.get(kljuc)
    D={'burek':8, 'buhtla':5}
    print("rjecnik ", mymax(D, D.get))

    kolekcija_osoba = [("dino", "plecko"), ("matej", "matic"), ("ivan", "papic")]
    print("osoba", mymax(kolekcija_osoba, lambda x: x[1] + "\0" + x[0] ))




if __name__ == "__main__":
  main()