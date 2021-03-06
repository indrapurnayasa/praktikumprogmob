package id.ac.unud1805551038.projectprogmob;

public class Constant {
    public static final String URL = "https://indra.pemirahimanikaunud.web.id/";
    public static final String HOME = URL+"api";
    public static final String LOGIN = HOME+"/login";
    public static final String REGISTER = HOME+"/register";
    public static final String SAVE_USER_INFO = HOME+"/save_user_info";
    public static final String TASK = HOME+"/posts";
    public static final String GET_USER = HOME+"/userRead";
    public static final String SET_USER = HOME+"/updateprofile";
    public static final String UPDATE_PASSWORD = HOME+"/updatepassword";
    public static final String MY_TASK = TASK+"/mytask";
    public static final String LOGOUT = HOME+"/logout";
    public static final String GET_PROFILE = HOME+"/getprofile";


    //TASK CONSTANT
    public static final String ADD_TASK = HOME+"/posts/create";
    public static final String DELETE_TASK = HOME+"/posts/delete";
    public static final String UPDATE_TASK = HOME+"/posts/update";


}
