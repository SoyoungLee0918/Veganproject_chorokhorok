package org.techtown.veganproject.ui.barcode;

public class PersonalData {
    private String member_id;
    private String member_barcode;
    private String member_img;
    private String member_vegantype;
    private String member_raw;

    public String getMember_id() {
        return member_id;
    }

    public String getMember_barcode() {
        return member_barcode;
    }

    public String getMember_vegantype() {return member_vegantype; }

    public String getMember_img() {
        return member_img;
    }

    public String getMember_raw() { return member_raw; }


    public void setMember_id(String member_id) { this.member_id = member_id; }

    public void setMember_barcode(String member_barcode) { this.member_barcode = member_barcode; }

    public void setMember_vegantype(String member_vegantype) { this.member_vegantype = member_vegantype; }

    public void setMember_img(String member_img) { this.member_img = member_img; }

    public void setMember_raw(String member_raw) { this.member_raw = member_raw; }
}