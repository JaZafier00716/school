from ui.windows import *

if __name__ == "__main__":
    class AppState:
        def __init__(self):
            self.nickname = "ZAM0074"
            self.units: Units = Units.Metric
            self.round = {
                "name": "Course Name",
                "location": "Course Location",
                "par": 72,
                "holes": [
                    {
                        "id": 1,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 2,
                        "par": 3, "distance": {
                        "imperial": 150,
                        "metric": 46
                    }, "throws": 0},
                    {
                        "id": 3,
                        "par": 5, "distance": {
                        "imperial": 500,
                        "metric": 152
                    }, "throws": 0},
                    {
                        "id": 4,
                        "par": 4, "distance": {
                        "imperial": 400,
                        "metric": 122
                    }, "throws": 0},
                    {
                        "id": 5,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 6,
                        "par": 3, "distance": {
                        "imperial": 150,
                        "metric": 46
                    }, "throws": 0},
                    {
                        "id": 7,
                        "par": 5, "distance": {
                        "imperial": 500,
                        "metric": 152
                    }, "throws": 0},
                    {
                        "id": 8,
                        "par": 4, "distance": {
                        "imperial": 400,
                        "metric": 122
                    }, "throws": 0},
                    {
                        "id": 9,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 10,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 11,
                        "par": 3, "distance": {
                        "imperial": 150,
                        "metric": 46
                    }, "throws": 0},
                    {
                        "id": 12,
                        "par": 5, "distance": {
                        "imperial": 500,
                        "metric": 152
                    }, "throws": 0},
                    {
                        "id": 13,
                        "par": 4, "distance": {
                        "imperial": 400,
                        "metric": 122
                    }, "throws": 0},
                    {
                        "id": 14,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 15,
                        "par": 4, "distance": {
                        "imperial": 350,
                        "metric": 107
                    }, "throws": 0},
                    {
                        "id": 16,
                        "par": 3, "distance": {
                        "imperial": 150,
                        "metric": 46
                    }, "throws": 0},
                    {
                        "id": 17,
                        "par": 5, "distance": {
                        "imperial": 500,
                        "metric": 152
                    }, "throws": 0},
                    {
                        "id": 18,
                        "par": 4, "distance": {
                        "imperial": 400,
                        "metric": 122
                    }, "throws": 0},
                ]
            }

        def get_total_score(self):
            total_score = 0
            holes = self.round.get("holes", [])
            hole_items = holes.values() if isinstance(holes, dict) else holes

            for hole in hole_items:
                total_score += hole.get("throws", 0)
            return total_score

        def update_nickname(self, new_nick):
            self.nickname = new_nick
            print(f"Nickname updated to: {self.nickname}")

        def update_units(self, new_units: Units):
            self.units = new_units
            print(f"Units updated to: {self.units}")


    app_state = AppState()

    app = CourseScreen(app_state)
    app.mainloop()
