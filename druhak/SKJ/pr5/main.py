from fastapi import Depends, FastAPI
from pydantic import BaseModel, Field
from typing import Optional

from sqlalchemy.orm import Session
from database import engine
import models

models.Base.metadata.create_all(bind=engine)



class TaskCreate(BaseModel):
    title: str = Field(..., max_length=50, description="Title of the task", min_length=5)
    # description: Optional[str] = None
    description: str | None = Field(None, description="Description of the task")
    completed: bool = Field(False, description="Whether the task is completed or not")

    model_config = {
        "json_schema_extra": {
            "example": {
                "title": "Task title",
                "description": "Task description",
                "completed": True,
            }
        }
    }

app = FastAPI()


def get_db():
    db = Session(bind=engine.connect())
    try:
        yield db
    finally:
        db.close()

@app.get("/")
def read_root():
    return {"message": "Welcome to API", "version": "0.1"}

"""
@app.get("/tasks/")
def get_tasks():
    return tasks_db


@app.post("/tasks/")
def create_task(task: TaskCreate):
    tasks_db.append(task)
    return {"message": "Task created", "task": task}
"""

@app.get("/db-tasks/")
def get_db_tasks(db: Session = Depends(get_db), tags=["db-tasks"]):
    return db.query(models.TaskDB).all()

@app.post("/db-tasks/", response_model=TaskCreate, tags=["db-tasks"], summary="Create a new task in the database", response_description="Task created successfully")
def create_db_task(task: TaskCreate, db: Session = Depends(get_db)):
    db_task = models.TaskDB(title=task.title, description=task.description, completed=task.completed)
    db.add(db_task)
    db.commit()
    db.refresh(db_task)
    return db_task

@app.post("/db-mark-completed/{task_id}", tags=["db-tasks"])
def mark_task_completed(task_id: int, db: Session = Depends(get_db)):
    task_select = db.query(models.TaskDB).filter(models.TaskDB.id == task_id)
    task = db.scalar(task_select)

    task.completed = True
    db.commit()
    db.refresh(task)
    return task



# pedantic - zabrani vlozeni krychle do trojuhelnikove diry
# pokud ho tam nedame, tak nam dirou projde i granat
