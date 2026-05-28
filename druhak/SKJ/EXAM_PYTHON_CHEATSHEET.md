# Python Exam Cheat Sheet

This file is tailored to the tasks in this project: parsing files, counting values, sorting with tie-breakers, writing stateful classes, simulations, transactions, grids, and pytest-style validation.

## Terminal Commands

Run all tests in a task folder:

```bash
cd /home/jan/Downloads/skj/Testy/zadani6
/home/jan/Downloads/skj/venv/bin/python -m pytest -q tests.py
```

Run one test:

```bash
/home/jan/Downloads/skj/venv/bin/python -m pytest -q tests.py::test_name
```

Verbose test output:

```bash
/home/jan/Downloads/skj/venv/bin/python -m pytest -vv tests.py
```

Run a Python file:

```bash
python3 tasks.py
/home/jan/Downloads/skj/venv/bin/python tasks.py
```

Syntax check:

```bash
python3 -m py_compile tasks.py
```

Find task/test files:

```bash
find . -name 'tasks.py' -o -name 'tests.py'
```

Search code:

```bash
grep -R "function_name" -n . --include='*.py'
```

## Imports

```python
import math
import re
import csv
from pathlib import Path
from collections import defaultdict, Counter, deque
from typing import List, Dict, Tuple, Optional
```

Useful constants/functions:

```python
math.inf
-math.inf
math.sqrt(x)
math.ceil(x)
math.floor(x)
abs(x)
round(x)
min(values)
max(values)
sum(values)
len(values)
```

## File Reading

Basic text file:

```python
with open(path, "r", encoding="utf-8") as f:
    for line in f:
        line = line.strip()
```

For grid/map files, preserve spaces:

```python
line = line.rstrip("\n")
```

Open relative to the script:

```python
from pathlib import Path

def resolve_path(file_path):
    path = Path(file_path)
    if not path.is_absolute() and not path.exists():
        path = Path(__file__).resolve().parent / path
    return path
```

Parse comma-separated integers:

```python
x, y, z = map(int, line.strip().split(","))
```

Parse whitespace fields:

```python
parts = line.split()
```

Parse `time: name x y z`:

```python
left, right = line.strip().split(":")
time = int(left)
name, x, y, z = right.split()
pos = (int(x), int(y), int(z))
```

Parse transaction lines:

```python
src, rest = line.strip().split("->")
src = src.strip()
dst, amount = rest.strip().split()
amount = int(amount)
```

CSV:

```python
import csv

with open(path, newline="", encoding="utf-8") as f:
    reader = csv.reader(f)
    for name, subject, points in reader:
        points = int(points)
```

## Counting And Dictionaries

Manual counter:

```python
counts = {}
for item in items:
    counts[item] = counts.get(item, 0) + 1
```

`defaultdict` counter:

```python
from collections import defaultdict

counts = defaultdict(int)
for item in items:
    counts[item] += 1
```

Totals and counts for averages:

```python
stats = {}
for subject, points in rows:
    total, count = stats.get(subject, (0, 0))
    stats[subject] = (total + points, count + 1)

averages = {
    subject: total // count
    for subject, (total, count) in stats.items()
}
```

Nested dict:

```python
data = {}
if subject not in data:
    data[subject] = {"lesson": [], "project": [], "test": []}
data[subject]["lesson"].append(points)
```

Most common manually:

```python
best_key = None
best_count = -1
for key, count in counts.items():
    if count > best_count:
        best_key = key
        best_count = count
```

## Lists, Sets, Tuples

Useful methods:

```python
lst.append(x)
lst.pop()
lst.pop(0)
lst.remove(x)
lst.sort()
sorted(lst)

s.add(x)
s.remove(x)
x in s
a.issubset(b)

d.keys()
d.values()
d.items()
```

Unique visitors in one day:

```python
visitors = {int(x) for x in line.strip().split(",") if x != ""}
```

