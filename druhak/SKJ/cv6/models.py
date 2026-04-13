from sqlalchemy import String, Integer, ForeignKey
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column, relationship

class Base(DeclarativeBase):
    pass

class User(Base):
    __tablename__ = 'users'

    id: Mapped[Integer] = mapped_column(Integer, primary_key = True)
    name: Mapped[str] = mapped_column(String(50), nullable = False)
    email: Mapped[str] = mapped_column(String(100))
    age: Mapped[int] = mapped_column(Integer, nullable = True)

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
