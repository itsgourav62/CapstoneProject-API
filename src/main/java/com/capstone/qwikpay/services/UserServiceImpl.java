package com.capstone.qwikpay.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repo;
	
	
	@Override
	public UserEntity addNewUser(UserEntity user) {
		// TODO Auto-generated method stub
		return repo.save(user);
	}
	
	@Override
	public UserEntity getUser(Integer userId) {
		// TODO Auto-generated method stub
		Optional<UserEntity> user = repo.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}

	@Override
	public UserEntity updateUser(UserEntity user) {
		// TODO Auto-generated method stub
		if(repo.existsById(user.getId())) {
			UserEntity updateUser= repo.save(user);
		}
		return user;
	}

	@Override
	public String deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		repo.deleteById(userId);
		return "User with customer id "+userId+" deleted successfully";
	}

	@Override
	public List<UserEntity> getUsers() {
		// TODO Auto-generated method stub
		return (List<UserEntity>) repo.findAll();
	}
	
}
