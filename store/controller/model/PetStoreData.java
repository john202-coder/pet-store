package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {

    private Long petStoreId;
    private String petStoreName;
    private String petStoreAddress;
    private String petStoreCity;
    private String petStoreState;
    private String petStoreZip;
    private String petStorePhone;

    private Set<PetStoreCustomer> customers = new HashSet<>();
    private Set<PetStoreEmployee> employees = new HashSet<>();

    public PetStoreData(PetStore petStore) {
        petStoreId = petStore.getPetStoreId();
        petStoreName = petStore.getPetStoreName();
        petStoreAddress = petStore.getPetStoreAddress();
        petStoreCity = petStore.getPetStoreCity();
        petStoreState = petStore.getPetStoreState();
        petStoreZip = petStore.getPetStoreZip();
        petStorePhone = petStore.getPetStorePhone();

        petStore.getCustomers().forEach(customer -> 
            customers.add(new PetStoreCustomer(customer)));

        petStore.getEmployees().forEach(employee -> 
            employees.add(new PetStoreEmployee(employee)));
    }

    @Data
    @NoArgsConstructor
    public static class PetStoreCustomer {
        private Long customerId;
        private String customerFirstName;
        private String customerLastName;
        private String customerEmail;

        public PetStoreCustomer(pet.store.entity.Customer customer) {
            customerId = customer.getCustomerId();
            customerFirstName = customer.getCustomerFirstName();
            customerLastName = customer.getCustomerLastName();
            customerEmail = customer.getCustomerEmail();
        }
    }

    @Data
    @NoArgsConstructor
    public static class PetStoreEmployee {
        private Long employeeId;
        private String employeeFirstName;
        private String employeeLastName;
        private String employeeJobTitle;
        private String employeePhone;

        public PetStoreEmployee(pet.store.entity.Employee employee) {
            employeeId = employee.getEmployeeId();
            employeeFirstName = employee.getEmployeeFirstName();
            employeeLastName = employee.getEmployeeLastName();
            employeeJobTitle = employee.getEmployeeJobTitle();
            employeePhone = employee.getEmployeePhone();
        }
    }
}
