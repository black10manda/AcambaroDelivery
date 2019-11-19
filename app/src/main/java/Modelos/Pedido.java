package Modelos;

public class Pedido {
    private int id;
    private String user_id;
    private String type;
    private String money;
    private String address1;
    private String people1;
    private String indications1;
    private String address2;
    private String people2;
    private String indications2;
    private String status;

    public Pedido() {
    }

    public Pedido(String user_id, String type, String money, String address1, String people1, String indications1, String address2, String people2, String indications2, String status) {
        this.user_id = user_id;
        this.type = type;
        this.money = money;
        this.address1 = address1;
        this.people1 = people1;
        this.indications1 = indications1;
        this.address2 = address2;
        this.people2 = people2;
        this.indications2 = indications2;
        this.status = status;
    }

    public Pedido(int id, String user_id, String type, String money, String address1, String people1, String indications1, String address2, String people2, String indications2, String status) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.money = money;
        this.address1 = address1;
        this.people1 = people1;
        this.indications1 = indications1;
        this.address2 = address2;
        this.people2 = people2;
        this.indications2 = indications2;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getPeople1() {
        return people1;
    }

    public void setPeople1(String people1) {
        this.people1 = people1;
    }

    public String getIndications1() {
        return indications1;
    }

    public void setIndications1(String indications1) {
        this.indications1 = indications1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPeople2() {
        return people2;
    }

    public void setPeople2(String people2) {
        this.people2 = people2;
    }

    public String getIndications2() {
        return indications2;
    }

    public void setIndications2(String indications2) {
        this.indications2 = indications2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
