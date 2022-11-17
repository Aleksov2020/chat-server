package com.app.chatserver.dto;

import com.app.chatserver.models.Avatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    private ResponseMedia avatar;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer postsCounter;
    private Integer subscriberCounter;
    private Integer subscribedCounter;
    private Date dateLastEnter;
    private Date dateLastUpdate;
    private Date dateCreate;
    private Boolean isActive;
    private Boolean isReported;
    private Boolean isBanned;
    private Boolean isOnline;
}
