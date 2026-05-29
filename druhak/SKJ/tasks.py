from collections import defaultdict


class ReservationException(BaseException):
    pass


def archive_distance(file: str, target: str) -> int:
    """
    Úkol 1

    Univerzitní archiv postihl výpadek navigačního systému a studenti nemohou najít potřebné
    dokumenty ke zkoušce. Pomozte jim naimplementováním funkce `archive_distance`, která načte
    mapu archivu ze souboru a zjistí, na kolik kroků je nejbližší hledaný dokument.

    Parametr `file` obsahuje cestu k textovému souboru s mapou archivu.
    - První řádek souboru obsahuje rozměry mapy ve formátu `<height> <width>`.
    - Následující řádky obsahují mapu s daným počtem řádků a sloupců.
    - Každé políčko mapy je reprezentováno jedním znakem:
        - `S` je startovní pozice studenta. V mapě bude právě jedna.
        - `.` je volné políčko, po kterém lze chodit.
        - `#` je stěna, přes kterou nelze projít.
        - Jakýkoliv jiný znak reprezentuje dokument.

    Parametr `target` obsahuje znak hledaného dokumentu. Můžete předpokládat, že `target` bude
    vždy jeden znak a nebude to `S`, `.` ani `#`.

    Student se může v jednom kroku posunout pouze nahoru, dolů, doleva nebo doprava. Nesmí vyjít
    mimo mapu a nesmí procházet skrz stěny. Dokument je nalezen ve chvíli, kdy student vstoupí na
    políčko s daným dokumentem.

    Funkce vrátí nejmenší počet kroků ze startu k dokumentu `target`.
    Pokud se dokument v mapě nenachází nebo se k němu nelze dostat, vraťte `-1`.

    Příklad souboru:
    ```
    5 7
    #######
    #S..A.#
    ###.#.#
    #B....#
    #######
    ```

    archive_distance("archive-1.txt", "A")  # 3
    archive_distance("archive-1.txt", "B")  # 6
    archive_distance("archive-1.txt", "C")  # -1
    """
    # Read the grid dimensions and then load the map row by row.
    with open(file, "r", encoding="utf-8") as handle:
        first_line = handle.readline()
        if not first_line:
            return -1

        height, width = map(int, first_line.split())
        grid = [list(handle.readline().rstrip("\n")) for _ in range(height)]

    # Find the unique starting position marked with `S`.
    start = None
    for row in range(height):
        for col in range(width):
            if grid[row][col] == "S":
                start = (row, col)
                break
        if start is not None:
            break

    if start is None:
        return -1

    # Breadth-first search gives the shortest path in an unweighted grid.
    queue = [start]
    head = 0
    distances = {start: 0}

    while head < len(queue):
        row, col = queue[head]
        head += 1
        distance = distances[(row, col)]

        # Try all four orthogonal directions.
        for d_row, d_col in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            next_row = row + d_row
            next_col = col + d_col

            # Ignore moves that leave the map.
            if not (0 <= next_row < height and 0 <= next_col < width):
                continue

            cell = grid[next_row][next_col]
            # Walls cannot be crossed.
            if cell == "#":
                continue

            next_pos = (next_row, next_col)
            # Skip cells already visited by a shorter or equal path.
            if next_pos in distances:
                continue

            next_distance = distance + 1
            # The target is found as soon as we step on its cell.
            if cell == target:
                return next_distance

            # Mark the cell as visited and continue the search from there.
            distances[next_pos] = next_distance
            queue.append(next_pos)

    return -1


