
import playground
import random

from typing import List, Tuple, NewType

Pos = NewType('Pos', Tuple[int, int])


class Atom:

    def __init__(self, pos: Pos, vel: Pos, rad: int, col: str):
        """
        Initializer of Atom class

        :param x: x-coordinate
        :param y: y-coordinate
        :param rad: radius
        :param color: color of displayed circle
        """
        self.x = pos[0]
        self.y = pos[1]
        self.vel = vel
        self.rad = rad
        self.color = col


    def to_tuple(self) -> Tuple[int, int, int, str]:
        """
        Returns tuple representing an atom.

        Example: pos = (10, 12,), rad = 15, color = 'green' -> (10, 12, 15, 'green')
        """

        return (self.x, self.y, self.rad, self.color)

    def apply_speed(self, size_x: int, size_y: int):
        """
        Applies velocity `vel` to atom's position `pos`.

        :param size_x: width of the world space
        :param size_y: height of the world space
        """
        if(self.x-self.rad + self.vel[0] < 0 or self.x+self.rad + self.vel[0] > size_x):
            self.vel = (-self.vel[0], self.vel[1])
        if(self.y - self.rad + self.vel[1] < 0 or self.y + self.rad + self.vel[1] > size_y):
            self.vel = (self.vel[0], -self.vel[1])

        self.x += self.vel[0]
        self.y += self.vel[1]


class FallDownAtom(Atom):
    """
    Class to represent atoms that are pulled by gravity.
     
    Set gravity factor to ~3.

    Each time an atom hits the 'ground' damp the velocity's y-coordinate by ~0.7.
    """
    def __init__(self, pos: Pos, vel: Pos, rad: int, col: str):
        super().__init__(pos, vel, rad, col)
        self.g = 3
        self.damping = 0.7

    def apply_speed(self, size_x, size_y):
        print(self.vel[1])
        
        if(self.x-self.rad + self.vel[0] < 0 or self.x+self.rad + self.vel[0] > size_x):
            self.vel = (-self.vel[0], self.vel[1])
        if(self.y - self.rad + self.vel[1] < 0 or self.y + self.rad + self.vel[1] > size_y):
            self.vel = (self.vel[0]*self.damping, -self.vel[1]*self.damping)
        
        self.vel = (self.vel[0], (self.vel[1] + self.g))

        self.x += self.vel[0]
        self.y += self.vel[1]
        if(self.y > size_y - self.rad):
            self.y = size_y - self.rad

class ExampleWorld:

    def __init__(self, size_x: int, size_y: int, no_atoms: int, no_falldown_atoms: int):
        """
        ExampleWorld initializer.

        :param size_x: width of the world space
        :param size_y: height of the world space
        :param no_atoms: number of 'bouncing' atoms
        :param no_falldown_atoms: number of atoms that respect gravity
        """

        self.width = size_x
        self.height = size_y
        self.atoms = []
        self.atoms += self.generate_atoms(no_atoms, no_falldown_atoms)

    def generate_atoms(self, no_atoms: int, no_falldown_atoms) -> List[Atom|FallDownAtom]:
        """
        Generates `no_atoms` Atom instances using `random_atom` method.
        Returns list of such atom instances.

        :param no_atoms: number of Atom instances
        :param no_falldown_atoms: numbed of FallDownAtom instances
        """

        atoms = []
        for _ in range(no_atoms):
            atoms.append(self.random_atom())
        for _ in range(no_falldown_atoms):
            atoms.append(self.random_falldown_atom())
        return atoms

    def random_atom(self) -> Atom:
        """
        Generates one Atom instance at random position in world, with random velocity, random radius
        and 'green' color.
        """
        return Atom(
            pos=(random.randint(0, self.width), random.randint(0, self.height)),
            vel=(random.randint(-10, 10), random.randint(-10, 10)),
            rad=random.randint(5, 15),
            col='green',
        )

    def random_falldown_atom(self):
        """
        Generates one FalldownAtom instance at random position in world, with random velocity, random radius
        and 'yellow' color.
        """
        return FallDownAtom(
            pos=(random.randint(0, self.width), random.randint(0, self.height)),
            vel=(random.randint(-10, 10), random.randint(-10, 10)),
            rad=random.randint(5, 15),
            col='yellow',
        )

    def add_atom(self, pos_x, pos_y):
        """
        Adds a new Atom instance to the list of atoms. The atom is placed at the point of left mouse click.
        Velocity and radius is random.

        :param pos_x: x-coordinate of a new Atom
        :param pos_y: y-coordinate of a new Atom

        Method is called by playground on left mouse click.
        """

        self.atoms.append(Atom(
            pos=(pos_x, pos_y),
            vel=(random.randint(-10, 10), random.randint(-10, 10)),
            rad=random.randint(5, 15),
            col='green',
        ))
        


    def add_falldown_atom(self, pos_x, pos_y):
        """
        Adds a new FallDownAtom instance to the list of atoms. The atom is placed at the point of right mouse click.
        Velocity and radius is random.

        Method is called by playground on right mouse click.

        :param pos_x: x-coordinate of a new FallDownAtom
        :param pos_y: y-coordinate of a new FallDownAtom
        """

        self.atoms.append(FallDownAtom(
            pos=(pos_x, pos_y),
            vel=(random.randint(-10, 10), random.randint(-10, 10)),
            rad=random.randint(5, 15),
            col='yellow',
        ))

    def tick(self):
        """
        Method is called by playground. Sends a tuple of atoms to rendering engine.

        :return: tuple or generator of atom objects, each containing (x, y, radius, color) attributes of atom 
        """
        atom_tuples = []
        for atom in self.atoms:
            atom.apply_speed(self.width, self.height)
            atom_tuples.append(atom.to_tuple())

        return tuple(atom_tuples)


if __name__ == '__main__':
    size_x, size_y = 700, 400
    no_atoms = 2
    no_falldown_atoms = 3

    world = ExampleWorld(size_x, size_y, no_atoms, no_falldown_atoms)

    playground.run((size_x, size_y), world)
