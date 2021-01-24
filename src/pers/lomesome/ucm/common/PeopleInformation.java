package pers.lomesome.ucm.common;

import java.io.Serializable;

public class PeopleInformation implements Serializable {
    private static final long serialVersionUID = -8433144405520117163L;
    private String userid;
    private String nickname;
    private String note;
    private String head;
    private String sex;
    private String age;
    private String signature;
    private String phonenumber;
    private String birthday;
    public PeopleInformation(){}
    public PeopleInformation(String userid, String nickname, String note, String head, String sex, String age, String signature, String phonenumber, String birthday){
        this.userid = userid;
        this.nickname = nickname;
        this.note = note;
        this.head = head;
        this.sex = sex;
        this.age = age;
        this.signature = signature;
        this.phonenumber = phonenumber;
        this.birthday = birthday;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString(){
        return "userid: " + userid + " nickname: " + nickname;
    }

    public boolean equalsObjct(PeopleInformation p){
        return this.userid.replace("@@##","").equals(p.getUserid().replace("@@##",""));
    }

    public boolean myEquals(PeopleInformation peopleInformation){
        return this.sex.equals(peopleInformation.getSex()) && this.age == peopleInformation.getAge() && this.birthday == peopleInformation.getBirthday();
    }
}
