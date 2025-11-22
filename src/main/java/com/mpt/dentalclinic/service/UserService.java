package com.mpt.dentalclinic.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Integer id) {
        return userRepository.findById(id)
                .map(this::removePassword);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return userRepository.findAll().stream()
                .map(this::removePassword)
                .collect(Collectors.toList());
    }

    public Optional<UserEntity> findByEmail(String email) {
    return userRepository.findByEmail(email);
}

    @Transactional(readOnly = true)
    public Page<UserEntity> findAllWithPagination(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(this::removePassword);
    }


    @Transactional(readOnly = true)
    public List<UserEntity> searchUsers(String name, String email, Integer userId) {
        if (userId != null) {
            return userRepository.findById(userId)
                    .map(user -> List.of(removePassword(user)))
                    .orElse(List.of());
        } else if (name != null || email != null) {
            return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    name != null ? name : "",
                    email != null ? email : "")
                    .stream()
                    .map(this::removePassword)
                    .collect(Collectors.toList());
        }
        return findAll();
    }

    public UserEntity updateUser(UserEntity user) {
        UserEntity existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getUserId()));
        
        if (user.getName() != null) existingUser.setName(user.getName());
        if (user.getSurname() != null) existingUser.setSurname(user.getSurname());
        if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getMedicalCardNumber() != null) existingUser.setMedicalCardNumber(user.getMedicalCardNumber());
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        
        return userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserEntity removePassword(UserEntity user) {
        user.setPassword(null);
        return user;
    }
}