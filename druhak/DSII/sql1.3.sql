SELECT Student.fname, Student.lname, COUNT(StudentCourse.course_code) AS cnt
FROM
    Student
    LEFT JOIN StudentCourse ON Student.login = StudentCourse.student_login
WHERE Student.email LIKE '%vsb.cz'
GROUP BY Student.login, Student.fname, Student.lname;


SELECT Student.fname, Student.lname, COUNT(DISTINCT Course.teacher_login) AS cnt
FROM
    Student
    LEFT JOIN StudentCourse ON Student.login = StudentCourse.student_login AND StudentCourse.year = 2020
    LEFT JOIN Course ON StudentCourse.course_code = Course.code
GROUP BY Student.login, Student.fname, Student.lname;

-- 6
with T as (
    select teacher.login, teacher.fname, teacher.lname, count(course.code) as cnt
    from teacher
    left join course on teacher.login = course.teacher_login
    group by teacher.login, teacher.fname, teacher.lname 
)

select *
from T
where cnt = (select max(cnt) from T);


insert into teacher (login, fname, lname, department) values ('bur154', 'Peter', 'Burton', 'Department of Mathematic');

delete from StudentCourse 
where course_code in(
    select code
    from Course
    join Teacher on Course.teacher_login = Teacher.login
    where teacher.fname = 'Carl' and teacher.lname = 'Artis'
);


update student
set date_of_birth = TO_DATE('1997-03-02', 'YYYY-MM-DD')
where login = 'smi324';


select login, fname, MONTHS_BETWEEN(CURRENT_TIMESTAMP, date_of_birth) / 12 as age
from student;

select 
    login,
    extract(year from date_of_birth) as year,
    extract(month from date_of_birth) as month,
    extract(day from date_of_birth) as day
from student;

select UPPER(fname || ' ' || lname) as full_name
from teacher