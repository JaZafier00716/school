class Library
{
private:
  User *users;
  Branch *branches;
  unsigned int usernum;
  unsigned int branchnum;

public:
  Library();
  Library(Branch *branches, Users *users, unsigned int usernum, unsigned int branchnum);
  ~Library();

  Book searchBook(string query);
  Book getBookDetails(string bookID);
  Book *filterBooks(string *filters);
  Book *getAllBooks();
};
