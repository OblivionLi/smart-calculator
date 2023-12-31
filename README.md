# smart-calculator

#### A smart calculator that calculates the results of basic arithmetic expressions, taking into account the order of operations, where parentheses have the highest priority, followed by exponentiation, multiplication and division, and finally addition and subtraction.
#### It is also capable of processing arithmetic expressions, including those involving large integers, such as `112234567890 + 112234567890 * (10000000999 - 999)`. Additionally, it offers a convenient feature allowing users to assign values to variables and utilize them within expressions.
---
##### Example (`>` is user input):
---
```
> 2^2
4
> 2*2^3
16
> 8 * 3 + 12 * (4 - 2)
48
> 2 - 2 + 3
3
> 4 * (2 + 3
Invalid expression
> -10
-10
> a=4
> b=5
> c=6
> a*2+b*3+c*(2+3)
53
> 1 +++ 2 * 3 -- 4
11
> 3 *** 5
Invalid expression
> 4+3)
Invalid expression
> 112234567890 + 112234567890 * (10000000999 - 999)
1122345679012234567890
> a = 800000000000000000000000
> b = 100000000000000000000000
> a + b
900000000000000000000000
> /command
Unknown command
> /exit
Bye!
```
