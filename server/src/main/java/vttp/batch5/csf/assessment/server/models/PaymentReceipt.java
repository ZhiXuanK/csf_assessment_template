package vttp.batch5.csf.assessment.server.models;

public class PaymentReceipt {
    
    private String payment_id;

    private String order_id;

    private Long timestamp;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public PaymentReceipt(String payment_id, String order_id, Long timestamp) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.timestamp = timestamp;
    }

    public PaymentReceipt(){
        
    }

}
