/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.List;
import ml.bma.bsop.backend.data.BSOPProductionException;
import java.util.Optional;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.AccountRepository;
import ml.bma.bsop.backend.data.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ironman
 */


@Service
public class AccountService extends CrudService<Account> {

    private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
    private static final Account ADMIN = new Account("zelatko@seznam.cz",Authority.ROLE_ADMIN);
    
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        if(accountRepository.findOneByEmail(ADMIN.getEmail()) == null) {
            ADMIN.setPassword("password");
            ADMIN.setEnable(true);
            accountRepository.save(ADMIN);
        }
    	this.accountRepository = accountRepository;
	this.passwordEncoder = passwordEncoder;
    }

    public Account findOneByEmail(String email) {
	return getRepository().findOneByEmail(email);
    }
    
    public boolean existsEmail(String email) {        
        return null != findOneByEmail(email);
    }

    @Override
    public List<Account> findAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByEmailLikeIgnoreCase(repositoryFilter);
	} else {
            return getRepository().findAll();
	}
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
                String repositoryFilter = "%" + filter.get() + "%";
                return getRepository().countByEmailLikeIgnoreCase(repositoryFilter);
        } else {
                return getRepository().count();
        }
    }

    @Override
    protected final AccountRepository getRepository() {
        return this.accountRepository;
    }

    public String encodePassword(String value) {
        return this.passwordEncoder.encode(value);
    }

    @Override
    @Transactional
    public Account save(Account entity) {
        throwIfAccountLocked(entity.getId());
        return super.save(entity);
    }

    @Override
    @Transactional
    public void delete(long accountId) {
        throwIfAccountLocked(accountId);
        super.delete(accountId);
    }

    private void throwIfAccountLocked(Long accountId) {
        if (accountId == null) {
                return;
        }

        Account account = getRepository().findOne(accountId);
        if (account.isLocked()) {
                throw new BSOPProductionException(MODIFY_LOCKED_USER_NOT_PERMITTED);
        }
    }

    @Override
    public List<Account> findAll() {
        return getRepository().findAll();
    }

}
