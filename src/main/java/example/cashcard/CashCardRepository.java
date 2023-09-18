package example.cashcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The cash card repository.
 *
 * <p>Spring Data JDBC provides the implementation for this interface
 * at runtime.
 *
 * @author Felipe Gutierrez
 * @author Josh Cummings
 */
public interface CashCardRepository extends CrudRepository<CashCard, Long> {
	Iterable<CashCard> findByOwner(String owner);

	default Iterable<CashCard> findAll(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String owner = authentication.getName();
		return findByOwner(owner);
	}
}