Unique unordered pair:

```python
pair = tuple(sorted((name1, name2)))
pairs.add(pair)
```

Palindrome:

```python
def je_palindrom(s):
    return s == s[::-1]
```

## Sorting

Ascending:

```python
items.sort()
```

Count descending, name ascending:

```python
sorted_items = sorted(counts.items(), key=lambda x: (-x[1], x[0]))
```

Count descending, ID descending:

```python
sorted_items = sorted(counts.items(), key=lambda x: (x[1], x[0]), reverse=True)
```

Smallest queue, then lowest ID:

```python
chosen = min(counters, key=lambda c: (len(c["queue"]), c["id"]))
```

Sort dict records:

```python
records.sort(key=lambda r: r["id"])
```

Most frequent target with lexicographic tie:

```python
target = sorted(target_counts.items(), key=lambda x: (-x[1], x[0]))[0][0]
```

## Exceptions And Classes

```python
class MyException(Exception):
    pass

raise MyException()
raise Exception("message")
```

Basic class:

```python
class Thing:
    def __init__(self, capacity):
        self.capacity = capacity
        self.items = []

    def count(self):
        return len(self.items)
```

pytest expects exceptions like:

```python
with pytest.raises(Exception):
    obj.method()
```

## Circular Index

Used in control panel / conveyor belt tasks.

```python
class Panel:
    def __init__(self, symbols):
        self.symbols = symbols
        self.index = 0
        self.output = []

    def move_right(self):
        self.index = (self.index + 1) % len(self.symbols)

    def move_left(self):
        self.index = (self.index - 1) % len(self.symbols)

    def select(self):
        self.output.append(self.symbols[self.index])

    def result(self):
        return self.output
```

## Bounding Box / Coordinates

```python
def spocti_krychli(path):
    xs, ys, zs = [], [], []
    with open(path) as f:
        for line in f:
            x, y, z = map(int, line.strip().split(","))
            xs.append(x)
            ys.append(y)
            zs.append(z)

    return (max(xs) - min(xs)) * (max(ys) - min(ys)) * (max(zs) - min(zs))
```

Streaming min/max:

```python
min_x = min_y = min_z = math.inf
max_x = max_y = max_z = -math.inf
```

## Distance / Dangerous Contacts

```python
dist = math.sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2 + (z1 - z2) ** 2)
```

Compare pairs once:

```python
from collections import defaultdict
import math

def find_dangerous_contacts(path, max_distance):
    by_time = defaultdict(list)

    with open(path) as f:
        for line in f:
            time_text, rest = line.strip().split(":")
            name, x, y, z = rest.split()
            by_time[int(time_text)].append((name, (int(x), int(y), int(z))))

    contacts = set()
    for users in by_time.values():
        for i in range(len(users)):
            for j in range(i + 1, len(users)):
                name1, p1 = users[i]
                name2, p2 = users[j]
                distance = math.sqrt(sum((p1[k] - p2[k]) ** 2 for k in range(3)))
                if distance <= max_distance:
                    contacts.add(tuple(sorted((name1, name2))))

    return sorted(contacts)
```

## Grid BFS / Reachability

```python
from collections import deque

def is_reachable(path, target):
    with open(path) as f:
        height, width = map(int, f.readline().split())
        grid = [f.readline().rstrip("\n") for _ in range(height)]

    start = None
    for r in range(height):
        for c in range(width):
            if grid[r][c] == "S":
                start = (r, c)

    q = deque([start])
    seen = {start}
    directions = [(1, 0), (-1, 0), (0, 1), (0, -1)]

    while q:
        r, c = q.popleft()
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if not (0 <= nr < height and 0 <= nc < width):
                continue
            if (nr, nc) in seen:
                continue
            ch = grid[nr][nc]
            if ch == target:
                return True
            if ch == " ":
                seen.add((nr, nc))
                q.append((nr, nc))

    return False
```

