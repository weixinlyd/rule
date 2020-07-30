package com.study.mapper;
import com.study.entity.Role;
import com.study.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserMapper {

     User loadUserByUsername(@Param("userName") String userName);

     List<Role> getUserRolesByUid(@Param("id") Integer id);

}