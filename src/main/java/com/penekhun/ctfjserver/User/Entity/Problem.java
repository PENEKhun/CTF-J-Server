package com.penekhun.ctfjserver.User.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Problem", schema = "ctf")
public class Problem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "flag", nullable = false, length = 100)
    private String flag;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "default_score", nullable = false)
    private Integer defaultScore;

    @Column(name = "modify_time", nullable = false)
    private Instant modifyTime;

    public boolean isCorrect(String flag){
        return (this.flag.equals(flag));
    }

    public Instant getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Instant modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(Integer defaultScore) {
        this.defaultScore = defaultScore;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthorId(Account account) {
        this.authorId = account.getId();
    }
}