class Dog:
  def __init__(self, name):
    self.name = name

  def make_sound(self):
    return f"{self.name} barks: Woof!"
  
class Cat:
  def __init__(self, name):
    self.name = name

  def make_sound(self):
    return f"{self.name} meows: Meow!"
  
animals: list[Dog|Cat] = [Dog("Lassie"), Cat("Tom"), Dog("Fik"), 1]


for animal in animals:
  print(animal.make_sound())


# init vola new, ktery vytvori instanci tridy, a ten vola init, ktery nastavi atributy. A pak vola make_sound, ktery vraci string s hlasem zvirat.