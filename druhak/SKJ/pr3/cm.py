# context manager
# with open('cities.txt', 'rt' ) as f:
#     for line in f:
#         print(line, end='')

class MCM:
  def __init(self, filename, mode):
    self.filename = filename
    self.mode = mode

  def __enter__(self):
    self.file = open(self.filename, self.mode)
    return self.file

  def __exit__(self, exc_type, exc_value, traceback):
    self.file.close()

with MCM('cities.txt', 'rt') as f:
    for line in f:
        print(line, end='')