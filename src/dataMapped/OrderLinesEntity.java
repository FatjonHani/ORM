package dataMapped;

import javax.persistence.*;

@Entity
@Table(name = "order_lines", schema = "public", catalog = "webshop")
public class OrderLinesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "article_id", nullable = true)
    private Integer articleId;
    @Basic
    @Column(name = "order_id", nullable = true)
    private Integer orderId;
    @Basic
    @Column(name = "amount", nullable = true)
    private Integer amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
