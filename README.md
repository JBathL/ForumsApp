# ForumsApp
An android app for posting and replying to forums. 
This app was made through colloboration with Jacob Mack at jmack23@uncc.edu

Installation and Running
  To install this app you will need to download Android Studio and the project files. Once downloaded, open Android Studio and go to file,
  open. Either find the project files through the exporler there, or drag and drop them in. You will want to click on the file that has a 
  small green android icon, otherwise you'll just be opening the file paths and you can't run the project. Now, you will need to install
  the correct emulator, or download the app directly onto your android phone. Go to AVD Manager and find "Nexus 5," and install it. 
  Then you can hit the triangle button at the top to run the program, start up of the emulator takes a while. To put it on your 
  phone you should first connect your phone to the computer. Put your phone in developer mode, each android device is different so look
  up how to do yours. --------

Database
  Firebase, a google powered database. For more information about Firebase, go to https://firebase.google.com/ . 
  Users email, password, and user id are stored here. Forums, their comments, post author, and time are all stored here as well. 
  Deleting a forum, by pressing the trash can icon on posts you have made, will remove it from the database completely. 
  
Users
  Currently, email addresses don't have to be confirmed, or even actually exist. For testing purposes, 
  you can use any email and it won't effect that email in any way. With the settings we chose, Firebase only checks that the 
  characters match for authorization.
  
User Interface and Design
  At the current state this app is in, the UI design was not heavily considered; functionality was prioritized. It was made using
  a constraint layout, which means that components are placed in terms of dp. Whether that be, number of dp away from another 
  component or the margin. The code for this is in XML format. 
