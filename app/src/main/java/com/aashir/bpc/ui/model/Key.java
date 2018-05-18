package com.aashir.bpc.ui.model;

public class Key {

    public static class Class {
        public static final String[] ASSIGNMENT = {"Assignment_A1", "Assignment_A2"};

        public static final String BULLETIN = "Bulletin";

        public static final String COMMENTS = "Comments";

        public static final String EVENT = "Events";

        public static final String IMAGES = "Images";

        public static final String[] SOCIETY = {"Society_Art", "Society_It", "Society_Media"};

        public static final String TEACHERS = "Teachers";

        public static final String[] TIMETABLE = {"Timetable_A1", "Timetable_A2"};
    }

    public static class User {
        public static final String NAME = "name";
        public static final String IMAGE = "image";
    }

    public static class Post {
        public static final String AUTHOR = "postAuthor";
        public static final String CONTENT = "postContent";
        public static final String IMAGE = "postImage";
        public static final String IMAGE_THUMB = "postImageThumb";
        public static final String TIME = "postTime";
    }

    public static class Assignment {
        public static final String SUBJECT = "subjectName";
        public static final String TITLE = "assTitle";
        public static final String CONTENT = "assContent";
        public static final String POST_TIME = "postTime";
        public static final String DUE_TIME = "dueTime";
        public static final String COMPLETED_BY = "completedBy";
        public static final String TEACHER = "subjectTeacher";
    }

    public static class TimeTable {
        public static final String SUBJECT = "subjectName";
        public static final String ON_DAY = "onDay";
        public static final String FROM_TIME = "fromTime";
        public static final String TO_TIME = "toTime";
    }

    public static class Event {
        public static final String TITLE = "eventTitle";
        public static final String TIME = "eventTime";
        public static final String CONTENT = "eventContent";
        public static final String IMAGE = "eventImage";
        public static final String VENUE = "eventVenue";
        public static final String PHONE = "eventPhone";
        public static final String ADDRESS = "eventAddress";
        public static final String LOCATION = "eventLocation";
        public static final String ATTENDEES = "eventAttendees";
    }

    public static class Comment {
        public static final String PARENT = "commParent";
        public static final String AUTHOR = "commAuthor";
        public static final String CONTENT = "commContent";
        public static final String TIME = "commTime";
    }

    public static class Teacher {
        public static final String NAME = "teacherName";
        public static final String IMAGE = "teacherPicture";
        public static final String SUBJECT = "teacherSubject";
        public static final String PHONE = "teacherPhone";
        public static final String EMAIL = "teacherEmail";
    }

    public static class Image {
        public static final String IMAGE = "image";
    }

    public static class Preferences {
        public static final String ID = "pref_id_school";
        public static final String SOCIETY = "pref_society";
        public static final String SUBJECTS = "pref_subjects";
        public static final String ASSIGNMENT = "pref_assignment";
        public static final String TIMETABLE = "pref_timetable";
    }
}
