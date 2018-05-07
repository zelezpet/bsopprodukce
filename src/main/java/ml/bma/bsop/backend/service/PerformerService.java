/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import ml.bma.bsop.backend.AbilityRepository;
import ml.bma.bsop.backend.AddressRepository;
import ml.bma.bsop.backend.BankAccountRepository;
import ml.bma.bsop.backend.CellPhoneRepository;
import ml.bma.bsop.backend.PerformerRepository;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.BankAccount;
import ml.bma.bsop.backend.data.entity.CellPhone;
import ml.bma.bsop.backend.data.entity.Performer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */

@Service
public class PerformerService extends CrudService<Performer> {
    
    
    private final PerformerRepository performerRepository;
    private final AbilityRepository abilityRepository;
    private final AddressRepository addressRepository;
    private final CellPhoneRepository cellPhoneRepository;
    private final BankAccountRepository bankAccountRepository;
    
    @Autowired
    public PerformerService(
            PerformerRepository performerRepository, AbilityRepository abilityRepository,
            AddressRepository addressRepository, CellPhoneRepository cellPhoneRepository,
            BankAccountRepository bankAccountRepository) {
        this.performerRepository = performerRepository;
        this.abilityRepository = abilityRepository;
        this.addressRepository = addressRepository;
        this.cellPhoneRepository = cellPhoneRepository;
        this.bankAccountRepository = bankAccountRepository;
        
    }

    @Override
    protected PerformerRepository getRepository() {
        return this.performerRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        //not supported
        return 1L;
    }

    @Override
    public List<Performer> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByFirstnameLikeIgnoreCaseOrSurnameLikeIgnoreCase(repositoryFilter, repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }

    public void saveAbilities(List<Ability> newAbilities) {
        for(Ability ability: newAbilities) {
            abilityRepository.save(ability);
        }
    }
    
    public void deleteAbilities(Set<Ability> deleteAbilities) {
        for(Ability ability: deleteAbilities) {
            abilityRepository.delete(ability);
        }
    }

    public Performer findPerformer(Account current) {
        return performerRepository.findOneByAccount(current);
    }
    
    public boolean save(Performer oldPerformer, Performer newPerformer, List<Ability> savedAbilities) {
        
        boolean change;
        Set<Ability> newAbilities = new HashSet<>();
        Set<Ability> deleteAbilities = new HashSet<>();
        for(Ability newAbility: oldPerformer.getAbilities()) {
            boolean contain = false;
            for(Ability savedAbility: savedAbilities) {
                if(savedAbility.equals(newAbility)) {
                    contain = true;
                }
            }
            if(!contain) {
                newAbilities.add(newAbility);
            }
        }
        for(Ability oldAbility: savedAbilities) {
                boolean delete = true;
                for(Ability ability: oldPerformer.getAbilities()) {
                    if(ability.equals(oldAbility)) {
                        delete = false;
                    }
                }
                if(delete) {
                    deleteAbilities.add(oldAbility);
                    
                }
            }
        String firstName = oldPerformer.getFirstname();
        String surname = oldPerformer.getSurname();
        String op = oldPerformer.getOp();
        String pas = oldPerformer.getPas();
        Address addr = oldPerformer.getAddress();
        CellPhone cell = oldPerformer.getCellPhone();
        BankAccount bankAccount = oldPerformer.getBank();        
        String newFirstName = newPerformer.getFirstname();
        String newSurname = newPerformer.getSurname();
        String newOp = newPerformer.getOp();
        String newPas = newPerformer.getPas();
        Address newAddr = newPerformer.getAddress();
        CellPhone newCell = newPerformer.getCellPhone();
        BankAccount newBankAccount = newPerformer.getBank();
        change = false;
        boolean address, phone ,bank;
        address = phone = bank = false;
        if(!firstName.equalsIgnoreCase(newFirstName)) {
            oldPerformer.setFirstname(newFirstName);
            change = true;
        }
        if(!surname.equalsIgnoreCase(newSurname)) {
            oldPerformer.setSurname(newSurname);
            change = true;
        }
        if(!Objects.equals(newOp, op)) {
            oldPerformer.setOp(newOp);
            change = true;
        }
        if(!Objects.equals(newPas, pas)) {
            oldPerformer.setPas(newPas);
            change = true;
        }
        if(!newAddr.equals(addr)) {
            oldPerformer.setAddress(newAddr);
            change = address = true;
        }
        if(!newCell.equals(cell)) {
            oldPerformer.setCellPhone(newCell);
            change = phone = true;
        }
        if(!newBankAccount.equals(bankAccount)) {
            oldPerformer.setBank(newBankAccount);
            change = bank = true;
        }
              
        if(change || (newAbilities.size() > 0) || (deleteAbilities.size() > 0)) {
            super.save(oldPerformer);
            this.deleteAbilities(deleteAbilities);
            
            if(address) {
                this.addressRepository.delete(addr);
            }
            if(phone) {
                this.cellPhoneRepository.delete(cell);
            }
            if(bank) {
                this.bankAccountRepository.delete(bankAccount);
            }
            
            return true;
        }
        
        return false;
    }

    @Override
    public List<Performer> findAll() {
        return getRepository().findAll();
    }
    
    
}
