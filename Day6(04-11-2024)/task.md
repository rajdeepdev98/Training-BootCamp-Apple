Question 4:

declare a array of tuple like this

val records = Array(
  (1, "Alice", 85), (2, "Bob", 92), (3, "Charlie", 78), (4, "David", 66), (5, "Eve", 90),
  (6, "Frank", 73), (7, "Grace", 88), (8, "Hannah", 91), (9, "Isaac", 84), (10, "Judy", 76),
  (11, "Kevin", 82), (12, "Laura", 79), (13, "Mike", 95), (14, "Nina", 70), (15, "Oscar", 89),
  (16, "Paul", 80), (17, "Quinn", 77), (18, "Rachel", 93), (19, "Sam", 85), (20, "Tina", 74),
  (21, "Uma", 69), (22, "Victor", 96), (23, "Wendy", 87), (24, "Xander", 68), (25, "Yara", 94),
  (26, "Zane", 81), (27, "Oliver", 78), (28, "Sophia", 85), (29, "Liam", 90), (30, "Mia", 83),
  (31, "Noah", 88), (32, "Emma", 75), (33, "Ava", 92), (34, "William", 86), (35, "James", 91),
  (36, "Lucas", 72), (37, "Amelia", 79), (38, "Ella", 89), (39, "Mason", 76), (40, "Logan", 95),
  (41, "Ethan", 84), (42, "Charlotte", 82), (43, "Benjamin", 80), (44, "Alexander", 71),
  (45, "Michael", 88), (46, "Isabella", 73), (47, "Daniel", 86), (48, "Elijah", 81),
  (49, "Matthew", 79), (50, "Jackson", 92)
)

in the StudentOps function defined earlier
add a function filterStudents(x:Student => Boolean): List[Student]

and Array[Student] should be implicitly converted into List[Student]

Eventhough we use record variable which is array type we must
be able to call filterStudents

Test the filterStudents functions by passing records variable to the constructor

Question 5:

Create an application
that asks you to enter (sno,name,city,department)
and add the the tuple record in the appropriate deparment 
and print the organization tree
Create a tree data structure of your own
 Note: Do not use map or any other collection

 and for representing (sno,name,city) use tuple 
 and the application must be interactive
 need to stop when you say exit
 
Organization
└── Finance
    ├── Payments
    │   ├── (1,Ravi,Chennai)
    │   ├── (2,Ram,Chennai)
    │   
    │  
    │   
    │   
    │   
    └── Sales
        ├── Marketing
        │   ├── (3,"Rohan","Kolkata")
        │   ├── (4,"RAkesh","Mumbai")
        │ 
        ├── (5,Ravi,Mumbai)
        ├
        ├── Advertisements
        │   ├── (6,Ricky,Chennai)
        │   
        │ 
        │  
        └── SalesManagement

  

  