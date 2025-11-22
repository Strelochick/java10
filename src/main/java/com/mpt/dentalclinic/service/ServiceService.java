package com.mpt.dentalclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mpt.dentalclinic.entity.ServiceEntity;
import com.mpt.dentalclinic.model.ServiceModel;
import com.mpt.dentalclinic.repository.ServiceRepository;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceEntity createService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    @Transactional(readOnly = true)
    public Optional<ServiceEntity> findById(Integer id) {
        return serviceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ServiceEntity> findAll() {
        return serviceRepository.findAll();
    }

    @Transactional(readOnly = true)
public Page<ServiceEntity> searchServicesWithPagination(
    String name, 
    Integer categoryId, 
    Pageable pageable) {
    
    System.out.println("Searching services with name: " + name + ", categoryId: " + categoryId);
    
    if (!StringUtils.hasText(name) && categoryId == null) {
        Page<ServiceEntity> result = serviceRepository.findAll(pageable);
        System.out.println("Found " + result.getTotalElements() + " services");
        return result;
    }

    Page<ServiceEntity> result = serviceRepository.searchServicesPageable(
        StringUtils.hasText(name) ? name : null,
        categoryId,
        pageable
    );
    
    System.out.println("Found " + result.getTotalElements() + " filtered services");
    return result;
}

    @Transactional(readOnly = true)
    public Page<ServiceEntity> findAllWithPagination(int page, int size) {
        return serviceRepository.findAll(PageRequest.of(page, size));
    }

   @Transactional(readOnly = true)
public List<ServiceEntity> searchServices(String name, Integer categoryId) {
    String searchName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
    Integer searchCategoryId = (categoryId != null && categoryId > 0) ? categoryId : null;
    
    return serviceRepository.searchServices(searchName, searchCategoryId);
}

    public ServiceEntity updateService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
public List<ServiceEntity> findByName(String name) {
    return serviceRepository.findByNameContainingIgnoreCase(name);
}

@Transactional(readOnly = true)
public List<ServiceEntity> findByCategoryId(Integer categoryId) {
    return serviceRepository.findByCategoryId(categoryId);
}

@Transactional(readOnly = true)
public List<ServiceModel> getAllServices() {
    return serviceRepository.findAll()
            .stream()
            .map(this::convertToModel)
            .collect(Collectors.toList());
}

@Transactional(readOnly = true)
public ServiceModel getServiceById(Integer id) {
    return serviceRepository.findById(id)
            .map(this::convertToModel)
            .orElse(null);
}

private ServiceModel convertToModel(ServiceEntity entity) {
    ServiceModel model = new ServiceModel();
    model.setServicesId(entity.getServicesId());
    model.setName(entity.getName());
    model.setDescription(entity.getDescription());
    model.setPrice(entity.getPrice());
    if (entity.getCategory() != null) {
        model.setCategoryId(entity.getCategory().getCategoryId());
        model.setCategoryName(entity.getCategory().getCategoryName());
    }
    return model;
}
}