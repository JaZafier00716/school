def fizzbuzz(num):
    """
    Return 'Fizz' if `num` is divisible by 3, 'Buzz' if `num` is divisible by 5, 'FizzBuzz' if `num` is divisible both by 3 and 5.
    If `num` isn't divisible neither by 3 nor by 5, return `num`.
    Example:
        fizzbuzz(3) # Fizz
        fizzbuzz(5) # Buzz
        fizzbuzz(15) # FizzBuzz
        fizzbuzz(8) # 8
    """
    if(num % 3 == 0 and num % 5 == 0):
        return "FizzBuzz"
    elif(num % 3 == 0):
        return "Fizz"
    elif(num % 5 == 0):
        return "Buzz"
    else:
        return num


def fibonacci(n):
    """
    Return the `n`-th Fibonacci number (counting from 0).
    Example:
        fibonacci(0) == 0
        fibonacci(1) == 1
        fibonacci(2) == 1
        fibonacci(3) == 2
        fibonacci(4) == 3
    """
    if(n == 0):
        return 0
    elif(n == 1):
        return 1
    else:
        return fibonacci(n-1) + fibonacci(n-2)


def dot_product(a, b):
    """
    Calculate the dot product of `a` and `b`.
    Assume that `a` and `b` have same length.
    Hint:
        lookup `zip` function
    Example:
        dot_product([1, 2, 3], [0, 3, 4]) == 1*0 + 2*3 + 3*4 == 18
    """

    tuples = zip(a, b)

    sum = 0
    for tuple in tuples:
        sum += tuple[0] * tuple[1]
    return sum

def redact(data, chars):
    """
    Return `data` with all characters from `chars` replaced by the character 'x'.
    Characters are case sensitive.
    Example:
        redact("Hello world!", "lo")        # Hexxx wxrxd!
        redact("Secret message", "mse")     # Sxcrxt xxxxagx
    """

    for char in chars:
        data = data.replace(char, "x")

    print(data)
    return data


def count_words(data):
    """
    Return a dictionary that maps word -> number of occurences in `data`.
    Words are separated by spaces (' ').
    Characters are case sensitive.

    Hint:
        "hi there".split(" ") -> ["hi", "there"]

    Example:
        count_words('this car is my favourite what car is this')
        {
            'this': 2,
            'car': 2,
            'is': 2,
            'my': 1,
            'favourite': 1,
            'what': 1
        }
    """
    if len(data) == 0:
        return {}

    data_arr = data.split(" ")
    dictionary = {}

    for word in data_arr:
        if word in dictionary:
            dictionary[word] += 1
        else:
            dictionary[word] = 1
    return dictionary

print(count_words("this car is my favourite what car is this"))
print(count_words(""))

def bonus_fizzbuzz(num):
    """
    Implement the `fizzbuzz` function.
    `if`, match-case and cycles are not allowed.
    """

    return (((num, "Buzz")[num % 5 == 0], "Fizz")[num % 3 == 0], "FizzBuzz")[num % 15 == 0]

def bonus_utf8(cp):
    """
    Encode `cp` (a Unicode code point) into 1-4 UTF-8 bytes - you should know this from `Základy číslicových systémů (ZDS)`.
    Example:
        bonus_utf8(0x01) == [0x01]
        bonus_utf8(0x1F601) == [0xF0, 0x9F, 0x98, 0x81]
    """
    if cp < 0x80:
        return [cp]
    elif cp < 0x800:
        return [0xC0 | (cp >> 6), 0x80 | (cp & 0x3F)]
    elif cp < 0x10000:
        return [0xE0 | (cp >> 12), 0x80 | ((cp >> 6) & 0x3F), 0x80 | (cp & 0x3F)]
    elif cp < 0x110000:
        return [0xF0 | (cp >> 18), 0x80 | ((cp >> 12) & 0x3F), 0x80 | ((cp >> 6) & 0x3F), 0x80 | (cp & 0x3F)]
    else:
        raise ValueError("Invalid Unicode code point")
