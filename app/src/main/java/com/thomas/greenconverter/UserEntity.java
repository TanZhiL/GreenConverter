package com.thomas.greenconverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author: Thomas.<br/>
 * Date: 2020/1/1 10:29<br/>
 * GitHub: https://github.com/TanZhiL<br/>
 * CSDN: https://blog.csdn.net/weixin_42703445<br/>
 * Email: 1071931588@qq.com<br/>
 * Description:
 */
@Entity
public class UserEntity {
    @Id(autoincrement = true)
    private long id;
    @Convert(columnType = String.class,converter = com.thomas.GreenConverter.User_Converter.class)
    private User mUser;
    @Convert(columnType = String.class,converter =com.thomas.GreenConverter.User_ListConverter.class)
    private List<User> mUsers;


    @Generated(hash = 2139644936)
    public UserEntity(long id, User mUser, List<User> mUsers) {
        this.id = id;
        this.mUser = mUser;
        this.mUsers = mUsers;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }





    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getMUser() {
        return this.mUser;
    }

    public void setMUser(User mUser) {
        this.mUser = mUser;
    }

    public List<User> getMUsers() {
        return this.mUsers;
    }

    public void setMUsers(List<User> mUsers) {
        this.mUsers = mUsers;
    }
}
