001
import java.awt.Point;
002
import java.util.*;
003
/**
004
 * The ADFGVX code was used by the German durin World War I
005
 * It is more difficult to deciher than the Playfair code because it uses a double
006
 * encryption but was deciphered by the French Army Lieutenant Georges Painvin before the end of the war.
007
 *
008
 * All the encoded messages will only contain the letters ADFGVX. Actually any 6 letters would do
009
 * but ADFGVX were choosen because these letters are very different in Morse code.
010
 *
011
 * When a telegraph operator has to transmit a message like "Hello my dear" he just read the
012
 * sentence, can even remember it, and transmit the text "Hello my dear". However when he has
013
 * to transmit a coded string like "ZWHYW WQ JFGT" it has to read and transmit letter by letter so
014
 * the translation from letter to Morse is more error prone. Using letters that are quite different
015
 * in Morse reduced to possibility of errors.
016
 *
017
 * The Ceasar code uses a number of shifts.
018
 * The Vigenere and Playfair codes use a Key.
019
 * This code use a 6 X 6 matrix where are randomly stored the 26 letters of the alphabet and the 10
020
 * numeric digits. This matrix should be known by both the sender and the receiver.
021
 * Example:
022
 *
023
 *      A  D  F  G  V  X
024
 *    +------------------
025
 *  A | D  6  E  A  M  1
026
 *  D | 0  I  N  3  C  B
027
 *  F | T  Y  S  W  Z  9
028
 *  G | 2  L  Q  O  K  V
029
 *  V | F  G  8  H  J  P
030
 *  X | V  X  4  5  R  7
031
 * 
032
 *  The first step of the encoding process is to represent every letter/number of the message
033
 *  by the ADFGVX letter of the row and the column where the letter is located in the matrix so
034
 * 
035
 *  H  e  l  l  o    m  y   d  e  a  r
036
 *  VG AF GD GD GG   AV FD  AA AG AG XV
037
 * 
038
 *  OK here nothing new. It is a simple substitution and a frequency analysis would permit to
039
 *  decipher that code quite fast.
040
 *  But now, as a second step, the code use a transposition that will make the
041
 *  decipher process a lot more complicated.
042
 * 
043
 *  A key is used that must also be known by both the sender and the receiver
044
 *  Let's use JOHN as a key. We first write the key on a line and write the message as coded under it
045
 * 
046
 *   J O H N
047
 *   -------
048
 *   V G A F    H e
049
 *   G D G D    l l
050
 *   G G A V    o m
051
 *   F D A A    y d
052
 *   A G A G    e a
053
 *   X V        r
054
 *  
055
 *   then we put the Key in alphabetical order and re-arrange the columns accordingly
056
 *  
057
 *   J O H N  ->   H J N O
058
 *   -------       -------
059
 *   V G A F  ->   A V F G
060
 *   G D G D  ->   G G D D
061
 *   G G A V  ->   A G V G
062
 *   F D A A  ->   A F A D
063
 *   A G A G  ->   A A G G
064
 *   X V D A  ->     X   V
065
 *  
066
 *   The final coded message is produced by reading letter each column in the right table from top to bottom
067
 *   So the final message here is:
068
 *   A G A A A V G G F A X F D V A G G D G D G V
069
 *   --------- ----------- --------- -----------
070
 *     Col H     Col J       Col N     Col O
071
 *    
072
 *   Contrary to the other codes previously seen, this one changes the total coded line
073
 *   when a letter is appended to the original message
074
 * 
075
 */
