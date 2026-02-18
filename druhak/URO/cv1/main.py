import random
import customtkinter as ctk

class BikeApp(ctk.CTk):
    def __init__(self):
        super().__init__()
        self.title("Bike App")
        self.geometry("1200x800")
        self.font = ("JetBrains Mono", 20)
        self.padding = 10
        self.create_form()

    def create_form(self):
        self.brand_label = ctk.CTkLabel(self, text="Brand", font=self.font)
        self.brand_label.pack(pady=self.padding)
        self.brand_input = ctk.CTkEntry(self, placeholder_text="Brand", font=self.font)
        self.brand_input.pack(pady=self.padding)
        self.model_label = ctk.CTkLabel(self, text="Model", font=self.font)
        self.model_label.pack(pady=self.padding)
        self.model_input = ctk.CTkEntry(self, placeholder_text="Model", font=self.font)
        self.model_input.pack(pady=self.padding)
        self.size_label = ctk.CTkLabel(self, text="Size", font=self.font)
        self.size_label.pack(pady=self.padding)
        self.size_input = ctk.CTkOptionMenu(self, values=SIZE, font=self.font)
        self.size_input.pack(pady=self.padding)
        self.submit_button = ctk.CTkButton(self, text="Submit", font=self.font, command=self.save_bike)
        self.submit_button.pack(pady=self.padding)

    def save_bike(self):
        brand = self.brand_input.get()
        model = self.model_input.get()
        size = self.size_input.get()

        if(not brand or not model or not size):
            print("Please fill in all fields")
            return
        
        bike = Bike(brand, model, size)
        with open("bikes.csv", "a+") as f:
            f.write(bike.to_csv() + "\n")
        print(f"Saved bike: {bike}")
        self.brand_input.delete(0, ctk.END)
        self.model_input.delete(0, ctk.END)
        self.size_input.set(SIZE[0])

class Bike:
    id = 0

    def __init__(self, brand, model, size):
        self.id = Bike.id
        Bike.id += 1
        self.brand = brand
        self.model = model
        self.size = size

    def new():
        brand = random.choice(list(BRAND_MODEL.keys()))
        model = random.choice(BRAND_MODEL[brand])
        size = random.choice(SIZE)
        return Bike(brand, model, size)
    
    def __str__(self):
        return f"Bike(id={self.id}, brand={self.brand}, model={self.model}, size={self.size}\n"
    
    def __repr__(self):
        return str(self)
    
    def to_csv(self):
        return f"{self.id}, {self.brand}, {self.model}, {self.size}"



SIZE = ["S", "M", "L", "XL"]
BRAND_MODEL = {
    "Bianchi": ["Stadale", "Piastri"],
    "Giant": ["Trance", "TCR"],
    "Autor": ["Domane", "Omnium"],
}

if __name__ == "__main__":
    bikes = [Bike.new() for i in range(10)]
    print(bikes)

    with open("bikes.csv", "a+") as f:
        for bike in bikes:
            f.write(bike.to_csv() + "\n")

    app = BikeApp()
    app.mainloop()