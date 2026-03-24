from xmlrpc.client import ServerProxy

s = ServerProxy("http://127.0.0.1:10000")

a = 5
b = 10

print(s.add(a, b))

a = [5, 6, 7]
b = [10, 11, 12]
print(s.add(a, b))