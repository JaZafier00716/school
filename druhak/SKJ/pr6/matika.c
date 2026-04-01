unsigned long long factorial(unsigned int n) {
    if (n <= 1) {
        return 1;
    }
    unsigned long long result = 1;
    for (unsigned int i = 2; i <= n; ++i) {
        result *= i;
    }
    return result;
}