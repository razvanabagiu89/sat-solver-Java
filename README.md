# SAT solver

## Task 1 : Plantarea Spionilor

Acest task se refera de fapt la problema colorarii unui graf - K color. Astfel,
familiile sunt nodurile, relatiile sunt muchiile iar spionii reprezinta culorile
grafului.
In functia formulateOracleQuestion() am impus 3 constrangeri referitoare la 
generarea de clauze:

1) fiecare familie trebuie sa aiba asignata neaparat un spion, ceea ce inseamna
ca o familie nu poate ramane fara spion.

2) fiecare familie trebuie sa aiba maxim un spion, ceea ce se traduce si din
constrangerea 1) ca o familie poate avea exact un spion.

3) familiile care se inteleg intre ele, trebuie sa aiba spioni diferiti.

Pentru generarea clauzelor m-am folosit de matricea de adiacenta a grafului
formuland clauzele pentru formatul FNC printr-un StringBuilder pe care ulterior
l-am scris in fisier.

Complexitati:
1) O(nrVars), unde nrVars = N * K , dar N > K semnficiativ deci O(N)
2) Se folosesc 2 for loop-uri, in care din al doilea se face break fie cand
s-a atins numarul de combinatii, fie cand limita sa a fost atinsa. Astfel, 
pe worst-case cand se iese atunci cand s au atins numarul de combinatii adica
(K*(K-1))/2 iteratii, complexitatea este : O(nrVars) * O(K*(K-1))/2 , adica
O(N*K), unde N > K semnificativ deci putem reduce la O(N) * O((K*(K-1)/2)),
deci complexitatea totala va fi O(N*K(K-1)/2).
3) Primele 2 for-uri se duc pana la N (numarul de familii), iar al treilea for
imbricat va merge pana la K (nr de spioni), deci complexitatea este
O(N^2 * K)

Deci, complexitatea totala pentru generarea clauzelor este:
O(N) + O(N*K(K-1)/2) + O(N^2 * K)

Pentru decipher() am luat in considerare spionii intorsi de oracol care sunt
mai mari decat 0, insemnand ca acestia au fost asignati familiei respective.

## Task 2 : Investigatiile Familiilor Extinse

Acest task se refera de fapt la problema gasirii unei clica - K clique. Astfel,
familiile sunt nodurile, relatiile sunt muchiile iar familia extinsa reprezinta
clica gasita de K - dimensiunea familiei extinse noduri.

In functia formulateOracleQuestion() am impus 4 constrangeri referitoare la 
generarea de clauze:

1) Familia extinsa trebuie neaparat sa contina K familii, K fiind dimensiunea
familiei extinsa data din fisier la read()
2) Familiile ce nu au o relatie intre ele nu se pot afla simultan in familia
extinsa, deoarece familia extinsa trebuie sa fie un subgraf complet
3) O pozitie din familia extinsa nu poate fi ocupata de 2 familii in acelasi
timp
4) O familie nu poate sa apare de 2 ori in familia extinsa in acelasi timp

Pentru generarea clauzelor m-am folosit de matricea de adiacenta a grafului
formuland clauzele pentru formatul FNC printr-un StringBuilder analog taskului 1
, dar de asemenea folosind un HashMap in carea am stocat clauzele pentru a 
evita clauzele duplicate.

Complexitati:

1) O(nrVars), unde nrVars = N * K, dar N > K semnificativ deci O(N)
2) Primul for merge pana la N, iar al doilea porneste de la indexul curent
al lui I si merge tot pana la N. In worst case, aceste 2 for-uri luate
separat au complexitatea O(N^2). Apoi, pentru fiecare familie si corespondenta
ei din muchia care nu exista, mai exista 2 for-uri de la familia curenta
pana la numarul de variabile, dar iteratorul ambelor loop-uri
parcurge de fiecare data nrVars/N pasi, deci K pasi. Aceste doua for-uri in worst
case au complexitatea O(K^2). Inmultind complexitatile for-urilor imbricate
obtinem O(N^2) * O(K^2)
3) Cele 2 for-uri merg pana la nrVars, unde nrVars = N * K. Al doilea for
porneste de la indexul primului, dar din al doilea for se iese analog taskului
1 atunci cand s-a atins numarul de combinatii sau limita. Deci la N(N-1)/2
iteratii in worst case se va executa break. Deci, complexitatea celor doua este
O(N*K) * O((N*(N-1))/2), unde N > K deci putem reduce la O(N) * O((N*(N-1))/2)
4) Primul for parcurge N pasi, iar cele doua for-uri imbricate parcurg
nrVars/N pasi, adica K pasi. In worst case atunci cand al doilea for porneste
de la primul index al primului for, iar al treilea de la primul index al celui
de al doilea for, complexitatea va fi O(N) * O(K^2)

