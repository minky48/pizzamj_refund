package pizzamj;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Refund_table")
public class Refund {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String reason;

    @PostPersist
    public void onPostPersist(){
        Refunded refunded = new Refunded();
        BeanUtils.copyProperties(this, refunded);
        refunded.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        pizzamj.external.Delivery delivery = new pizzamj.external.Delivery();

        delivery.setOrderId(this.getOrderId());
        delivery.setDeliveryStatus("refunded");

        // mappings goes here
        RefundApplication.applicationContext.getBean(pizzamj.external.DeliveryService.class)
            .delivery(delivery);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }




}
