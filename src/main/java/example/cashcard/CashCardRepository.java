package example.cashcard;

import org.springframework.data.repository.CrudRepository;

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
}
