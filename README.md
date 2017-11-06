# Adfgvx-Cipher

# Adfgvx-Cipher


The ADFGVX code was used by the German durin World War I
  It is more difficult to deciher than the Playfair code because it uses a double
  encryption but was deciphered by the French Army Lieutenant Georges Painvin before the end of the war.
 
  All the encoded messages will only contain the letters ADFGVX. Actually any 6 letters would do
  but ADFGVX were choosen because these letters are very different in Morse code.
 
  When a telegraph operator has to transmit a message like "Hello my dear" he just read the
  sentence, can even remember it, and transmit the text "Hello my dear". However when he has
  to transmit a coded string like "ZWHYW WQ JFGT" it has to read and transmit letter by letter so
  the translation from letter to Morse is more error prone. Using letters that are quite different
  in Morse reduced to possibility of errors.
 
  The Ceasar code uses a number of shifts.
  The Vigenere and Playfair codes use a Key.
  This code use a 6 X 6 matrix where are randomly stored the 26 letters of the alphabet and the 10
  numeric digits. This matrix should be known by both the sender and the receiver.
  Example:
 
       A  D  F  G  V  X
     +------------------
   A | D  6  E  A  M  1
   D | 0  I  N  3  C  B
   F | T  Y  S  W  Z  9
   G | 2  L  Q  O  K  V
   V | F  G  8  H  J  P
   X | V  X  4  5  R  7
  
   The first step of the encoding process is to represent every letter/number of the message
   by the ADFGVX letter of the row and the column where the letter is located in the matrix so
  
   H  e  l  l  o    m  y   d  e  a  r
   VG AF GD GD GG   AV FD  AA AG AG XV
  
   OK here nothing new. It is a simple substitution and a frequency analysis would permit to
   decipher that code quite fast.
   But now, as a second step, the code use a transposition that will make the
   decipher process a lot more complicated.
  
   A key is used that must also be known by both the sender and the receiver
   Let's use JOHN as a key. We first write the key on a line and write the message as coded under it
  
    J O H N
    -------
    V G A F    H e
    G D G D    l l
    G G A V    o m
    F D A A    y d
    A G A G    e a
    X V        r
   
    then we put the Key in alphabetical order and re-arrange the columns accordingly
   
    J O H N  ->   H J N O
    -------       -------
    V G A F  ->   A V F G
    G D G D  ->   G G D D
    G G A V  ->   A G V G
    F D A A  ->   A F A D
    A G A G  ->   A A G G
    X V D A  ->     X   V
   
    The final coded message is produced by reading letter each column in the right table from top to bottom
    So the final message here is:
    A G A A A V G G F A X F D V A G G D G D G V
    --------- ----------- --------- -----------
      Col H     Col J       Col N     Col O
     
    Contrary to the other codes previously seen, this one changes the total coded line
    when a letter is appended to the original message
  
 /
