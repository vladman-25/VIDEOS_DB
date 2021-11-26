MAN ANDREI VLAD 321CD

TEMA 1 POO

Am folosit o structura Singleton pt Database.
In main imi transfer datele din input in Database, iar apoi apelez execut toate actiunile.

COMENZI
-FAVORITE
    verific daca userul are deja acel film in fav, daca nu, verific daca l-a vazut iar apoi il adaug
-VIEW
    incrementez value din map-ul history de la cheia filmului
-RATING
    verific daca a vazut filmul, apoi daca deja a rate-uit videoul iar daca nu il rateuiesc si retin nr total
    de ratings ale user ului si un map cu useri si nota in video

QUERIES
-ACTOR AVG
    calculez nota actorului astfel: iau toate filmele care contine acest actor, si le calculez media, daca au media
    peste 0 (adica minim un rating) se adauga la media aritmetica. Analog pt seriale
    apoi ii sortez in fct de nota si nume, crescator sau descrescator
-ACTOR AWARDS
    iau toti actorii si verific daca au toate premiile primite ca parametru
    apoi ii sortez in fct de nr de premii si nume
-ACTOR DESC
    iau toti actorii si verific daca au cuvintele respective in descriere
    apoi ii sortez alfabetic

pt toate: iau toate videourile care au ca an/gen filtrele date ca parametru
-MOVIES/SHOWS RATINGS
    sortez in fct de nota medie si alfabetic
-MOVIES/SHOWS FAVORITES
    calculez pt fiecare video de cate ori apare in favorite si le sortez in fct de asta si alfabetic
-MOVIES/SHOWS LONGEST
    sortez in fct de lungime
-MOVIES/SHOWS MOST VIEWED
    calculez pt fiecare video de cate ori a fost vazut si le sortez in fct de asta si alfabetic
-USERS
    ii sortez in functie de nr de ratings, calculat cu apelarea comenzilor de RATING

RECOMANDARI
-STANDARD
    caut in lista de filme primul film nevazut, daca nu se gaseste niciunu atunci nu se poate aplica
-BEST
    se calculeaza ratingurile iar apoi se sorteaza in fct de medie
-SEARCH
    se cauta filmele nevbazute si cu genre-ul dat iar apoi sunt sortate in fct de rating si alfabetic
-POPULAR
    se calculeaza nr de vizionari pt toate videourile
    se creeaza un map cu toate genurile si se adauga fiecare gen al fiecarui film + nr de vizionari
    se sorteaza genurile
    se iau pe rand genurile si filmele si se verifica daca nu au fost vazute si daca au acel gen
    daca se gaseste vreun film se afiseaza altfel nu se poate aplica
-FAVORITE
    se calculeaza nr de favorite iar apoi se sorteaza