Deci , complexitatea totala pentru generarea clauzelor este:
O(N) + O(N^2) * O(K^2) + O(N) * O((N*(N-1))/2) + O(N) * O(K^2)


Pentru decipher() am luat in considerare familiile pozitive intoarse de oracol,
pe care le-am convertit la pozitia lor in familia extinsa si am scris in 
fisierul de out.


## Task 3 : Arestarile Mafiotilor

Acest task se refera la problema K-Vertex cover, iar reducerea sa la taskul 2
reprezinta reducerea lui K-vertex cover la K-clique. Logica din spatele 
implementarii mele este in felul urmator:
Citesc graf-ul dat la read() intr-o matrice de adiacenta ce reprezinta
familiile si relatiile dintre ele, apoi creez matricea de adiacenta pentru
graful complementar care semnifica familiile si relatiile care nu exista dintre
ele. Astfel, rezolvand K-clique (task2) pe graful complementar si gasind
cea mai mare familie extinsa pe muchiile care nu exista, in contrast cu graful
normal vom elimina toate familiile care nu sunt din familia extinsa de la taskul
2 si astfel vom gasi restul de familii ce trebuiesc arestate astfel incat sa nu
mai existe relatii intre ele. Ca acest algoritm sa fie optim, va trebui ca
familia extinsa in taskul 2 sa aiba cele mai mari dimensiuni posibile, ca 
numarul de arestari din taskul 3 sa fie complementul ei si sa fie minimul.

  
Implementare functii:
- reduceToTask2(Task2 task2)- aceasta functie primeste un obiect de tipul Task2
in care seteaza numarul de muchii al grafului complementar, M_prim. M_prim este
calculat prin diferenta a numarului total de muchii intr-un subgraf complet ->
N*(N-1)/2 - M (numarul actual de muchii). Complexitatea reducerii catre
taskul 2 doar din aceasta functie va fi reprezentata doar de parcurgerea
grafului si anume O(N^2). 

- solve() - se apeleaza reduceToTask2() pentru a seta datele necesare explicate
mai sus, iar apoi mai ramane de setat doar K - ce semnifica dimensiunea familiei
extinse de cautat. Astfel, printr-un for K se seteaza incepand cu numarul de 
familii totale (N) pana la final. Va exista intotdeauna o solutie, dar
algoritmul va tine cont doar de minimul dintre iteratii in care K va fi cel mai
mare posibil pentru care exista o solutie.
Complexitatea acestei functii va fi O(N^2) de la reduceToTask2() si O(N) in
worst case in care nu se gaseste familia extinsa printre primele iteratii.

Deci, complexitatea totala este:
O(N^2) + O(N) = O(N^2) 

## Project structure

