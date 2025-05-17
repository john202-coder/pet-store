package pet.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

    @Autowired
    private PetStoreService petStoreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
        log.info("Received request to create pet store: {}", petStoreData);
        return petStoreService.savePetStore(petStoreData);
    }

    @PutMapping("/{petStoreId}/customer")
    @ResponseStatus(HttpStatus.OK)
    public PetStoreData addCustomerToPetStore(@PathVariable Long petStoreId,
                                               @RequestBody PetStoreCustomer customer) {
        log.info("Adding customer to pet store with ID={}: {}", petStoreId, customer);
        return petStoreService.saveCustomer(petStoreId, customer);
    }

    @PutMapping("/{petStoreId}/employee")
    @ResponseStatus(HttpStatus.OK)
    public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId,
                                                  @RequestBody PetStoreEmployee employee) {
        log.info("Adding employee to pet store with ID={}: {}", petStoreId, employee);
        return petStoreService.saveEmployee(petStoreId, employee);
    }

    @GetMapping("/{petStoreId}")
    public PetStoreData getPetStoreById(@PathVariable Long petStoreId) {
        log.info("Fetching pet store with ID={}", petStoreId);
        return new PetStoreData(petStoreService.findPetStoreById(petStoreId));
    }

    @GetMapping
    public List<PetStoreData> getAllPetStores() {
        log.info("Fetching all pet stores");
        return petStoreService.findAllPetStores();
    }

    @DeleteMapping("/{petStoreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePetStore(@PathVariable Long petStoreId) {
        log.info("Deleting pet store with ID={}", petStoreId);
        petStoreService.deletePetStoreById(petStoreId);
    }

    @DeleteMapping("/{petStoreId}/customer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCustomerFromPetStore(@PathVariable Long petStoreId, @PathVariable Long customerId) {
        log.info("Removing customer ID={} from pet store ID={}", customerId, petStoreId);
        petStoreService.removeCustomerFromPetStore(petStoreId, customerId);
    }

    @DeleteMapping("/{petStoreId}/employee/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployeeFromPetStore(@PathVariable Long petStoreId, @PathVariable Long employeeId) {
        log.info("Removing employee ID={} from pet store ID={}", employeeId, petStoreId);
        petStoreService.removeEmployeeFromPetStore(petStoreId, employeeId);
    }
}

