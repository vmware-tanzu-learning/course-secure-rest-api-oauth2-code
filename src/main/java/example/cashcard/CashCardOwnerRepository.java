package example.cashcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CashCardOwnerRepository extends CrudRepository<CashCardOwner, Long>, PagingAndSortingRepository<CashCardOwner, Long> {

}