```bash

├── checker_log.txt
├── checker.py
│   ├── README
│   └── src
│       └── java_implementation
│           ├── BonusTask.java
│           ├── Main.java
│           ├── Makefile
│           ├── Task1.java
│           ├── Task2.java
│           ├── Task3.java
│           └── Task.java
├── in
│   ├── bonus
│   │   ├── 00.in
│   │   ├── 01.in
│   │   ├── 02.in
│   │   ├── 03.in
│   │   ├── 04.in
│   │   ├── 05.in
│   │   ├── 06.in
│   │   ├── 07.in
│   │   ├── 08.in
│   │   ├── 09.in
│   │   ├── 10.in
│   │   ├── 11.in
│   │   ├── 12.in
│   │   ├── 13.in
│   │   ├── 14.in
│   │   ├── 15.in
│   │   ├── 16.in
│   │   ├── 17.in
│   │   ├── 18.in
│   │   ├── 19.in
│   │   ├── 20.in
│   │   ├── 21.in
│   │   ├── 22.in
│   │   ├── 23.in
│   │   ├── 24.in
│   │   ├── 25.in
│   │   ├── 26.in
│   │   ├── 27.in
│   │   ├── 28.in
│   │   ├── 29.in
│   │   ├── 30.in
│   │   ├── 31.in
│   │   ├── 32.in
│   │   ├── 33.in
│   │   ├── 34.in
│   │   └── custom.in
│   ├── task1
│   │   ├── 00.in
│   │   ├── 01.in
│   │   ├── 02.in
│   │   ├── 03.in
│   │   ├── 04.in
│   │   ├── 05.in
│   │   ├── 06.in
│   │   ├── 07.in
│   │   ├── 08.in
│   │   ├── 09.in
│   │   ├── 10.in
│   │   ├── 11.in
│   │   ├── 12.in
│   │   ├── 13.in
│   │   ├── 14.in
│   │   ├── 15.in
│   │   ├── 16.in
│   │   ├── 17.in
│   │   ├── 18.in
│   │   ├── 19.in
│   │   ├── 20.in
│   │   ├── 21.in
│   │   ├── 22.in
│   │   ├── 23.in
│   │   ├── 24.in
│   │   ├── 25.in
│   │   ├── 26.in
│   │   ├── 27.in
│   │   ├── 28.in
│   │   ├── 29.in
│   │   ├── 30.in
│   │   ├── 31.in
│   │   ├── 32.in
│   │   ├── 33.in
│   │   └── custom.in
│   ├── task2
│   │   ├── 00.in
│   │   ├── 01.in
│   │   ├── 02.in
│   │   ├── 03.in
│   │   ├── 04.in
│   │   ├── 05.in
│   │   ├── 06.in
│   │   ├── 07.in
│   │   ├── 08.in
│   │   ├── 09.in
│   │   ├── 10.in
│   │   ├── 11.in
│   │   ├── 12.in
│   │   ├── 13.in
│   │   ├── 14.in
│   │   ├── 15.in
│   │   ├── 16.in
│   │   ├── 17.in
│   │   ├── 18.in
│   │   ├── 19.in
│   │   ├── 20.in
│   │   ├── 21.in
│   │   ├── 22.in
│   │   ├── 23.in
│   │   ├── 24.in
│   │   ├── 25.in
│   │   ├── 26.in
│   │   ├── 27.in
│   │   ├── 28.in
│   │   ├── 29.in
│   │   ├── 30.in
│   │   ├── 31.in
│   │   ├── 32.in
│   │   ├── 33.in
│   │   └── custom.in
│   └── task3
│       ├── 00.in
│       ├── 01.in
│       ├── 02.in
│       ├── 03.in
│       ├── 04.in
│       ├── 05.in
│       ├── 06.in
│       ├── 07.in
│       ├── 08.in
│       ├── 09.in
│       ├── 10.in
│       ├── 11.in
│       ├── 12.in
│       ├── 13.in
│       ├── 14.in
│       ├── 15.in
│       ├── 16.in
│       ├── 17.in
│       ├── 18.in
│       ├── 19.in
│       ├── 20.in
│       ├── 21.in
│       ├── 22.in
│       ├── 23.in
│       ├── 24.in
│       ├── 25.in
│       ├── 26.in
│       ├── 27.in
│       ├── 28.in
│       ├── 29.in
│       ├── 30.in
│       ├── 31.in
│       ├── 32.in
│       ├── 33.in
│       ├── 34.in
│       └── custom.in
├── out
│   ├── bonus
│   ├── production
│   │   └── Tema-2-AA-skel
│   │       ├── BonusTask.class
│   │       ├── Main.class
│   │       ├── Makefile
│   │       ├── Task1.class
│   │       ├── Task2.class
│   │       ├── Task3.class
│   │       └── Task.class
│   ├── task1
│   │   ├── 00.out
│   │   ├── 01.out
│   │   ├── 02.out
│   │   ├── 03.out
│   │   ├── 04.out
│   │   ├── 05.out
│   │   ├── 06.out
│   │   ├── 07.out
│   │   ├── 08.out
│   │   ├── 09.out
│   │   ├── 10.out
│   │   ├── 11.out
│   │   ├── 12.out
│   │   ├── 13.out
│   │   ├── 14.out
│   │   ├── 15.out
│   │   ├── 16.out
│   │   ├── 17.out
│   │   ├── 18.out
│   │   ├── 19.out
│   │   ├── 20.out
│   │   ├── 21.out
│   │   ├── 22.out
│   │   ├── 23.out
│   │   ├── 24.out
│   │   ├── 25.out
│   │   ├── 26.out
│   │   ├── 27.out
│   │   ├── 28.out
│   │   ├── 29.out
│   │   ├── 30.out
│   │   ├── 31.out
│   │   ├── 32.out
│   │   └── 33.out
│   ├── task2
│   │   ├── 00.out
│   │   ├── 01.out
│   │   ├── 02.out
│   │   ├── 03.out
│   │   ├── 04.out
│   │   ├── 05.out
│   │   ├── 06.out
│   │   ├── 07.out
│   │   ├── 08.out
│   │   ├── 09.out
│   │   ├── 10.out
│   │   ├── 11.out
│   │   ├── 12.out
│   │   ├── 13.out
│   │   ├── 14.out
│   │   ├── 15.out
│   │   ├── 16.out
│   │   ├── 17.out
│   │   ├── 18.out
│   │   ├── 19.out
│   │   ├── 20.out
│   │   ├── 21.out
│   │   ├── 22.out
│   │   ├── 23.out
│   │   ├── 24.out
│   │   ├── 25.out
│   │   ├── 26.out
│   │   ├── 27.out
│   │   ├── 28.out
│   │   ├── 29.out
│   │   ├── 30.out
│   │   ├── 31.out
│   │   ├── 32.out
│   │   └── 33.out
│   └── task3
│       ├── 00.out
│       ├── 01.out
│       ├── 02.out
│       ├── 03.out
│       ├── 04.out
│       ├── 05.out
│       ├── 06.out
│       ├── 07.out
│       ├── 08.out
│       ├── 09.out
│       ├── 10.out
│       ├── 11.out
│       ├── 12.out
│       ├── 13.out
│       ├── 14.out
│       ├── 15.out
│       ├── 16.out
│       ├── 17.out
│       ├── 18.out
│       ├── 19.out
│       ├── 20.out
│       ├── 21.out
│       ├── 22.out
│       ├── 23.out
│       ├── 24.out
│       ├── 25.out
│       ├── 26.out
│       ├── 27.out
│       ├── 28.out
│       ├── 29.out
│       ├── 30.out
│       ├── 31.out
│       ├── 32.out
│       ├── 33.out
│       └── 34.out
├── README.md
├── ref
│   ├── bonus
│   │   ├── 00.ref
│   │   ├── 01.ref
│   │   ├── 02.ref
│   │   ├── 03.ref
│   │   ├── 04.ref
│   │   ├── 05.ref
│   │   ├── 06.ref
│   │   ├── 07.ref
│   │   ├── 08.ref
│   │   ├── 09.ref
│   │   ├── 10.ref
│   │   ├── 11.ref
│   │   ├── 12.ref
│   │   ├── 13.ref
│   │   ├── 14.ref
│   │   ├── 15.ref
│   │   ├── 16.ref
│   │   ├── 17.ref
│   │   ├── 18.ref
│   │   ├── 19.ref
│   │   ├── 20.ref
│   │   ├── 21.ref
│   │   ├── 22.ref
│   │   ├── 23.ref
│   │   ├── 24.ref
│   │   ├── 25.ref
│   │   ├── 26.ref
│   │   ├── 27.ref
│   │   ├── 28.ref
│   │   ├── 29.ref
│   │   ├── 30.ref
│   │   ├── 31.ref
│   │   ├── 32.ref
│   │   ├── 33.ref
│   │   └── 34.ref
│   ├── task1
│   │   ├── 00.ref
│   │   ├── 01.ref
│   │   ├── 02.ref
│   │   ├── 03.ref
│   │   ├── 04.ref
│   │   ├── 05.ref
│   │   ├── 06.ref
│   │   ├── 07.ref
│   │   ├── 08.ref
│   │   ├── 09.ref
│   │   ├── 10.ref
│   │   ├── 11.ref
│   │   ├── 12.ref
│   │   ├── 13.ref
│   │   ├── 14.ref
│   │   ├── 15.ref
│   │   ├── 16.ref
│   │   ├── 17.ref
│   │   ├── 18.ref
│   │   ├── 19.ref
│   │   ├── 20.ref
│   │   ├── 21.ref
│   │   ├── 22.ref
│   │   ├── 23.ref
│   │   ├── 24.ref
│   │   ├── 25.ref
│   │   ├── 26.ref
│   │   ├── 27.ref
│   │   ├── 28.ref
│   │   ├── 29.ref
│   │   ├── 30.ref
│   │   ├── 31.ref
│   │   ├── 32.ref
│   │   └── 33.ref
│   ├── task2
│   │   ├── 00.ref
│   │   ├── 01.ref
│   │   ├── 02.ref
│   │   ├── 03.ref
│   │   ├── 04.ref
│   │   ├── 05.ref
│   │   ├── 06.ref
│   │   ├── 07.ref
│   │   ├── 08.ref
│   │   ├── 09.ref
│   │   ├── 10.ref
│   │   ├── 11.ref
│   │   ├── 12.ref
│   │   ├── 13.ref
│   │   ├── 14.ref
│   │   ├── 15.ref
│   │   ├── 16.ref
│   │   ├── 17.ref
│   │   ├── 18.ref
│   │   ├── 19.ref
│   │   ├── 20.ref
│   │   ├── 21.ref
│   │   ├── 22.ref
│   │   ├── 23.ref
│   │   ├── 24.ref
│   │   ├── 25.ref
│   │   ├── 26.ref
│   │   ├── 27.ref
│   │   ├── 28.ref
│   │   ├── 29.ref
│   │   ├── 30.ref
│   │   ├── 31.ref
│   │   ├── 32.ref
│   │   └── 33.ref
│   └── task3
│       ├── 00.ref
│       ├── 01.ref
│       ├── 02.ref
│       ├── 03.ref
│       ├── 04.ref
│       ├── 05.ref
│       ├── 06.ref
│       ├── 07.ref
│       ├── 08.ref
│       ├── 09.ref
│       ├── 10.ref
│       ├── 11.ref
│       ├── 12.ref
│       ├── 13.ref
│       ├── 14.ref
│       ├── 15.ref
│       ├── 16.ref
│       ├── 17.ref
│       ├── 18.ref
│       ├── 19.ref
│       ├── 20.ref
│       ├── 21.ref
│       ├── 22.ref
│       ├── 23.ref
│       ├── 24.ref
│       ├── 25.ref
│       ├── 26.ref
│       ├── 27.ref
│       ├── 28.ref
│       ├── 29.ref
│       ├── 30.ref
│       ├── 31.ref
│       ├── 32.ref
│       ├── 33.ref
│       └── 34.ref
├── sat_in
│   ├── bonus
│   ├── task1
│   │   ├── 00.cnf
│   │   ├── 01.cnf
│   │   ├── 02.cnf
│   │   ├── 03.cnf
│   │   ├── 04.cnf
│   │   ├── 05.cnf
│   │   ├── 06.cnf
│   │   ├── 07.cnf
│   │   ├── 08.cnf
│   │   ├── 09.cnf
│   │   ├── 10.cnf
│   │   ├── 11.cnf
│   │   ├── 12.cnf
│   │   ├── 13.cnf
│   │   ├── 14.cnf
│   │   ├── 15.cnf
│   │   ├── 16.cnf
│   │   ├── 17.cnf
│   │   ├── 18.cnf
│   │   ├── 19.cnf
│   │   ├── 20.cnf
│   │   ├── 21.cnf
│   │   ├── 22.cnf
│   │   ├── 23.cnf
│   │   ├── 24.cnf
│   │   ├── 25.cnf
│   │   ├── 26.cnf
│   │   ├── 27.cnf
│   │   ├── 28.cnf
│   │   ├── 29.cnf
│   │   ├── 30.cnf
│   │   ├── 31.cnf
│   │   ├── 32.cnf
│   │   └── 33.cnf
│   ├── task2
│   │   ├── 00.cnf
│   │   ├── 01.cnf
│   │   ├── 02.cnf
│   │   ├── 03.cnf
│   │   ├── 04.cnf
│   │   ├── 05.cnf
│   │   ├── 06.cnf
│   │   ├── 07.cnf
│   │   ├── 08.cnf
│   │   ├── 09.cnf
│   │   ├── 10.cnf
│   │   ├── 11.cnf
│   │   ├── 12.cnf
│   │   ├── 13.cnf
│   │   ├── 14.cnf
│   │   ├── 15.cnf
│   │   ├── 16.cnf
│   │   ├── 17.cnf
│   │   ├── 18.cnf
│   │   ├── 19.cnf
│   │   ├── 20.cnf
│   │   ├── 21.cnf
│   │   ├── 22.cnf
│   │   ├── 23.cnf
│   │   ├── 24.cnf
│   │   ├── 25.cnf
│   │   ├── 26.cnf
│   │   ├── 27.cnf
│   │   ├── 28.cnf
│   │   ├── 29.cnf
│   │   ├── 30.cnf
│   │   ├── 31.cnf
│   │   ├── 32.cnf
│   │   └── 33.cnf
│   └── task3
│       ├── 00.cnf
│       ├── 01.cnf
│       ├── 02.cnf
│       ├── 03.cnf
│       ├── 04.cnf
│       ├── 05.cnf
│       ├── 06.cnf
│       ├── 07.cnf
│       ├── 08.cnf
│       ├── 09.cnf
│       ├── 10.cnf
│       ├── 11.cnf
│       ├── 12.cnf
│       ├── 13.cnf
│       ├── 14.cnf
│       ├── 15.cnf
│       ├── 16.cnf
│       ├── 17.cnf
│       ├── 18.cnf
│       ├── 19.cnf
│       ├── 20.cnf
│       ├── 21.cnf
│       ├── 22.cnf
│       ├── 23.cnf
│       ├── 24.cnf
│       ├── 25.cnf
│       ├── 26.cnf
│       ├── 27.cnf
│       ├── 28.cnf
│       ├── 29.cnf
│       ├── 30.cnf
│       ├── 31.cnf
│       ├── 32.cnf
│       ├── 33.cnf
│       └── 34.cnf
├── sat_oracle.py
├── sat_out
│   ├── bonus
│   ├── task1
│   │   ├── 00.sol
│   │   ├── 01.sol
│   │   ├── 02.sol
│   │   ├── 03.sol
│   │   ├── 04.sol
│   │   ├── 05.sol
│   │   ├── 06.sol
│   │   ├── 07.sol
│   │   ├── 08.sol
│   │   ├── 09.sol
│   │   ├── 10.sol
│   │   ├── 11.sol
│   │   ├── 12.sol
│   │   ├── 13.sol
│   │   ├── 14.sol
│   │   ├── 15.sol
│   │   ├── 16.sol
│   │   ├── 17.sol
│   │   ├── 18.sol
│   │   ├── 19.sol
│   │   ├── 20.sol
│   │   ├── 21.sol
│   │   ├── 22.sol
│   │   ├── 23.sol
│   │   ├── 24.sol
│   │   ├── 25.sol
│   │   ├── 26.sol
│   │   ├── 27.sol
│   │   ├── 28.sol
│   │   ├── 29.sol
│   │   ├── 30.sol
│   │   ├── 31.sol
│   │   ├── 32.sol
│   │   └── 33.sol
│   ├── task2
│   │   ├── 00.sol
│   │   ├── 01.sol
│   │   ├── 02.sol
│   │   ├── 03.sol
│   │   ├── 04.sol
│   │   ├── 05.sol
│   │   ├── 06.sol
│   │   ├── 07.sol
│   │   ├── 08.sol
│   │   ├── 09.sol
│   │   ├── 10.sol
│   │   ├── 11.sol
│   │   ├── 12.sol
│   │   ├── 13.sol
│   │   ├── 14.sol
│   │   ├── 15.sol
│   │   ├── 16.sol
│   │   ├── 17.sol
│   │   ├── 18.sol
│   │   ├── 19.sol
│   │   ├── 20.sol
│   │   ├── 21.sol
│   │   ├── 22.sol
│   │   ├── 23.sol
│   │   ├── 24.sol
│   │   ├── 25.sol
│   │   ├── 26.sol
│   │   ├── 27.sol
│   │   ├── 28.sol
│   │   ├── 29.sol
│   │   ├── 30.sol
│   │   ├── 31.sol
│   │   ├── 32.sol
│   │   └── 33.sol
│   └── task3
│       ├── 00.sol
│       ├── 01.sol
│       ├── 02.sol
│       ├── 03.sol
│       ├── 04.sol
│       ├── 05.sol
│       ├── 06.sol
│       ├── 07.sol
│       ├── 08.sol
│       ├── 09.sol
│       ├── 10.sol
│       ├── 11.sol
│       ├── 12.sol
│       ├── 13.sol
│       ├── 14.sol
│       ├── 15.sol
│       ├── 16.sol
│       ├── 17.sol
│       ├── 18.sol
│       ├── 19.sol
│       ├── 20.sol
│       ├── 21.sol
│       ├── 22.sol
│       ├── 23.sol
│       ├── 24.sol
│       ├── 25.sol
│       ├── 26.sol
│       ├── 27.sol
│       ├── 28.sol
│       ├── 29.sol
│       ├── 30.sol
│       ├── 31.sol
│       ├── 32.sol
│       ├── 33.sol
│       └── 34.sol
├── setup.sh
├── src
│   └── java_implementation
│       ├── BonusTask.class
│       ├── BonusTask.java
│       ├── Main.class
│       ├── Main.java
│       ├── Makefile
│       ├── Task1.class
│       ├── Task1.java
│       ├── Task2.class
│       ├── Task2.java
│       ├── Task3.class
│       ├── Task3.java
│       ├── Task.class
│       └── Task.java
└── Tema-2-AA-skel.iml
```