## Time Simulations

Advance exactly `n` ticks:

```python
def advance_time(self, n):
    for _ in range(n):
        self.time += 1
        self.remove_finished()
        self.move_ready()
```

Do not remove from a list while iterating it:

```python
remaining = []
for item in self.items:
    if done(item):
        self.finished += 1
    else:
        remaining.append(item)
self.items = remaining
```

Move in accepted order while capacity allows:

```python
self.vaccination.sort(key=lambda p: p["accepted_at"])
still_vaccinating = []

for patient in self.vaccination:
    if ready(patient) and len(self.waiting) < self.waiting_capacity:
        patient["waiting_since"] = self.time
        self.waiting.append(patient)
    else:
        still_vaccinating.append(patient)

self.vaccination = still_vaccinating
```

Queue:

```python
queue.append(x)
x = queue.pop(0)
```

Efficient queue:

```python
from collections import deque

q = deque()
q.append(x)
x = q.popleft()
```

## Wallet / Transactions

```python
class TransactionException(Exception):
    pass

def get_account(accounts, account_id):
    if account_id not in accounts:
        accounts[account_id] = {
            "id": account_id,
            "balance": 0,
            "incoming": 0,
            "outgoing": 0,
            "targets": {},
        }
    return accounts[account_id]

def generate_wallet_info(path):
    accounts = {}

    with open(path) as f:
        for line in f:
            src, rest = line.strip().split("->")
            src = src.strip()
            dst, amount = rest.strip().split()
            amount = int(amount)

            dst_acc = get_account(accounts, dst)

            if src:
                if src not in accounts:
                    raise TransactionException()
                src_acc = accounts[src]
                if src_acc["balance"] < amount:
                    raise TransactionException()

                src_acc["balance"] -= amount
                src_acc["outgoing"] += 1
                src_acc["targets"][dst] = src_acc["targets"].get(dst, 0) + 1

            dst_acc["balance"] += amount
            dst_acc["incoming"] += 1

    result = []
    for acc in accounts.values():
        most = None
        if acc["targets"]:
            most = sorted(acc["targets"].items(), key=lambda x: (-x[1], x[0]))[0][0]

        result.append({
            "id": acc["id"],
            "balance": acc["balance"],
            "incoming-count": acc["incoming"],
            "outgoing-count": acc["outgoing"],
            "most-frequent-target": most,
        })

    return sorted(result, key=lambda x: x["id"])
```

## Library / Inventory

```python
class Knihovna:
    def __init__(self):
        self.knihy = {}

    def pridej_knihu(self, nazev):
        self.knihy[nazev] = self.knihy.get(nazev, 0) + 1

    def vypujc_knihu(self, nazev):
        if self.knihy.get(nazev, 0) <= 0:
            return False
        self.knihy[nazev] -= 1
        return True

    def vrat_pocet_kopii(self):
        return sum(self.knihy.values())

    def vrat_nejcetnejsi_knihu(self):
        if not self.knihy:
            return None
        return max(self.knihy.items(), key=lambda x: x[1])[0]
```

## Tax / Cheapest-First Selling

```python
class TradeException(Exception):
    pass

class TaxEvaluator:
    def __init__(self, people):
        self.people = {
            person: {"coins": {}, "gain": 0}
            for person in people
        }

    def _person(self, name):
        if name not in self.people:
            raise TradeException()
        return self.people[name]

    def buy_crypto(self, name, coin, price, amount):
        person = self._person(name)
        person["coins"].setdefault(coin, []).append([price, amount])
        person["coins"][coin].sort(key=lambda lot: lot[0])

    def sell_crypto(self, name, coin, price, amount):
        person = self._person(name)
        lots = person["coins"].get(coin, [])
        if sum(lot_amount for _, lot_amount in lots) < amount:
            raise TradeException()

        remaining = amount
        cost = 0
        new_lots = []

        for buy_price, lot_amount in lots:
            sold = min(remaining, lot_amount)
            cost += sold * buy_price
            remaining -= sold

            left = lot_amount - sold
            if left > 0:
                new_lots.append([buy_price, left])

            if remaining == 0:
                index = lots.index([buy_price, lot_amount])
                new_lots.extend(lots[index + 1:])
                break

        person["coins"][coin] = new_lots
        gain = price * amount - cost
        person["gain"] += gain
        return gain

    def total_coin_value(self, name, coin, current_price):
        person = self._person(name)
        return sum(amount for _, amount in person["coins"].get(coin, [])) * current_price

    def get_tax(self, name):
        gain = self._person(name)["gain"]
        return 0 if gain <= 0 else math.ceil(gain * 0.15)
```

