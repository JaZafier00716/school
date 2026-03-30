from sqlalchemy import create_engine, String, Integer, select, ForeignKey
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, Session, relationship

engine = create_engine('sqlite:///db.sqlite', echo=True) # echo allows detailed description


class Base(DeclarativeBase):
    pass

class User(Base):
    __tablename__ = 'users'

    id: Mapped[Integer] = mapped_column(Integer, primary_key = True)
    name: Mapped[str] = mapped_column(String(50), nullable = False)
    email: Mapped[str] = mapped_column(String(100))

    posts: Mapped[list["Post"]] = relationship(back_populates = "author") # not a column in the table just represents the relationship for sync

    def __repr__(self):
        return f"User(id={self.name}, name={self.name}, email={self.email})"

class Post(Base):
    __tablename__ = 'posts'
    id: Mapped[Integer] = mapped_column(Integer, primary_key = True)
    title: Mapped[str] = mapped_column(String(100), nullable = False)
    content: Mapped[str] = mapped_column(String(500))

    # Keep DB compatibility: existing table uses column name "author".
    author_id: Mapped[int] = mapped_column("author", ForeignKey("users.id"), nullable=False)
    author: Mapped["User"] = relationship(back_populates = "posts")

    def __repr__(self):
        return f"Title: {self.title}\nContent: {self.content}"


Base.metadata.create_all(engine)

# Insert
with Session(engine) as session:
    existing_emails = {
        user.email for user in session.execute(select(User)).scalars().all()
    }

    users_to_add = []
    if "adam@vsb.cz" not in existing_emails:
        users_to_add.append(User(name = "Adam", email = "adam@vsb.cz"))
    if "namornik@vsb.cz" not in existing_emails:
        users_to_add.append(User(name = "Pepek", email = "namornik@vsb.cz"))

    session.add_all(users_to_add)

    session.commit() # pro zapsani do databaze

# Select
with Session(engine) as session:
    stmt = select(User)

    result = session.execute(stmt).scalars().all()

    for user in result:
        print(user)

# Update
with Session(engine) as session:
    stmt = select(User).where(User.email == 'adam@vsb.cz')

    user = session.execute(stmt).scalars().first()
    if user is None:
        raise RuntimeError("User with email adam@vsb.cz was not found")

    user.name = 'Adam Albert'

    #session.delete(user)

    session.commit()


with Session(engine) as session:
    posts = [
        Post(title = "title1", content = "content1"),
        Post(title = "title2", content = "content2"),
    ]

    stmt = select(User).where(User.email == 'adam@vsb.cz')

    user = session.execute(stmt).scalars().first()
    if user is None:
        raise RuntimeError("User with email adam@vsb.cz was not found")

    user.posts = posts
    session.commit()


with Session(engine) as session:
    stmt = select(User).where(User.email == 'adam@vsb.cz')

    result = session.execute(stmt).scalars().all()

    for user in result:
        if user.name == 'Adam Albert':
            for post in user.posts:
                print(post)


