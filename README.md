# Library Management System
_(This project was done for academic class of Advanced DBMS. Here Document oriented database program (MongoDb) is used along with Java)_

A library management system using Mongo DB and Java where the admin can add or remove books and users can borrow a copy of a book. This system has the following main use cases: 
-  A user assigned as a library manager can add, update, or delete information for books and loan records.
-  A user can login into the user system (assuming that she has already registered).
    - If the user is a library manager, she will have a main screen to manage book and loan information. If the user is a normal user, her screen allows her to borrow or return books. This user system is developed as a desktop-based GUI application.
-  A user can search for books using a web-based system. In the web browser, the user can specify in the URL to search for book title, author, or publisher. The web page displays search results ranked by relevance based on text similarity.
-  A user can borrow a copy of a book. The loan record has at least the borrow date and the due date. When the user returns the book, the loan record is added with the actual return date.
-  A library manager can add a new book to the database or update its field (e.g., title, price). He/She can also add or remove a copy of a book.
-  A library manager can also view the loan records. He/She can extend the due date for a loan record.


