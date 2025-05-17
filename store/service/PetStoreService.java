package pet.store.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

    @Autowired
    private PetStoreDao petStoreDao;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private CustomerDao customerDao;

    @Transactional
    public PetStoreData savePetStore(PetStoreData petStoreData) {
        Long petStoreId = petStoreData.getPetStoreId();
        PetStore petStore = findOrCreatePetStore(petStoreId);
        copyPetStoreFields(petStore, petStoreData);
        petStore = petStoreDao.save(petStore);
        return new PetStoreData(petStore);
    }

    private PetStore findOrCreatePetStore(Long petStoreId) {
        if (petStoreId == null) {
            return new PetStore();
        } else {
            return findPetStoreById(petStoreId);
        }
    }

    private Customer findOrCreateCustomer(Long petStoreId, PetStoreCustomer dto) {
        if (dto.getCustomerId() != null) {
            return customerDao.findById(dto.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer with ID=" + dto.getCustomerId() + " not found."));
        }
        return new Customer();
    }

    private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
        petStore.setPetStoreName(petStoreData.getPetStoreName());
        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());

        Set<Customer> customers = new LinkedHashSet<>();
        for (PetStoreCustomer customerData : petStoreData.getCustomers()) {
            Customer customer = new Customer();
            copyCustomerFields(customer, customerData);
            customers.add(customer);
        }
        petStore.setCustomers(customers);

        Set<Employee> employees = new LinkedHashSet<>();
        for (PetStoreEmployee employeeData : petStoreData.getEmployees()) {
            Employee employee = new Employee();
            copyEmployeeFields(employee, employeeData);
            employees.add(employee);
        }
        petStore.setEmployees(employees);
    }

    public PetStore findPetStoreById(Long petStoreId) {
        return petStoreDao.findById(petStoreId)
            .orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " not found."));
    }

    public List<PetStoreData> findAllPetStores() {
        return petStoreDao.findAll().stream()
            .map(PetStoreData::new)
            .toList();
    }

    public Employee findEmployeeById(Long petStoreId, Long employeeId) {
        Employee employee = employeeDao.findById(employeeId)
            .orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " not found."));

        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee does not belong to the given pet store.");
        }

        return employee;
    }

    public Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
        if (employeeId == null) {
            return new Employee();
        }
        return findEmployeeById(petStoreId, employeeId);
    }

    public void copyEmployeeFields(Employee employee, PetStoreEmployee employeeData) {
        employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
        employee.setEmployeeLastName(employeeData.getEmployeeLastName());
        employee.setEmployeePhone(employeeData.getEmployeePhone());
        employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());
    }

    private void copyCustomerFields(Customer customer, PetStoreCustomer dto) {
        customer.setCustomerFirstName(dto.getCustomerFirstName());
        customer.setCustomerLastName(dto.getCustomerLastName());
        customer.setCustomerEmail(dto.getCustomerEmail());
    }

    @Transactional
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee employeeData) {
        PetStore petStore = findPetStoreById(petStoreId);

        Employee employee = findOrCreateEmployee(employeeData.getEmployeeId(), petStoreId);
        copyEmployeeFields(employee, employeeData);

        employee.setPetStore(petStore);
        petStore.getEmployees().add(employee);

        employee = employeeDao.save(employee);

        return new PetStoreEmployee(employee);
    }

    @Transactional
    public PetStoreData saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
        PetStore petStore = petStoreDao.findById(petStoreId)
            .orElseThrow(() -> new NoSuchElementException("Pet store with ID " + petStoreId + " not found."));

        Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer);
        copyCustomerFields(customer, petStoreCustomer);

        customer.getPetStores().add(petStore);
        petStore.getCustomers().add(customer);

        customer = customerDao.save(customer);
        petStore = petStoreDao.save(petStore);

        return new PetStoreData(petStore);
    }

    

    @Transactional
    public void deletePetStoreById(Long petStoreId) {
        PetStore petStore = findPetStoreById(petStoreId);
        petStoreDao.delete(petStore);
    }

    @Transactional
    public void removeCustomerFromPetStore(Long petStoreId, Long customerId) {
        PetStore store = findPetStoreById(petStoreId);
        Customer customer = customerDao.findById(customerId)
            .orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " not found."));

        store.getCustomers().remove(customer);
        customer.getPetStores().remove(store);

        petStoreDao.save(store);
        customerDao.save(customer);
    }

    @Transactional
    public void removeEmployeeFromPetStore(Long petStoreId, Long employeeId) {
        PetStore store = findPetStoreById(petStoreId);
        Employee employee = employeeDao.findById(employeeId)
            .orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " not found."));

        if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
            throw new IllegalArgumentException("Employee does not belong to pet store ID=" + petStoreId);
        }

        store.getEmployees().remove(employee);
        employeeDao.delete(employee);
    }
}

