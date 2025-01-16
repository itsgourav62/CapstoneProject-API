package com.capstone.qwikpay.services;

import java.util.List;

import com.capstone.qwikpay.entities.UserEntity;


public interface UserService {
	public UserEntity addNewUser(UserEntity user);
	//Retrieve
	public UserEntity getUser(Integer userId);
	//Update
	public UserEntity updateUser(UserEntity user);
	//Delete
	public String deleteUser(Integer userId);
	
	//Retrieve All
	public List<UserEntity> getUsers();
}
