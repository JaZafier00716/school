SELECT Student.fname, Student.lname, COUNT(StudentCourse.course_code) AS cnt
FROM
    Student
    LEFT JOIN StudentCourse ON Student.login = StudentCourse.student_login
WHERE Student.email LIKE '%vsb.cz'
GROUP BY Student.login, Student.fname, Student.lname;
