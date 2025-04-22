class Book
{
private:
  string ean;
  string name;
  string author;
  int year;
  string publisher;
  string description;
  string genre;
  string damageDescription;

public:
  bool updateDamage(string ean, string name, string author, int year, string publisher, string description, string genre, string damageDescription);
  bool removeBook();
};
