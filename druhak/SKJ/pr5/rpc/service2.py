# rpc - remote procedure call

from xmlrpc.server import SimpleXMLRPCServer

class Service:
    def add(self, x, y):
        return x + y

def main():
    server = SimpleXMLRPCServer(("localhost", 10000))
    print("Listening on port 10000")
    server.register_instance(Service())
    server.serve_forever()

if __name__ == "__main__":
    main()