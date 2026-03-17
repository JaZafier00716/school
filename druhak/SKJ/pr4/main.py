from fastapi import FastAPI
from pydantic import BaseModel
from typing import Optional

class TaskCreate(BaseModel):
    title: str
    description: Optional[str] = None
    completed: bool = False


app = FastAPI()

tasks_db = []


@app.get("/")
def read_root():
    return {"message": "Welcome to API", "version": "0.1"}

@app.get("/tasks")
def get_tasks():
    return tasks_db

@app.post("/tasks/")
def create_task(task: TaskCreate):
    tasks_db.append(task)
    return {"message": "Task created", "task": task}

# pedantic - zabrani vlozeni krychle do trojuhelnikove diry
# pokud ho tam nedame, tak nam dirou projde i granat