## Study Database

```python
class StudyDatabase:
    TYPES = ("lesson", "project", "test")

    def __init__(self):
        self.subjects = {}

    def _ensure_subject(self, subject):
        if subject not in self.subjects:
            self.subjects[subject] = {t: [] for t in self.TYPES}

    def add_points(self, subject, point_type, points):
        self._ensure_subject(subject)
        self.subjects[subject][point_type].append(points)
        return sum(self.subjects[subject][point_type])

    def total_points_per_subject(self, point_type=None):
        result = {}
        for subject, data in self.subjects.items():
            if point_type is None:
                result[subject] = sum(sum(values) for values in data.values())
            else:
                result[subject] = sum(data[point_type])
        return result

    def average_points_per_type(self, point_type):
        result = {}
        for subject, data in self.subjects.items():
            values = data[point_type]
            result[subject] = 0 if not values else sum(values) // len(values)
        return result

    def passed_subjects(self):
        totals = self.total_points_per_subject()
        return sorted(subject for subject, total in totals.items() if total >= 51)
```

## Counter Manager

```python
class CounterManager:
    MAX_QUEUE = 5

    def __init__(self, counters):
        self.counters = []
        for i, activities in enumerate(counters, start=1):
            self.counters.append({
                "id": i,
                "activities": set(activities),
                "queue": [],
                "finished": 0,
            })

    def queue_visitor(self, requirements):
        requirements = set(requirements)
        candidates = [
            c for c in self.counters
            if requirements.issubset(c["activities"])
            and len(c["queue"]) < self.MAX_QUEUE
        ]
        if not candidates:
            raise Exception()

        chosen = min(candidates, key=lambda c: (len(c["queue"]), c["id"]))
        chosen["queue"].append(requirements)
        return chosen["id"]

    def counter_advance(self, counter_id):
        if counter_id < 1 or counter_id > len(self.counters):
            raise Exception()
        counter = self.counters[counter_id - 1]
        if not counter["queue"]:
            raise Exception()
        counter["queue"].pop(0)
        counter["finished"] += 1

    def counter_queue_sizes(self):
        return [len(c["queue"]) for c in self.counters]

    def counter_finished_visitors(self):
        return [c["finished"] for c in self.counters]
```

## Regex / Parser

Use `fullmatch`, not `match`, when validating the whole string.

```python
REGISTER = r"R([0-9]|1[0-5])"
NUMBER = r"[0-9]+"
MEM_NUMBER = r"\[[0-9]+\]"
MEM_REGISTER = r"\[R([0-9]|1[0-5])\]"

if re.fullmatch(REGISTER, text):
    ...
```

Fix opcode with one typo:

```python
def distance(a, b):
    if len(a) != len(b):
        return 999
    return sum(1 for x, y in zip(a, b) if x != y)

def fix_opcode(opcode):
    for valid in ("MOV", "ADD"):
        if opcode == valid or distance(opcode, valid) == 1:
            return valid
    raise ProgramException()
```

Normalize assembly lines:

