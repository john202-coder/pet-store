package pet.store.service;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

    @Autowired
    private PetStoreDao petStoreDao;

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
            return petStoreDao.findById(petStoreId)
                .orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " not found"));
        }
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
            customer.setCustomerFirstName(customerData.getCustomerFirstName());
            customer.setCustomerLastName(customerData.getCustomerLastName());
            customer.setCustomerEmail(customerData.getCustomerEmail());
            customers.add(customer);
        }
        petStore.setCustomers(customers);

        Set<Employee> employees = new LinkedHashSet<>();
        for (PetStoreEmployee employeeData : petStoreData.getEmployees()) {
            Employee employee = new Employee();
            employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
            employee.setEmployeeLastName(employeeData.getEmployeeLastName());
            employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());
            employee.setEmployeePhone(employeeData.getEmployeePhone());
            employees.add(employee);
        }
        petStore.setEmployees(employees);
    }
}
