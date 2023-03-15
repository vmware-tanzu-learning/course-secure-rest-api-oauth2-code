package example.cashcard;

import org.springframework.data.annotation.Id;

/**
 * The cash card entity.
 *
 * <p>Note that when using Spring Data JDBC, it is not necessary to supply
 * the `id` when inserting a cash card record.
 *
 * @author Felipe Gutierrez
 * @author Josh Cummings
 */
public record CashCard(@Id Long id, Double amount, String owner) {
	public CashCard(Double amount, String owner) {
		this(null, amount, owner);
	}
}
