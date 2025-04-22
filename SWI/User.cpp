enum Roles
{
  User,
  LoggedUser,
  Employee,
  Manager,
};

class User
{
private:
  string id;
  string firstname;
  string lastname;
  string email;
  Roles role;

public:
  User(string id, string firstname, string lastname, string email);
  ~User();

  bool login(string email, string password);
  bool register(string firstname, string lastname, string email, string password);
  void changePassword(string password);
};
