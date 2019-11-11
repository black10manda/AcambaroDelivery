package Modelos;

public class Usuario {
    private String user;
    private String pass;
    private String name;
    private String phone;
    private String address;
    private String question;
    private String answer;
    private int id;

    public Usuario(String user, String pass, String name, String phone, String address, String question, String answer) {
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.question = question;
        this.answer = answer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario(String user, String pass, int id) {
        this.user = user;
        this.pass = pass;
        this.id = id;
    }

    public Usuario() {
    }

    public Usuario(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