class StudyRoomManager:
    """
    Úkol 2

    Knihovna chce lépe plánovat obsazenost studoven. Naimplementujte třídu `StudyRoomManager`,
    která bude spravovat rezervace studijních místností.

    Každá místnost má textové ID a kapacitu. Rezervace se evidují pro konkrétní den a časový
    interval. Časové intervaly berte jako polouzavřené: rezervace `[start, end)` zabírá čas od
    `start` včetně do `end` bez koncového okamžiku. To znamená, že rezervace `9-11` a `11-12`
    se nepřekrývají.

    Třída poskytuje následující rozhraní:
    ```python
    # Vytvoří správce se třemi místnostmi.
    manager = StudyRoomManager([("A", 4), ("B", 4), ("C", 10)])

    # Rezervuje dostupnou místnost na pondělí od 9 do 11 pro 3 osoby.
    # Metoda vrátí ID vybrané místnosti.
    manager.reserve("po", 9, 11, 3)  # "A"

    # Při výběru místnosti se volí dostupná místnost s dostatečnou kapacitou.
    # Z nich se preferuje nejmenší dostačující kapacita.
    # Pokud má více místností stejnou kapacitu, vybere se lexikograficky nejmenší ID místnosti.
    manager.reserve("po", 10, 12, 4)  # "B", protože A je v tomto čase obsazená

    # Zruší přesně odpovídající rezervaci.
    manager.cancel("A", "po", 9, 11)

    # Vrátí rezervace dané místnosti a dne seřazené podle času začátku.
    manager.schedule("B", "po")  # ((10, 12, 4),)
    ```

    Pravidla:
    - Pokud `start >= end`, metoda `reserve` vyvolá výjimku `ReservationException`.
    - Pokud není dostupná žádná místnost s dostatečnou kapacitou, metoda `reserve` vyvolá
      výjimku `ReservationException`.
    - Pokud metoda `cancel` nenajde přesně odpovídající rezervaci, vyvolá `ReservationException`.
    - Pokud metoda `schedule` nebo `cancel` obdrží neznámé ID místnosti, vyvolá
      `ReservationException`.
    """

    def __init__(self, rooms: list[tuple[str, int]]):
        # Store room capacities for quick lookup by room ID.
        self._rooms = {room_id: capacity for room_id, capacity in rooms}
        # Reservations are grouped by room ID and then by day.
        self._reservations = defaultdict(lambda: defaultdict(list))

    def reserve(self, day: str, start: int, end: int, people: int) -> str:
        # Invalid time interval: the reservation must have positive length.
        if start >= end:
            raise ReservationException()

        # Try rooms from smallest capacity to largest, then by lexicographic ID.
        for room_id, capacity in sorted(self._rooms.items(), key=lambda item: (item[1], item[0])):
            if capacity < people:
                continue

            reservations = self._reservations[room_id][day]
            # Two half-open intervals overlap only if they share at least one moment.
            if any(not (end <= reserved_start or start >= reserved_end) for reserved_start, reserved_end, _ in reservations):
                continue

            # First room that fits and is free wins.
            reservations.append((start, end, people))
            return room_id

        raise ReservationException()

    def cancel(self, room_id: str, day: str, start: int, end: int) -> None:
        # Unknown room IDs are not allowed.
        if room_id not in self._rooms:
            raise ReservationException()

        # Look for the exact reservation tuple (same start and end).
        reservations = self._reservations[room_id].get(day, [])
        target_reservation = None
        for reservation in reservations:
            if reservation[:2] == (start, end):
                target_reservation = reservation
                break

        if target_reservation is None:
            raise ReservationException()

        # Remove the matching reservation.
        reservations.remove(target_reservation)

    def schedule(self, room_id: str, day: str) -> tuple:
        # Unknown room IDs should fail immediately.
        if room_id not in self._rooms:
            raise ReservationException()

        # Return the room's reservations for that day in chronological order.
        reservations = self._reservations[room_id].get(day, [])
        return tuple(sorted(reservations, key=lambda reservation: (reservation[0], reservation[1], reservation[2])))


def evaluate_homeworks(path: str) -> tuple:
    """
    Úkol 3

    V předmětu se odevzdává několik domácích úloh a studenti mohou jednu úlohu odevzdat opakovaně.
    Do výsledků se však pro každého studenta a každou úlohu počítá pouze jeho nejlepší pokus.
    Naimplementujte funkci `evaluate_homeworks`, která načte záznamy o odevzdáních ze souboru a
    spočítá celkové výsledky.

    Každý řádek souboru má formát:
    `<student>;<task>;<points>`

    - `<student>` je jméno studenta.
    - `<task>` je název úlohy.
    - `<points>` je celé číslo udávající počet bodů za daný pokus.

    Funkce vrátí dvojici `(student_ranking, task_winners)`.

    `student_ranking` je n-tice dvojic `(student, total_points)`, kde `total_points` je součet
    nejlepších pokusů daného studenta ze všech úloh. Výsledek seřaďte sestupně podle bodů.
    Pokud má více studentů stejný počet bodů, seřaďte je vzestupně podle jména.

    `task_winners` je n-tice trojic `(task, student, points)`. Pro každou úlohu obsahuje studenta,
    který za ni získal nejvíce bodů. Pokud má více studentů stejný nejlepší výsledek, vyberte
    lexikograficky nejmenší jméno studenta. Výsledek seřaďte vzestupně podle názvu úlohy.

    Prázdný soubor vraťte jako `((), ())`.

    Příklad souboru:
    ```
    Anna;hw1;8
    Boris;hw1;10
    Anna;hw1;12
    Anna;hw2;7
    Cyril;hw1;12
    Boris;hw2;5
    Cyril;hw2;7
    ```

    evaluate_homeworks("submissions-simple.txt")
    # (
    #     (("Anna", 19), ("Cyril", 19), ("Boris", 15)),
    #     (("hw1", "Anna", 12), ("hw2", "Anna", 7))
    # )
    """
    # best_attempts[student][task] keeps the best score that student achieved on that task.
    best_attempts: dict[str, dict[str, int]] = defaultdict(dict)

    with open(path, "r", encoding="utf-8") as handle:
        for line in handle:
            line = line.strip()
            if not line:
                continue

            # Parse one submission record: student;task;points.
            student, task, points_text = line.split(";")
            points = int(points_text)
            current_best = best_attempts[student].get(task)
            if current_best is None or points > current_best:
                # Keep only the best submission for each student-task pair.
                best_attempts[student][task] = points

    if not best_attempts:
        return ((), ())

    # Sum each student's best task scores and sort by total desc, name asc.
    student_ranking = tuple(
        sorted(
            ((student, sum(tasks.values())) for student, tasks in best_attempts.items()),
            key=lambda item: (-item[1], item[0]),
        )
    )

    # For every task, remember which student currently leads and with how many points.
    task_best: dict[str, tuple[str, int]] = {}
    for student, tasks in best_attempts.items():
        for task, points in tasks.items():
            if task not in task_best:
                task_best[task] = (student, points)
            else:
                best_student, best_points = task_best[task]
                # Higher score wins; ties are broken by lexicographically smaller name.
                if points > best_points or (points == best_points and student < best_student):
                    task_best[task] = (student, points)

    # Return winners sorted by task name.
    task_winners = tuple(
        (task, student, points)
        for task, (student, points) in sorted(task_best.items(), key=lambda item: item[0])
    )

    return student_ranking, task_winners
