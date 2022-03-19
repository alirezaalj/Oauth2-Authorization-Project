package ir.alirezaalijani.security.authorization.service.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "mails")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationMail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(length = 100)
    private String toMail;
    @Column(length = 100)
    private String fromMail;
    @Column(length = 100)
    private String subject;
    @Lob
    private String message;
    private Date sendAt;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private Boolean isSend;
    @Lob
    private String actionUrl;
    @Column(length = 150)
    private String templateHtml;
}
