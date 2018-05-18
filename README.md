# BPC
Student Network

Please present the following details to anyone testing the app, as they are required to login

Administrator Login:
Username: admin
Password: admin

User Login:
Username: jon
Password: snow

The idea is to keep students of a specific school or university up to date via a centralised backend with an appealing design.

Backend
All data is served to the app by the means of a mBAAS, namely backendless. The server/databse setup can be recreated from the simple POJOs. 

Security
With steps taken to ensure security and reliability. HTTPS connections are made when connecting to the Backend for protection against hacks. Class and deep object level permissions to protect data from attacks.

Login
Students will login with the provided details and select their subjects and society which they are a member of.

Bulletin / Society
The Bulletin contains updates and/or news from the school administration. Society contains news from an authority figure of the respective society. When an update is posted by such a figure, all students are notified of it, incase if the society, only the members are notified.

Assignments
Students get assignments of the subjects they chose. The app provides a system for managing said assignments. They are separated by those on going, those overdue and those completed. A notification is sent to all students who study a specific subject when a new assignment is posted.

Timetable
According to the subjects chosen by the student, a personalised timetable will be set up for the week.

Events
All major school events are available through the app with a system set up specifically to manage event attendees. This bundled with details such as location, contact information etc.

Teachers
The school faculty respective to the chosen class, including teachers and administration are available through the app. With students being able to contact their respective teachers.


— — — — — — — — — — — — — — — — — — — — — — —


INTERNET
Required for data transfer to and from the backend.

ACCESS_NETWORK_STATE
Required for handling situations where network is unavailable, busy etc.

VIBRATE
Required to vibrate the device when a notification is received.

WAKE_LOCK
Required to turn device screen on when a notification arrives

GET_ACCOUNTS
Required by the backend to fallback on its own notification system for devices that do not support GCM.

RECEIVE_BOOT_COMPLETED
Required to setup notification receiving system when device is booted up.

WRITE_EXTERNAL_STORAGE
Required by Google Maps

— — — — — — — — — — — — — — — — — — — — — — —


The app/project is near completion, with some things that are to be finalised, but its safe to say that its the first of its kind in Pakistan and its something that I feel will truly help students.

An app/project made for students, by a student. 
~ Me (Lincoln totally didn’t say anything similar)
