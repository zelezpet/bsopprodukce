/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend;

import java.util.List;
import ml.bma.bsop.backend.data.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ironman
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    public Account findOneById(long id);
    
    public Account findOneByEmail(String email);

    public long countByEmailLikeIgnoreCase(String emailLike);

    public List<Account> findByEmailLikeIgnoreCase(String repositoryFilter);
}