076
public class Adfgvx {
077
 
078
    // the letters used that will be transmitted in Morse code
079
    private static final char[] morse = {'A', 'D', 'F', 'G', 'V', 'X'};
080
    // the key
081
    private String key;
082
    // the Grid used that will be randomly filled
083
    private char[][] grid;
084
    // the original columns (unsorted)
085
    private Column[] col;
086
    // the Colums sorted by their header
087
    private Column[] colAlpha;
088
     
089
    /**
090
     * Constructor that receives the Key as parameter
091
     */
092
    public Adfgvx(String key) {
093
        // Fill an arrayList with all the characters to put in the grid
094
        ArrayList<Character> al = new ArrayList<Character>();
095
        for(char c = 'A'; c <= 'Z'; c++)
096
            al.add(c);
097
        for(char c = '0'; c <= '9'; c++)
098
            al.add(c);
099
        // create a Random numbers generator to randomly extract the char from the arrayList
100
        Random ran = new Random();
101
        // create our Grid
102
        grid = new char[morse.length][morse.length];
103
        // fill it
104
        for(int i = 0; i < 6; i++) {
105
            for(int j = 0; j < 6; j++) {
106
                // generate a random number between 0 and the arrayList size
107
                int index = ran.nextInt(al.size());
108
                // remove the char at that index and store it into the grid
109
                grid[i][j] = al.remove(index);
110
            }
111
        }
112
 
113
        // call our method to register the key
114
        setKey(key);
115
    }
116
 
117
    /**
118
     * Use to set the initial key or to change it later on
119
     */
120
    public void setKey(String key) {
121
        // if the key is null ignore it
122
        if(key == null) {
123
            this.key = "";
124
            return;
125
        }
126
        // convert it into char[] to check if twice the same digit
127
        char[] digit = key.toCharArray();
128
        int len = digit.length;
129
        // the key cannot contain twice the same character
130
        for(int i = 0; i < len - 1; i++) {
131
            for(int j = i + 1; j < len; j++) {
132
                if(digit[i] == digit[j]) {
133
                    this.key = "";
134
                    return;
135
                }
136
            }
137
        }
138
        // ok key is valid
139
        this.key = key;
140
        // build our columns with each charac of the key as header
141
        col = new Column[len];
142
        colAlpha = new Column[len];
143
        for(int i = 0; i < len; i++) {
144
            // the original columns
145
            col[i] = new Column(digit[i]);
146
            // and the ones that will be sorted later (but same column)
147
            colAlpha[i] = col[i];
148
        }
149
        // sort our second serie of columns in alphabetical order
150
        Arrays.sort(colAlpha);
151
         
152
    }
153
 
154
    /**
155
     * To encode a message
156
     */
157
    public String encode(String clear) {
158
        // common method that will chek that the key is valid and that
159
        // the message is OK. It also suppress illegal characters in the message
160
        char[] digit = msgToProcess(clear, true);
161
        if(digit.length == 0)
162
            return "";
163
         
164
        // prepare the columns (we multiply by 2 because we will need two coded letter
165
        // for evry letter in the original message)
166
        prepareColumns(digit.length * 2);
167
 
168
        // find the coordinates of each character in the original message
169
        // and add it row by row in each column
170
        int k = 0;                          // index in the column array
171
        for(char c : digit) {
172
            Point p = findPos(c);
173
            // add the 2 letters
174
            col[k++].add(morse[p.x]);
175
            // wrap around at the end of the columns
176
            k %= col.length;
177
            col[k++].add(morse[p.y]);
178
            k %= col.length;
179
        }
180
         
181
        // use a StringBuilder to concatenate the char in each column
182
        StringBuilder sb = new StringBuilder(digit.length * 2);
183
        for(Column c : colAlpha) {
184
            sb.append(c.toString());
185
        }
186
        // return the full coded String but with ' ' between every pair
187
        digit = sb.toString().toCharArray();
188
        sb = new StringBuilder(digit.length + (digit.length / 2));
189
        // put the first 2 digits
190
        sb.append(digit[0]);
191
        sb.append(digit[1]);
192
        // then every other pair preceeded by ' '
193
        for(int i = 2; i < digit.length; i += 2) {
194
            sb.append(' ');
195
            sb.append(digit[i]);
196
            sb.append(digit[i+1]);
197
        }
198
         
199
        return sb.toString();
200
    }
201
 
202
    /**
203
     * Decode the coded String received as parameter
204
     */
205
    public String decode(String coded) {
206
        // common method that will chek that the key is valid and that
207
        // the message is OK. It also suppress illegal characters in the message
208
        char[] digit = msgToProcess(coded, false);
209
        if(digit.length == 0)
210
            return "";
211
         
212
        // prepare the columns
213
        prepareColumns(digit.length);
214
         
215
        // copy the coded message into each alpha sorted Columns
216
        int k = 0;          // index in the digit array
217
        for(Column c : colAlpha) {
218
            int size = c.getSize();         // number of charac in column
219
            for(int i = 0; i < size; i++)
220
                c.add(digit[k++]);          // append next digit
221
        }
222
 
223
        // put back as a long string the contain of each original colum
224
        // row by row
225
        StringBuilder sb = new StringBuilder(digit.length);
226
        int size = col[0].getSize();        // the longest one
227
        // scan all rows
228
        for(int row = 0; row < size; row++) {
229
            // scan for that row all column
230
            for(Column c : col) {
231
                // if this Column has less row noi need to continue the following
232
                // ones will be the same
233
                if(row >= c.getSize())
234
                    break;
235
                // so append the character at that row
236
                sb.append(c.getChar(row));
237
            }
238
        }
239
         
240
        // make a char array of the StringBuilder
241
        digit = sb.toString().toCharArray();
242
        // use a another char array for the decoded String
243
        char[] decoded = new char[digit.length / 2];
244
        // pass through our string 2 characters at the time to find the
245
        // equivalent on the grid
246
        for(int i = 0; i < digit.length; i += 2) {
247
            // found the X coordinate in the grid
248
            int x = 0;
249
            for(; x < morse.length; x++) {
250
                if(digit[i] == morse[x])
251
                    break;
252
            }
253
            // found the y coordinate in the grid
254
            int y = 0;
255
            for(; y < morse.length; y++) {
256
                if(digit[i+1] == morse[y])
257
                    break;
258
            }
259
            // assign the value from the grid
260
            decoded[i/2] = grid[x][y];
261
        }
262
        return new String(decoded).toLowerCase();
263
    }
264
     
265
    /**
266
     * For both coding and decoding returns an array of char of the message
267
     * to code/decode
268
     * The boolean coding is true for encoding and false for decoding
269
     * Returns an empty array if the key is not valid
270
     */
271
    private char[] msgToProcess(String str, boolean coding) {
272
        // if message is null return nothing
273
        if(str == null)
274
            return new char[0];
275
        // if I do not have a valid key return
276
        if(key.length() == 0)
277
            return new char[0];
278
        // keep only valid characters that we will stored in a StringBuilder
279
        StringBuilder sb = new StringBuilder(key.length());
280
        // convert to uppercase
281
        char[] digit = str.toUpperCase().toCharArray();
282
        // pass through each digit
283
        for(char c : digit) {
284
            if(coding) {
285
                // if encoding if a letter or a digit add it
286
                if((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
287
                    sb.append(c);
288
            }
289
            else {
290
                // when decoding only the morse letters are permitted
291
                for(char m : morse) {
292
                    // if letter contained in morse letters
293
                    if(m == c) {
294
                        sb.append(c);
295
                        break;          // no need to continue loop in the morse permitted letters
296
                    }
297
                }
298
            }
299
        }
300
        // have digit to now be an array of just the valid character
301
        digit = sb.toString().toCharArray();
302
        // if empty return it
303
        if(digit.length == 0)
304
            return digit;
305
        // when decoding the number of letter must be even
306
        if(!coding) {
307
            if(digit.length % 2 != 0)
308
                return new char[0];
309
        }
310
        // return the array of letters to process
311
        return digit;
312
    }
313
     
314
    /**
315
     * Prepare/initialize the col and colAlpha objects
316
     */
317
    private void prepareColumns(int len) {
318
        // calculate the number of letters that will be in each column
319
        int nbPerCol = len / col.length;
320
        // make an array of these length
321
        int[] nb = new int[col.length];
322
        for(int i = 0; i < col.length; i++)
323
            nb[i] = nbPerCol;
324
        // now if the message length is not an exact multiple of the message length
325
        // the first columns will have one more row
326
        int reminder = len - (col.length * nbPerCol);
327
        for(int i = 0; i < reminder; i++)
328
            nb[i]++;
329
         
330
        // we can now set the size of each of our column object
331
        for(int i = 0; i < col.length; i++) {
332
            col[i].setSize(nb[i]);
333
        }
334
    }
335
     
336
    /**
337
     * Find the position of the character in the grid
338
     * the X and Y position will be index in the Morse array to find the ADFGVX letters to
339
     * use to represent that digit
340
     */
341
    private Point findPos(char c) {
342
        // scan the Grid
343
        for(int x = 0; x < 6; x++) {
344
            for(int y = 0; y < 6; y++) {
345
                // if match return the coords
346
                if(c == grid[x][y])
347
                    return new Point(x, y);
348
            }
349
        }
350
        throw new IllegalStateException("Character " + c + " not found in Grid");
351
    }
352
    /**
353
     * For debug purpose a method to display the Grid
354
     */
355
    public void dumpGrid() {
356
        // header
357
        System.out.println("      GRID");
358
        System.out.println();
359
        // gap before printing A D F G V X
360
        System.out.print("    ");
361
        // the letters A D F G V X
362
        for(int i = 0; i < morse.length; i++)
363
            System.out.print(" " + morse[i]);
364
        System.out.println();
365
        // +---------- under the A D F G V X at the top
366
        System.out.print("  +--");
367
        for(int i = 0; i < morse.length; i++)
368
            System.out.print("--");
369
        System.out.println();
370
        // now the different row
371
        for(int i = 0; i < morse.length; i++) {
372
            // the letter at the beginning of the row
373
            System.out.print(morse[i] + " | ");
374
            // the Grid contents for that line
375
            for(int j = 0; j < morse.length; j++) {
376
                System.out.print(" " + grid[i][j]);
377
            }
378
            // ready for next line
379
            System.out.println();
380
        }
381
    }
382
     
383
    /**
384
     * For the GUI
385
     */
386
    public char[][] getGrid() {
387
        return grid;
388
    }
389
    public char[] getMorse() {
390
        return morse;
391
    }
392
 
393
    /**
394
     * To test the class
395
     */
396
    public static void main(String[] args) {
397
        // test the whole thing
398
         
399
        //--------------------------------------------
400
        // Automatic tests
401
        //--------------------------------------------
402
        // create an Adfgvx object with JOHN as Key
403
        Adfgvx adfgvx = new Adfgvx("JOHN");
404
        // dump the grid
405
        adfgvx.dumpGrid();
406
        // use a message
407
        String message = "This is the message to encode";
408
        // code it
409
        String coded = adfgvx.encode(message);
410
        System.out.println("Original: " + message);
411
        System.out.println("   Coded:   " + coded);
412
        // decode it back
413
        System.out.println(" Decoded: " + adfgvx.decode(coded));
414
        //---------------------------------------------
415
        // end of automatic tests
416
        //---------------------------------------------
417
         
418
        // now proceed with user input
419
        Scanner scan = new Scanner(System.in);
420
        System.out.print("\n    Enter a Key: ");
421
        String key = scan.nextLine();
422
        Adfgvx user = new Adfgvx(key);
423
        System.out.print("Enter a message: ");
424
        String msg = scan.nextLine();
425
        // dump the grid
426
        user.dumpGrid();
427
        System.out.println("  Key is: " + user.key);
428
        System.out.println("Original: " + msg);
429
        String cypher = user.encode(msg);
430
        System.out.println("   Coded: " + cypher);
431
        System.out.println(" Decoded: " + user.decode(cypher));
432
    }
433
 
434
    /**
435
     * An internal class to hold the data (the character of each column)
436
     * it implements comparable so the column could be sorted by alpahbetical order
437
     */
438
    private class Column implements Comparable<Column> {
439
 
440
        // the letter A D F G V X at the head of the column
441
        private char header;
442
        // all the letters in the column
443
        private char[] letters;
444
        // use when we cumulate the digits in the letters array
445
        private int index;
446
 
447
        /**
448
         * Constructor that receives the letter as header
449
         */
450
        Column(char header) {
451
            this.header = header;
452
        }
453
        /**
454
         * To set the number of elements in the column
455
         */
456
        void setSize(int size) {
457
            // build array to receive all elements
458
            letters = new char[size];
459
            // reset that we are at element 0
460
            index = 0;
461
        }
        /**
463
         * To return, while decoding, the number of characters to insert in the Column
464
         */
        int getSize() {
            return letters.length;
        }
        /**
469
         * To add a letter to the column
470
         */
        void add(char c) {
            letters[index++] = c;
        }
        /**
475
         * To get a single letter
476
         */
        char getChar(int n) {
            return letters[n];
        }
        /**
481
         * To return as a String the letters in the column
482
         */
        public String toString() {
            return new String(letters);
        }
        /**
488
         * To sort the columns by header
489
         */
        public int compareTo(Column other) {
            return header - other.header;
        }
    }
}
