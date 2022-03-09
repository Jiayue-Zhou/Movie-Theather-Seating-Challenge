# Movie-Theather-Seating-Challenge

Overview:

This program designed for Movie Theater Seating Challenge. It takes a file as input, 
which contains one line of input for each reservation request. The order of the lines in
the file reflects the order in which the reservation requests were received. The result of 
seating assignment will be wrote into the output file.


Assumptions: 

1. We assume customers prefer the seats in the center of the theater. So we take
the center row as the first reservation start row. If the center row is complete, we take
the exact row behind the center row, and if this row is also complete, we take the
row in front of the center row. Then we take the other exact row behind the center
row. So on and so forth.

2. We also take customer safety as important as possible. For each group (people
ordering together with an identifier), we allow them to sit together if possible. 
For different groups, we set a buffer of three seats and/or one row.

3. If people in a group cannot sit together(we do not have enough seats to satisfy
them to sit together, but we still have other available seats in the theater), we still
process this requirement but split the people. (The principle is to group people as
much as possible)

4. If we do not have enough seats for a certain identifier in the theater, 
we do not process this identifier. We just skip it and process other requirements, and
return a warning for this identifier.



How to Run the Program:

1. Open the termian and get into the src folder. 

2. Run

javac processor.java

to compile the program.

3. Use

java processor input.txt

to run the program. Or you can revise the command, if you have you own input file.

