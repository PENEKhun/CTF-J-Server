package com.penekhun.ctfjserver.User.Entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter //for model mapper
@Table(name = "Problem", schema = "ctf")
@EntityListeners(AuditingEntityListener.class)
public class Problem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long id;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "flag", nullable = false, length = 150)
    private String flag;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @Column(name = "min_score", nullable = false)
    private Integer minScore;

    @Column(name = "solve_threshold", nullable = false)
    private Integer solveThreshold;

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @LastModifiedBy
    @Column(name = "modify_time", nullable = false, updatable = false)
    private Timestamp modifyTime;

    public boolean isCorrect(String flag){
        return (this.flag.equals(flag));
    }

    public Boolean isPublic() {
        return isPublic;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthorId(Account account) {
        this.authorId = account.getId();
    }
}