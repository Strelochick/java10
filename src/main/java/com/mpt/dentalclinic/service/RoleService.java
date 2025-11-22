package com.mpt.dentalclinic.service;

import com.mpt.dentalclinic.entity.RoleEntity;
import com.mpt.dentalclinic.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity createRole(RoleEntity role) {
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new IllegalArgumentException("Роль с таким названием уже существует!");
        }
        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Optional<RoleEntity> findById(Integer id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<RoleEntity> findAllWithPagination(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<RoleEntity> searchRoles(String name, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            return roleRepository.findAll(pageable);
        }
        return roleRepository.findByRoleNameContainingIgnoreCase(name, pageable);
    }

    public RoleEntity updateRole(RoleEntity role) {
        Optional<RoleEntity> existingRole = roleRepository.findByRoleName(role.getRoleName());
        if (existingRole.isPresent() && !existingRole.get().getRoleId().equals(role.getRoleId())) {
            throw new IllegalArgumentException("Роль с таким названием уже существует!");
        }
        return roleRepository.save(role);
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}