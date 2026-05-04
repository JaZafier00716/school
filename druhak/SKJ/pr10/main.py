from typing import Optional, Callable, Any
import dataclasses


@dataclasses.dataclass
class City:
    name: str
    zip_code: int

@dataclasses.dataclass
class Person:
    name: str
    city: City
    age: int

def find_person() -> Person:

    return Person(
        name=  "John",
        city = City(name="New York", zip_code=10001),
        age = 30
    )


from typing import NewType

CarId = NewType("CarId", int)
DriverId = NewType("DriverId", int)

@dataclasses.dataclass
class RideInfo:
    car_id: CarId
    driver_id: DriverId
    start_time: str
    end_time: str

class Database:
    def get_car_id(self, brand: str) -> CarId:
        return CarId(1)
    def get_driver_id(self, name: str) -> DriverId:
        return DriverId(1)
    def get_ride_info(self, car_id: CarId, driver_id: DriverId) -> RideInfo:
        pass

db = Database()
car_id = db.get_car_id("Toyota")
driver_id = db.get_driver_id("Stig")
info = db.get_ride_info(car_id, driver_id)


class Rectangle:
    @staticmethod
    def from_x1x2y1y2(x1: float, x2: float, y1: float, y2: float) -> 'Rectangle':
        return Rectangle(x1, y1, x2 - x1, y2 - y1)

    @staticmethod
    def from_tl_and_size(tl_x: float, tl_y: float, width: float, height: float) -> 'Rectangle':
        return Rectangle(tl_x, tl_y, width, height)

class Client:
    """
    Rules:
    - Do not call `send_message` before calling `connect` and then `authenticate`
    - Do not call `connect` or `authenticate` more than once
    - Do not call `close` before calling `connect`
    - Do not call any method after calling `close`
    """

    def __init__(self, address: str):
        pass

    def connect(self):
        pass
    def authenticate(self, password: str):
        pass
    def send_message(self, message: str):
        pass
    def close(self):
        pass

class ConnectedClient:
    def authenticate(...) -> 'AuthenticatedClient' | None:
        pass
    def send_message(...):
        pass
    def close(...):
        pass

def connect(address: str) -> ConnectedClient|None:
    pass

class AuthenticatedClient:
    def send_message(...):
        pass
    def close(...):
        pass

with connect(...) as client:
    client.send_message("foo")


# Rict AI, at pise sound code, ktery bude obsahovat type
# at definuje typy, pouziva invarianty nebo state machines, aby se zabranilo ambiguous stavum


