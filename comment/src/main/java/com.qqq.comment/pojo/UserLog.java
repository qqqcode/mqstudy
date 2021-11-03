package com.qqq.comment.pojo;


import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@Builder
@ToString
public class UserLog implements Serializable {

    private String logName;

    private String logType;

    private String userName;

}
