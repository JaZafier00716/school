# rpc - remote procedure call

from xmlrpc.server import SimpleXMLRPCServer

def add(x, y):
    return x + y

def main():
    server = SimpleXMLRPCServer(("localhost", 10000))
    print("Listening on port 10000")
    server.register_function(add)
    server.serve_forever()

if __name__ == "__main__":
    main()