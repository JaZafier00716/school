from database import engine
from models import User, Post

from sqlalchemy import select
from sqlalchemy.orm import Session
from sqlalchemy import text

# with Session(engine) as session:
    # u1 = User(name = "Adam", email = "adam@vsb.cz")
    # u2 = User(name = "Pepa", email = "pepa@vsb.cz")
    #
    # session.add_all([u1, u2])
    # session.commit()


with Session(engine) as session:
    stmt = select(User)

    result = session.execute(stmt).scalars().all()

    for user in result:
        print(user)

with Session(engine) as session:
    result = session.execute(text("SELECT * FROM users"))

    for x in result:
        print(x)