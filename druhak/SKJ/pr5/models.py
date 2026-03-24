from database import Base
from sqlalchemy import String, Boolean
from sqlalchemy.orm import Mapped, mapped_column

class TaskDB(Base):
    __tablename__ = 'tasks'
    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    title: Mapped[str] = mapped_column(String(50))
    description: Mapped[str] = mapped_column(String, nullable=True)
    completed: Mapped[bool] = mapped_column(Boolean, default=False)

    def __repr__(self) -> str:
        return f"TaskDB(id={self.id}, title={self.title}, description={self.description}, completed={self.completed})"

