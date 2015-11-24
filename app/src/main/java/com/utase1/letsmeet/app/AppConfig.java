package com.utase1.letsmeet.app;

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://omega.uta.edu/~axr1662/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://omega.uta.edu/~axr1662/register.php";

    //Server create meeting url
    public static String URL_CREATEMEET = "http://omega.uta.edu/~axr1662/createmeet.php";

    //Server create event url
    public static String URL_CREATEEVENT = "http://omega.uta.edu/~axr1662/createevent.php";

    //Server create event url
    public static String URL_USERTIME = "http://omega.uta.edu/~axr1662/user_time.php";

    //Server get user free time
    public static String URL_getUsersAndFreeTime = "http://omega.uta.edu/~axr1662/getUsersAndFreeTime.php";

    //Server get user meetings
    public static String URL_GETMEETING = "http://omega.uta.edu/~axr1662/getmeetings.php";

    //Server put final event response
    public static String URL_GETEVENT = "http://omega.uta.edu/~axr1662/getevent.php";

    //Server get accepted info
    public static String URL_GETACCEPTED = "http://omega.uta.edu/~axr1662/final_accepted.php";
    public static String URL_GETDECLINED = "http://omega.uta.edu/~axr1662/final_declined.php";

    // Adding below URL's as a part of Google cloud messaging
    //Server put final event response
    public static String URL_REGGCM = "http://omega.uta.edu/~axr1662/storeGCMDetails.php";

    //used to share GCM regId with application server - using php app server
    public static String URL_SENDMESS = "http://omega.uta.edu/~axr1662/sendMessageGcm.php";

    //public static String GOOGLE_PROJECT_ID = "197366189680";
    // LetsMeet key: AIzaSyA1FmHLspN8Y_o2hWyRILGzRHUPqbWtwzw
    public static String GOOGLE_PROJECT_ID = "352921123411";
    public static String MESSAGE_KEY = "message";
    public static String URL_SCHEDULEFINALMEET = "http://omega.uta.edu/~axr1662/scheduleFinalMeeting.php";

}
