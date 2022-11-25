package com.healthcare.dogtraining.ui.Ship;

public class ShipOrderModel {

    String id, user_id,address,total_amount,payment_method,transaction_id,status,
            order_status,date_time,order_id,product_id, quanitity,cat_id, name, price,discounted_price,
            image,color,age;

    public ShipOrderModel(String id, String user_id, String address,
                                     String total_amount, String payment_method,
                                     String transaction_id, String status, String order_status,
                                     String date_time,String order_id,
                                     String product_id, String quanitity, String cat_id,
                                     String name, String price, String discounted_price,
                                     String image,String color,String age ) {

        this.id=id;
        this.user_id=user_id;
        this.address=address;
        this.total_amount=total_amount;
        this.payment_method=payment_method;
        this.transaction_id=transaction_id;
        this.status=status;
        this.order_status=order_status;
        this.date_time=date_time;
        this.order_id=order_id;
        this.product_id=product_id;
        this.quanitity=quanitity;
        this.cat_id=cat_id;
        this.name=name;
        this.price=price;
        this.discounted_price=discounted_price;
        this.image=image;
        this.color=color;
        this.age=age;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getQuanitity() {
        return quanitity;
    }

    public void setQuanitity(String quanitity) {
        this.quanitity = quanitity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