```python
lines = []
previous = None
for raw in program.splitlines():
    parts = raw.split()
    if not parts:
        continue
    if len(parts) != 3:
        raise ProgramException()

    opcode = fix_opcode(parts[0])
    line = f"{opcode} {parts[1]} {parts[2]}"

    if line != previous:
        lines.append(line)
    previous = line

formatted = "\n".join(lines) + ("\n" if lines else "")
```

## Assembly Interpreter Helpers

```python
def parse_arg(arg):
    if re.fullmatch(r"R([0-9]|1[0-5])", arg):
        return ("reg", int(arg[1:]))
    if re.fullmatch(r"[0-9]+", arg):
        return ("const", int(arg))
    if re.fullmatch(r"\[[0-9]+\]", arg):
        return ("mem_const", int(arg[1:-1]))
    if re.fullmatch(r"\[R([0-9]|1[0-5])\]", arg):
        return ("mem_reg", int(arg[2:-1]))
    raise ProgramException()

def read_value(arg, registers, memory):
    kind, value = parse_arg(arg)
    if kind == "const":
        return value
    if kind == "reg":
        return registers[value]
    if kind == "mem_const":
        return memory[value]
    if kind == "mem_reg":
        return memory[registers[value]]

def write_value(arg, value, registers, memory):
    kind, target = parse_arg(arg)
    if kind == "const":
        raise ProgramException()
    if kind == "reg":
        registers[target] = value
    elif kind == "mem_const":
        memory[target] = value
    elif kind == "mem_reg":
        memory[registers[target]] = value
```

Execute:

```python
registers = [0] * 16
memory = [0] * 1000

for line in formatted.splitlines():
    opcode, a, b = line.split()
    if opcode == "MOV":
        write_value(a, read_value(b, registers, memory), registers, memory)
    elif opcode == "ADD":
        value = read_value(a, registers, memory) + read_value(b, registers, memory)
        write_value(a, value, registers, memory)
```

## Flower Pot Belt

```python
class FlowerPotBelt:
    def __init__(self, positions, max_weight):
        self.positions = [None] * positions
        self.index = 0
        self.max_weight = max_weight

    def move_right(self):
        self.index = (self.index + 1) % len(self.positions)

    def move_left(self):
        self.index = (self.index - 1) % len(self.positions)

    def total_weight(self):
        total = 0
        for pot in self.positions:
            if pot is not None:
                total += sum(pot["soil"].values())
        return total

    def place_flowerpot(self, capacity):
        if self.positions[self.index] is not None:
            raise Exception()
        self.positions[self.index] = {"capacity": capacity, "soil": {}}

    def add_soil(self, soil_type, weight):
        pot = self.positions[self.index]
        if pot is None:
            raise Exception()
        if sum(pot["soil"].values()) + weight > pot["capacity"]:
            raise Exception()
        if self.total_weight() + weight > self.max_weight:
            raise Exception()
        pot["soil"][soil_type] = pot["soil"].get(soil_type, 0) + weight

    def get_pot(self):
        pot = self.positions[self.index]
        if pot is None:
            return None
        return tuple(sorted(pot["soil"].items()))

    def take_pot(self):
        pot = self.positions[self.index]
        if pot is None:
            raise Exception()
        if sum(pot["soil"].values()) * 2 < pot["capacity"]:
            raise Exception()
        self.positions[self.index] = None
```

## Common Exam Edge Cases

- Empty files and empty structures.
- Unknown names/accounts/subjects.
- Duplicate values in one day should often be ignored with `set`.
- Tie-breakers in sorting matter.
- Check whether the rule says `<` or `<=`.
- Never mutate a list while iterating over it.
- `advance_time(n)` usually means exactly `n` ticks, not `n + 1`.
- For maps, use `rstrip("\n")`, not `strip()`, because spaces can be valid cells.
- For regex validation, prefer `re.fullmatch`.
- Return exactly the requested type: list vs tuple vs dict vs `None`.
- If tests use `pytest.raises(Exception)`, any normal exception is enough.
