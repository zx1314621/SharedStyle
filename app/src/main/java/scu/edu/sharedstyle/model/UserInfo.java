package scu.edu.sharedstyle.model;

public class UserInfo {
    private String UID;
    private String Name;
    private String Phone;
    private String Street;
    private String State;
    private String ZIP;

    public String getUID() {return UID;}

    public void setUID(String UID){this.UID = UID;}

    public String getName() {return Name;}

    public void setName(String Name){this.Name=Name;}

    public String getPhone(){return Phone;}

    public void setPhone(String Phone){this.Phone=Phone;}

    public String getStreet(){return Street;}

    public void setStreet(String Street){this.Street=Street;}

    public String getState(){return State;}

    public void setState(String State){this.State=State;}

    public String getZIP(){return ZIP;}

    public void setZIP(String ZIP){this.ZIP=ZIP;}

    public UserInfo(){}

    public UserInfo(String UID, String Name){
        this.UID=UID;
        this.Name=Name;
    }
}
