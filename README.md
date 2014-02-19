DatabaseHandler
DatabaseHandler
===============

DBhandler for swansea group project


1) Add the jars from LIB and JAR to your eclipse/netbeans build path.

2) At the top of your Java file, add this line:
import groupProject.jamie.DatabaseHandler;
If you see an error saying something about SQLITE and JDBC, you haven't added the SQLite driver from the libs folder.


So, the way it works is you create an instance of the databaseHandler class like so

DatabaseHandler dH = new DatabaseHandler("Johnny"),  assuming the username is Johnny.

This will automatically create a Johnny.db file in your root folder.

You can then query the dH object to make posts, request a follower relationship etc.
